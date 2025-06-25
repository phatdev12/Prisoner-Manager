import crypto from 'crypto';

export function hashPassword(
  password: string, 
  salt = crypto.randomBytes(16).toString('hex')
): { salt: string, hash: string } {
  const hash = crypto.pbkdf2Sync(password, salt, 100000, 64, 'sha512').toString('hex');
  return {
    salt,
    hash
  }
}

export function verifyPassword(
  password: string, 
  salt: string, 
  hash: string
): boolean {
  console.log(password, salt, hash);
  const newHash = crypto.pbkdf2Sync(password, salt, 100000, 64, 'sha512').toString('hex');
  return newHash === hash;
}