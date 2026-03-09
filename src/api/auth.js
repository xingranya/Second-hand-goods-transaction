export function getToken() {
  return window.localStorage.getItem("token") || "";
}

export function setToken(token) {
  if (!token) return;
  window.localStorage.setItem("token", token);
}

export function clearToken() {
  window.localStorage.removeItem("token");
}

export function hasToken() {
  return Boolean(getToken());
}
