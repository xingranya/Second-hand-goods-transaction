<template>
  <div class="admin-shell">
    <aside class="admin-sidebar">
      <div class="admin-brand-block">
        <RouterLink class="admin-brand" to="/admin/dashboard">管理中心</RouterLink>
        <p>用于查看平台统计、商品信息、用户状态与订单数据。</p>
      </div>
      <nav class="admin-nav">
        <RouterLink
          v-for="item in navItems"
          :key="item.to"
          :to="item.to"
          class="nav-link"
          :class="{ active: route.path.startsWith(item.to) }"
        >
          <span class="material-symbols-outlined nav-icon">{{ item.icon }}</span>
          <span class="nav-copy">
            <strong>{{ item.label }}</strong>
            <small>{{ item.desc }}</small>
          </span>
        </RouterLink>
      </nav>
      <div class="admin-bottom">
        <p>{{ userStore.profile?.name || userStore.profile?.username || "管理员" }}</p>
        <div class="admin-actions">
          <RouterLink class="btn btn-muted back-link" to="/">返回前台</RouterLink>
          <button class="btn btn-muted" @click="logout">退出登录</button>
        </div>
      </div>
    </aside>
    <main class="admin-main">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

const navItems = [
  { label: "数据概览", desc: "查看平台关键统计", icon: "dashboard", to: "/admin/dashboard" },
  { label: "商品管理", desc: "处理商品状态与信息", icon: "inventory_2", to: "/admin/products" },
  { label: "用户管理", desc: "维护账号与认证状态", icon: "group", to: "/admin/users" },
  { label: "订单管理", desc: "跟踪订单与交易状态", icon: "receipt_long", to: "/admin/orders" }
];

function logout() {
  userStore.logout();
  router.push("/admin/login");
}
</script>

<style scoped>
.admin-shell {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 240px 1fr;
  background: linear-gradient(180deg, #f7faff 0%, #eef4fb 100%);
}

.admin-sidebar {
  padding: 24px 18px;
  border-right: 1px solid var(--border);
  background: rgba(255, 255, 255, 0.84);
  backdrop-filter: blur(18px);
  display: grid;
  grid-template-rows: auto 1fr auto;
  gap: 18px;
}

.admin-brand-block p {
  margin: 8px 0 0;
  color: var(--muted);
  font-size: 13px;
  line-height: 1.7;
}

.admin-brand {
  font-family: var(--font-display);
  font-size: 28px;
  color: var(--primary-strong);
}

.admin-nav {
  display: grid;
  gap: 8px;
  align-content: start;
  grid-auto-rows: min-content;
}

.nav-link {
  display: grid;
  grid-template-columns: 18px 1fr;
  gap: 12px;
  align-items: start;
  padding: 14px 14px;
  border-radius: 18px;
  color: var(--muted);
  font-weight: 700;
  border: 1px solid transparent;
  transition: background 0.2s ease, border-color 0.2s ease, transform 0.2s ease, color 0.2s ease;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.7);
  border-color: var(--border);
  transform: translateX(2px);
}

.nav-link.active {
  background: linear-gradient(135deg, rgba(36, 83, 199, 0.12), rgba(36, 83, 199, 0.04));
  color: var(--primary);
  border-color: rgba(36, 83, 199, 0.16);
  box-shadow: inset 0 0 0 1px rgba(36, 83, 199, 0.06);
}

.nav-icon {
  font-size: 20px;
  margin-top: 1px;
}

.nav-copy {
  display: grid;
  gap: 4px;
}

.nav-copy strong {
  font-size: 16px;
  line-height: 1.2;
}

.nav-copy small {
  color: var(--muted);
  font-size: 12px;
  font-weight: 600;
  line-height: 1.5;
}

.nav-link.active .nav-copy small {
  color: rgba(23, 58, 143, 0.78);
}

.admin-bottom p {
  margin: 0 0 10px;
  color: var(--muted);
  font-size: 13px;
}

.admin-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.back-link {
  text-align: center;
}

.admin-main {
  padding: 24px;
}

@media (max-width: 960px) {
  .admin-shell {
    grid-template-columns: 1fr;
  }

  .admin-sidebar {
    grid-template-rows: auto;
  }

  .admin-nav {
    grid-template-columns: 1fr;
  }
}
</style>
