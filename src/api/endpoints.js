export const productEndpoints = {
  list: [
    { method: "get", url: "/products" },
    { method: "get", url: "/product/list" },
    { method: "get", url: "/goods" },
    { method: "get", url: "/goods/list" }
  ],
  detail: (id) => [
    { method: "get", url: `/products/${id}` },
    { method: "get", url: `/product/${id}` },
    { method: "get", url: `/goods/${id}` },
    { method: "get", url: "/product/detail", params: { id } },
    { method: "get", url: "/goods/detail", params: { id } }
  ],
  create: [
    { method: "post", url: "/products" },
    { method: "post", url: "/product" },
    { method: "post", url: "/goods" },
    { method: "post", url: "/goods/add" }
  ]
};

export const messageEndpoints = {
  conversations: [
    { method: "get", url: "/messages/conversations" },
    { method: "get", url: "/message/conversations" },
    { method: "get", url: "/chat/conversations" },
    { method: "get", url: "/chat/list" }
  ],
  detail: (id) => [
    { method: "get", url: `/messages/${id}` },
    { method: "get", url: `/message/${id}` },
    { method: "get", url: `/chat/${id}` },
    { method: "get", url: "/chat/detail", params: { id } }
  ],
  send: (id, content) => [
    { method: "post", url: `/messages/${id}`, data: { content } },
    { method: "post", url: `/message/${id}`, data: { content } },
    { method: "post", url: `/chat/${id}`, data: { content } },
    { method: "post", url: "/chat/send", data: { conversationId: id, content } }
  ]
};

export const orderEndpoints = {
  detail: (id) => [
    { method: "get", url: `/orders/${id}` },
    { method: "get", url: `/order/${id}` },
    { method: "get", url: "/order/detail", params: { id } },
    { method: "get", url: "/orders/detail", params: { id } }
  ]
};

export const userEndpoints = {
  me: [
    { method: "get", url: "/users/me" },
    { method: "get", url: "/user/me" },
    { method: "get", url: "/user/current" },
    { method: "get", url: "/user/info" }
  ],
  verify: [
    { method: "post", url: "/users/verify" },
    { method: "post", url: "/user/verify" },
    { method: "post", url: "/auth/verify" }
  ],
  login: [
    { method: "post", url: "/auth/login" },
    { method: "post", url: "/user/login" },
    { method: "post", url: "/login" }
  ]
};
