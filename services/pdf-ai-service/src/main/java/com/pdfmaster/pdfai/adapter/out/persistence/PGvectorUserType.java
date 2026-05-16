package com.pdfmaster.pdfai.adapter.out.persistence;

import com.pgvector.PGvector;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * Bridges Hibernate to pgvector. Stored as the literal text representation
 * (e.g. {@code "[0.1,0.2,...]"}) and bound with explicit {@code ::vector} cast via
 * {@link Types#OTHER}; Postgres pgvector accepts that form on input.
 */
public class PGvectorUserType implements UserType<PGvector> {

  @Override
  public int getSqlType() {
    return Types.OTHER;
  }

  @Override
  public Class<PGvector> returnedClass() {
    return PGvector.class;
  }

  @Override
  public boolean equals(PGvector x, PGvector y) {
    if (x == y) return true;
    if (x == null || y == null) return false;
    return Arrays.equals(x.toArray(), y.toArray());
  }

  @Override
  public int hashCode(PGvector x) {
    return x == null ? 0 : Arrays.hashCode(x.toArray());
  }

  @Override
  public PGvector nullSafeGet(
      ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
      throws SQLException {
    String raw = rs.getString(position);
    if (raw == null) return null;
    try {
      return new PGvector(raw);
    } catch (SQLException ex) {
      throw new SQLException("Failed to parse pgvector literal: " + raw, ex);
    }
  }

  @Override
  public void nullSafeSet(
      PreparedStatement st, PGvector value, int index, SharedSessionContractImplementor session)
      throws SQLException {
    if (value == null) {
      st.setNull(index, Types.OTHER);
    } else {
      st.setObject(index, value.toString(), Types.OTHER);
    }
  }

  @Override
  public PGvector deepCopy(PGvector value) {
    return value == null ? null : new PGvector(Arrays.copyOf(value.toArray(), value.toArray().length));
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(PGvector value) {
    return value == null ? null : value.toString();
  }

  @Override
  public PGvector assemble(Serializable cached, Object owner) {
    if (cached == null) return null;
    try {
      return new PGvector((String) cached);
    } catch (SQLException ex) {
      throw new IllegalStateException("Failed to assemble PGvector from cache", ex);
    }
  }
}
