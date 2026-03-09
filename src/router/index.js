import { createRouter, createWebHistory } from "vue-router";
import MainLayout from "@/layouts/MainLayout.vue";
import HomePage from "@/views/HomePage.vue";
import SearchPage from "@/views/SearchPage.vue";
import ProductPage from "@/views/ProductPage.vue";
import PublishPage from "@/views/PublishPage.vue";
import MessagesPage from "@/views/MessagesPage.vue";
import WantedPage from "@/views/WantedPage.vue";
import OrderPage from "@/views/OrderPage.vue";
import OrderPayPage from "@/views/OrderPayPage.vue";
import OrderReviewPage from "@/views/OrderReviewPage.vue";
import RecentOrdersPage from "@/views/RecentOrdersPage.vue";
import ProfilePage from "@/views/ProfilePage.vue";
import VerifyPage from "@/views/VerifyPage.vue";
import LoginPage from "@/views/LoginPage.vue";
import NotFoundPage from "@/views/NotFoundPage.vue";

const routes = [
  {
    path: "/",
    component: MainLayout,
    children: [
      { path: "", name: "home", component: HomePage },
      { path: "search", name: "search", component: SearchPage },
      { path: "product/:id", name: "product", component: ProductPage },
      { path: "publish", name: "publish", component: PublishPage },
      { path: "messages", name: "messages", component: MessagesPage },
      { path: "wanted", name: "wanted", component: WantedPage },
      { path: "order/:id", name: "order", component: OrderPage },
      { path: "order/:id/pay", name: "orderPay", component: OrderPayPage },
      { path: "order/:id/review", name: "orderReview", component: OrderReviewPage },
      { path: "orders/recent", name: "recentOrders", component: RecentOrdersPage },
      { path: "profile", name: "profile", component: ProfilePage },
      { path: "verify", name: "verify", component: VerifyPage },
      { path: "login", name: "login", component: LoginPage }
    ]
  },
  { path: "/:pathMatch(.*)*", name: "notFound", component: NotFoundPage }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 };
  }
});

export default router;
