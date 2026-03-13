export function resolveRedirectTarget(rawRedirect, fallback = "/profile") {
  if (typeof rawRedirect !== "string") {
    return fallback;
  }
  if (!rawRedirect.startsWith("/") || rawRedirect.startsWith("//")) {
    return fallback;
  }
  if (rawRedirect.startsWith("/login")) {
    return fallback;
  }
  return rawRedirect;
}

export function createLoginLocation(route, fallback = "/profile") {
  return {
    path: "/login",
    query: {
      redirect: resolveRedirectTarget(route?.fullPath, fallback)
    }
  };
}

export function createAdminLoginLocation(route, fallback = "/admin/dashboard") {
  return {
    path: "/admin/login",
    query: {
      redirect: resolveRedirectTarget(route?.fullPath, fallback)
    }
  };
}
