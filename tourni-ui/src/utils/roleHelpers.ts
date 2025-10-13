/**
 * Helper functions for role-based access control
 */

/**
 * Parse roles from comma-separated string
 * @param userRoles - Comma-separated roles string or null
 * @returns Array of trimmed, uppercase role names
 */
const parseRoles = (userRoles: string | null | undefined): string[] => {
  if (!userRoles || typeof userRoles !== 'string') return [];
  
  return userRoles
    .split(',')
    .map(role => role.trim().toUpperCase())
    .filter(role => role.length > 0);
};

/**
 * Check if user has a specific role
 * @param userRoles - Comma-separated roles string or null
 * @param requiredRole - Role to check for
 * @returns true if user has the role
 */
export const hasRole = (userRoles: string | null | undefined, requiredRole: string): boolean => {
  if (!requiredRole) return false;
  
  const roles = parseRoles(userRoles);
  return roles.includes(requiredRole.trim().toUpperCase());
};

/**
 * Check if user has any of the specified roles
 * @param userRoles - Comma-separated roles string or null
 * @param requiredRoles - Array of roles to check
 * @returns true if user has at least one of the roles
 */
export const hasAnyRole = (userRoles: string | null | undefined, requiredRoles: string[]): boolean => {
  if (!requiredRoles || requiredRoles.length === 0) return false;
  
  const roles = parseRoles(userRoles);
  const normalizedRequired = requiredRoles.map(r => r.trim().toUpperCase());
  
  return normalizedRequired.some(requiredRole => roles.includes(requiredRole));
};

/**
 * Check if user has all of the specified roles
 * @param userRoles - Comma-separated roles string or null
 * @param requiredRoles - Array of roles to check
 * @returns true if user has all of the roles
 */
export const hasAllRoles = (userRoles: string | null | undefined, requiredRoles: string[]): boolean => {
  if (!requiredRoles || requiredRoles.length === 0) return false;
  
  const roles = parseRoles(userRoles);
  const normalizedRequired = requiredRoles.map(r => r.trim().toUpperCase());
  
  return normalizedRequired.every(requiredRole => roles.includes(requiredRole));
};

/**
 * Check if user has ADMIN role
 * @param userRoles - Comma-separated roles string or null
 * @returns true if user is an admin
 */
export const isAdmin = (userRoles: string | null | undefined): boolean => {
  return hasRole(userRoles, 'ADMIN');
};

/**
 * Check if user has USER role
 * @param userRoles - Comma-separated roles string or null
 * @returns true if user is a regular user
 */
export const isUser = (userRoles: string | null | undefined): boolean => {
  return hasRole(userRoles, 'USER');
};

