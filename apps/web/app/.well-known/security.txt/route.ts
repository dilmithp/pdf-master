/**
 * RFC 9116: /.well-known/security.txt
 * Mirrors /security.txt so both canonical paths work.
 */
export { GET, dynamic } from '@/app/security.txt/route';
