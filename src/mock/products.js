export const mockProducts = [
  {
    id: "p-1001",
    title: "MacBook Pro 13 (M1) 8+256，成色极佳",
    price: 5499,
    originPrice: 8999,
    condition: "95新",
    campus: "主校区",
    images: ["https://images.unsplash.com/photo-1517336714739-489689fd1ca8?w=1200"],
    tags: ["优先自提", "学生优惠"],
    publishTime: "2小时前",
    sellerId: "u-2001"
  },
  {
    id: "p-1002",
    title: "考研数学全套资料（新版）",
    price: 120,
    originPrice: 368,
    condition: "全新",
    campus: "东校区",
    images: ["https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=1200"],
    tags: ["可刀", "当天发货"],
    publishTime: "15分钟前",
    sellerId: "u-2002"
  },
  {
    id: "p-1003",
    title: "捷安特山地自行车，通勤神器",
    price: 450,
    originPrice: 1699,
    condition: "9成新",
    campus: "西校区",
    images: ["https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=1200"],
    tags: ["仅面交"],
    publishTime: "昨天",
    sellerId: "u-2003"
  }
];

export const mockProductDetail = {
  ...mockProducts[0],
  description:
    "因为升级设备，所以出售这台笔记本。电池循环次数低，屏幕无坏点，支持当面验机。配件齐全，价格可小刀。",
  seller: {
    id: "u-2001",
    name: "Alex Johnson",
    verified: true,
    major: "计算机科学",
    credit: 780,
    deals: 24,
    rating: "100%"
  },
  qa: [
    { id: "qa-1", user: "Sarah W.", time: "2小时前", content: "电池健康度还是100%吗？" },
    { id: "qa-2", user: "卖家", time: "1小时前", content: "健康度是94%，日常学习够用。" }
  ]
};
