export const mockConversations = [
  {
    id: "c-3001",
    peerUser: { id: "u-3001", name: "李晓敏", online: true },
    lastMessage: "学长，这本资料还在吗？",
    unreadCount: 1,
    orderId: "o-4001"
  },
  {
    id: "c-3002",
    peerUser: { id: "u-3002", name: "王哲", online: false },
    lastMessage: "下午三点图书馆见",
    unreadCount: 0,
    orderId: "o-4002"
  }
];

export const mockMessagesByConversation = {
  "c-3001": [
    { id: "m-1", from: "peer", content: "学长，这本资料还在吗？", time: "14:12" },
    { id: "m-2", from: "me", content: "还在，可以主校区面交。", time: "14:14" }
  ],
  "c-3002": [{ id: "m-3", from: "peer", content: "下午三点图书馆见", time: "10:40" }]
};
