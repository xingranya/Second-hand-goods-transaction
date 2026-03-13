export const productEndpoints = {
  list: [{ method: "get", url: "/products" }],
  detail: (id) => [{ method: "get", url: `/products/${id}` }],
  create: [{ method: "post", url: "/products" }]
};

export const systemEndpoints = {
  summary: [{ method: "get", url: "/system/summary" }]
};

export const messageEndpoints = {
  conversations: [{ method: "get", url: "/messages/conversations" }],
  detail: (peerUserId) => [{ method: "get", url: `/messages/conversations/${peerUserId}` }],
  send: (peerUserId, payload) => [
    { method: "post", url: `/messages/conversations/${peerUserId}`, data: payload }
  ]
};

export const orderEndpoints = {
  create: [{ method: "post", url: "/orders" }],
  detail: (id) => [{ method: "get", url: `/orders/${id}` }],
  my: [{ method: "get", url: "/orders/my" }],
  nextStep: (id) => [{ method: "post", url: `/orders/${id}/next-step` }]
};

export const wantedEndpoints = {
  list: [{ method: "get", url: "/wanted" }],
  create: [{ method: "post", url: "/wanted" }]
};

export const userEndpoints = {
  me: [{ method: "get", url: "/users/me" }],
  verify: [{ method: "post", url: "/users/verify" }],
  login: [{ method: "post", url: "/auth/login" }]
};

export const reviewEndpoints = {
  create: [{ method: "post", url: "/reviews" }]
};

export const adminEndpoints = {
  stats: [{ method: "get", url: "/admin/dashboard/stats" }],
  products: [{ method: "get", url: "/admin/products" }],
  productStatus: (id, status) => [{ method: "patch", url: `/admin/products/${id}/status`, params: { status } }],
  productDelete: (id) => [{ method: "delete", url: `/admin/products/${id}` }],
  users: [{ method: "get", url: "/admin/users" }],
  userEnabled: (id, enabled) => [{ method: "patch", url: `/admin/users/${id}/enabled`, params: { enabled } }],
  userVerify: (id, verified) => [{ method: "patch", url: `/admin/users/${id}/verify`, params: { verified } }],
  orders: [{ method: "get", url: "/admin/orders" }]
};
