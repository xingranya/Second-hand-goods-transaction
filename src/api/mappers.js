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
    id: String(pick(raw, ["id", "productId", "goodsId", "commodityId"], "")),
    title: pick(raw, ["title", "name", "goodsName", "productName"], "未命名商品"),
    price: Number(pick(raw, ["price", "currentPrice", "sellPrice", "secondPrice"], 0)),
    originPrice: Number(pick(raw, ["originPrice", "originalPrice", "purchasePrice"], 0)),
    condition: pick(raw, ["condition", "quality", "goodsStatus"], "未知成色"),
    campus: pick(raw, ["campus", "campusName", "location", "schoolArea"], "主校区"),
    images: pick(raw, ["images", "imageList", "imgList", "pictureList"], []),
    tags: pick(raw, ["tags", "tagList", "labels"], []),
    publishTime: pick(raw, ["publishTime", "createdAt", "createTime", "gmtCreate"], ""),
    sellerId: String(pick(raw, ["sellerId", "userId", "publisherId", "ownerId"], ""))
  };
}

export function mapOrder(raw = {}) {
  return {
    id: String(pick(raw, ["id", "orderId"], "")),
    status: pick(raw, ["status", "orderStatus", "stateName"], "已下单"),
    items: pick(raw, ["items", "orderItems"], []),
    totalAmount: Number(pick(raw, ["totalAmount", "amount", "payAmount"], 0)),
    buyer: pick(raw, ["buyer"], {}),
    seller: pick(raw, ["seller"], {}),
    payMethod: pick(raw, ["payMethod", "paymentMethod", "payTypeName"], "平台担保"),
    createTime: pick(raw, ["createTime", "createdAt", "gmtCreate"], "")
  };
}

export function mapConversation(raw = {}) {
  return {
    id: String(pick(raw, ["id", "conversationId", "sessionId"], "")),
    peerUser: pick(raw, ["peerUser", "targetUser", "toUser", "contactUser"], {}),
    lastMessage: pick(raw, ["lastMessage", "latestMessage", "messageContent"], ""),
    unreadCount: Number(pick(raw, ["unreadCount"], 0)),
    messages: pick(raw, ["messages"], [])
  };
}

export function mapWantedPost(raw = {}) {
  return {
    id: String(pick(raw, ["id", "wantedId", "purchaseId"], "")),
    title: pick(raw, ["title"], "未命名求购"),
    expectedPrice: pick(raw, ["expectedPrice", "price", "targetPrice"], ""),
    deadline: pick(raw, ["deadline", "wantedTime", "expireTime"], ""),
    description: pick(raw, ["description", "content", "remark"], ""),
    campus: pick(raw, ["campus", "campusName"], "主校区"),
    publisher: pick(raw, ["publisher", "user"], {}),
    publishTime: pick(raw, ["publishTime", "createdAt", "createTime"], "")
  };
}
