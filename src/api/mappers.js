function pick(obj, keys, fallback = "") {
  for (const key of keys) {
    if (obj && obj[key] !== undefined && obj[key] !== null) {
      return obj[key];
    }
  }
  return fallback;
}

export function mapProduct(raw = {}) {
  return {
    id: String(pick(raw, ["id", "productId"], "")),
    title: pick(raw, ["title", "name"], "未命名商品"),
    price: Number(pick(raw, ["price", "currentPrice"], 0)),
    originPrice: Number(pick(raw, ["originPrice", "originalPrice"], 0)),
    condition: pick(raw, ["condition", "quality"], "未知成色"),
    campus: pick(raw, ["campus", "campusName", "location"], "主校区"),
    images: pick(raw, ["images", "imageList"], []),
    tags: pick(raw, ["tags", "tagList"], []),
    publishTime: pick(raw, ["publishTime", "createdAt"], ""),
    sellerId: String(pick(raw, ["sellerId", "userId"], ""))
  };
}

export function mapOrder(raw = {}) {
  return {
    id: String(pick(raw, ["id", "orderId"], "")),
    status: pick(raw, ["status"], "已下单"),
    items: pick(raw, ["items", "orderItems"], []),
    totalAmount: Number(pick(raw, ["totalAmount", "amount"], 0)),
    buyer: pick(raw, ["buyer"], {}),
    seller: pick(raw, ["seller"], {}),
    payMethod: pick(raw, ["payMethod", "paymentMethod"], "平台担保"),
    createTime: pick(raw, ["createTime", "createdAt"], "")
  };
}

export function mapConversation(raw = {}) {
  return {
    id: String(pick(raw, ["id", "conversationId"], "")),
    peerUser: pick(raw, ["peerUser", "targetUser"], {}),
    lastMessage: pick(raw, ["lastMessage"], ""),
    unreadCount: Number(pick(raw, ["unreadCount"], 0)),
    messages: pick(raw, ["messages"], [])
  };
}

export function mapWantedPost(raw = {}) {
  return {
    id: String(pick(raw, ["id", "wantedId"], "")),
    title: pick(raw, ["title"], "未命名求购"),
    expectedPrice: pick(raw, ["expectedPrice", "price"], ""),
    deadline: pick(raw, ["deadline", "wantedTime"], ""),
    description: pick(raw, ["description", "content"], ""),
    campus: pick(raw, ["campus", "campusName"], "主校区"),
    publisher: pick(raw, ["publisher", "user"], {}),
    publishTime: pick(raw, ["publishTime", "createdAt"], "")
  };
}
