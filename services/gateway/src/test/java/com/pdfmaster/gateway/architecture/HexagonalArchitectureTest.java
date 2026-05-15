package com.pdfmaster.gateway.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

/**
 * Enforces hexagonal architecture boundaries for the gateway module.
 *
 * <p>These rules are deliberately strict: the domain must remain framework-free, the application
 * layer may not reach into adapters, and naming conventions in {@code adapter.in} / {@code config}
 * are mechanically enforced.
 */
class HexagonalArchitectureTest {

  private static final JavaClasses CLASSES =
      new ClassFileImporter()
          .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
          .importPackages("com.pdfmaster.gateway");

  @Test
  void domainHasNoSpringJpaOrAdapterDependencies() {
    noClasses()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            "org.springframework..", "jakarta.persistence..", "javax.persistence..", "..adapter..")
        .allowEmptyShould(true)
        .check(CLASSES);
  }

  @Test
  void applicationDoesNotDependOnAdapters() {
    noClasses()
        .that()
        .resideInAPackage("..application..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter..")
        .allowEmptyShould(true)
        .check(CLASSES);
  }

  @Test
  void inboundAdaptersFollowNamingConvention() {
    ArchRuleDefinition.classes()
        .that()
        .resideInAPackage("..adapter.in..")
        .and()
        .areNotAnonymousClasses()
        .and()
        .areNotMemberClasses()
        .and()
        .areNotRecords()
        .and()
        .haveSimpleNameNotEndingWith("package-info")
        .should()
        .haveSimpleNameEndingWith("Controller")
        .orShould()
        .haveSimpleNameEndingWith("Filter")
        .orShould()
        .haveSimpleNameEndingWith("WebFilter")
        .check(CLASSES);
  }

  @Test
  void configClassesFollowNamingConvention() {
    ArchRuleDefinition.classes()
        .that()
        .resideInAPackage("..config..")
        .and()
        .areNotAnonymousClasses()
        .and()
        .areNotMemberClasses()
        .and()
        .haveSimpleNameNotEndingWith("package-info")
        .should()
        .haveSimpleNameEndingWith("Config")
        .orShould()
        .haveSimpleNameEndingWith("Properties")
        .check(CLASSES);
  }
}
