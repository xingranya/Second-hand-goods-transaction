function pick(obj, keys, fallback = "") {
  for (const key of keys) {
    if (obj && obj[key] !== undefined && obj[key] !== null) {
      return obj[key];
    }
  }
  return fallback;
}

function normalizeImages(raw) {
  const list = pick(raw, ["images", "imageList", "imgList", "pictureList"], null);
  if (Array.isArray(list) && list.length) {
    return list;
  }
  const imageUrl = pick(raw, ["imageUrl", "cover", "image"], "");
  return imageUrl ? [imageUrl] : [];
}

function normalizeTags(raw) {
  const tags = pick(raw, ["tags", "tagList", "labels"], null);
  if (Array.isArray(tags)) {
    return tags;
  }
  const category = pick(raw, ["category"], "");
  return category ? [category] : [];
}

function normalizeOrderStatus(status) {
  const map = {
    PENDING: "已下单",
    COMPLETED: "待评价",
    CANCELLED: "已取消"
  };
  return map[status] || status || "已下单";
}

export function mapProduct(raw = {}) {
  const seller = pick(raw, ["seller"], {});
  return {
    id: String(pick(raw, ["id", "productId", "goodsId", "commodityId"], "")),
    title: pick(raw, ["title", "name", "goodsName", "productName"], "未命名商品"),
    price: Number(pick(raw, ["price", "currentPrice", "sellPrice", "secondPrice"], 0)),
    originPrice: Number(pick(raw, ["originPrice", "originalPrice", "purchasePrice"], 0)),
    condition: pick(raw, ["condition", "quality", "goodsStatus"], "未知成色"),
    campus: pick(raw, ["campus", "campusName", "location", "schoolArea", "school"], "主校区"),
    images: normalizeImages(raw),
    tags: normalizeTags(raw),
    publishTime: pick(raw, ["publishTime", "createdAt", "createTime", "gmtCreate"], ""),
    sellerId: String(pick(raw, ["sellerId", "userId", "publisherId", "ownerId", "seller.id"], seller?.id || "")),
    seller: {
      id: seller?.id || "",
      name: seller?.name || seller?.username || "未知卖家",
      credit: seller?.credit ?? 0
    },
    description: pick(raw, ["description", "content", "remark"], "")
  };
}

export function mapOrder(raw = {}) {
  const amount = Number(pick(raw, ["totalAmount", "amount", "payAmount"], 0));
  const items = pick(raw, ["items", "orderItems"], []).map((item, index) => ({
    id: String(pick(item, ["id", "productId"], `${index}`)),
    title: pick(item, ["title", "name", "productName"], "未命名商品"),
    count: Number(pick(item, ["count", "quantity"], 1)),
    price: Number(pick(item, ["price", "unitPrice"], 0))
  }));
  return {
    id: String(pick(raw, ["id", "orderId"], "")),
    status: normalizeOrderStatus(pick(raw, ["status", "orderStatus", "stateName"], "已下单")),
    items,
    totalAmount: amount,
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
