export const mockOrders = {
  "o-4001": {
    id: "o-4001",
    status: "待收货",
    items: [{ id: "p-1002", title: "Advanced Engineering Mathematics", count: 1, price: 45 }],
    totalAmount: 45,
    buyer: { id: "u-5001", name: "你" },
    seller: { id: "u-3001", name: "李晓敏" },
    payMethod: "平台资金担保",
    createTime: "2026-03-09 14:32"
  },
  "o-4002": {
    id: "o-4002",
    status: "待评价",
    items: [{ id: "p-1003", title: "捷安特山地自行车", count: 1, price: 450 }],
    totalAmount: 450,
    buyer: { id: "u-5001", name: "你" },
    seller: { id: "u-3002", name: "王哲" },
    payMethod: "校内面交",
    createTime: "2026-03-08 12:10"
  }
};
