export function getToken() {
  return window.localStorage.getItem("token") || "";
}

export function setToken(token) {
  if (!token) return;
  window.localStorage.setItem("token", token);
  window.dispatchEvent(new Event("auth-changed"));
}

export function clearToken() {
  window.localStorage.removeItem("token");
  window.dispatchEvent(new Event("auth-changed"));
}

export function hasToken() {
  return Boolean(getToken());
}
