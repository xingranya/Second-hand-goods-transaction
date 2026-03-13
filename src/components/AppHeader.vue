<template>
  <header class="header-wrap">
    <div class="page-container header-inner">
      <RouterLink class="brand" to="/">
        <span class="material-symbols-outlined">storefront</span>
        <span>校园集市</span>
      </RouterLink>
      <nav class="nav-desktop">
        <RouterLink
          v-for="item in navItems"
          :key="item.to"
          :to="item.to"
          class="nav-item"
          :class="{ active: isActive(item.to) }"
        >
          {{ item.label }}
        </RouterLink>
      </nav>
      <div class="header-tools">
        <RouterLink v-if="authed && userStore.isAdmin" class="auth-entry" to="/admin/dashboard">后台</RouterLink>
        <RouterLink class="publish-entry" to="/publish">
          <span class="material-symbols-outlined">add_circle</span>
          发布
        </RouterLink>
        <RouterLink v-if="!authed" class="auth-entry" to="/login">登录</RouterLink>
        <button v-else class="auth-entry logout" @click="logout">
          {{ displayName }}
        </button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from "vue";
import { storeToRefs } from "pinia";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const { isAuthenticated, profile } = storeToRefs(userStore);
const navItems = [
  { label: "首页", to: "/" },
  { label: "求购", to: "/wanted" },
  { label: "消息", to: "/messages" },
  { label: "我的", to: "/profile" }
];

const current = computed(() => route.path);
const authed = computed(() => isAuthenticated.value);
const displayName = computed(() => profile.value?.name || profile.value?.username || "退出");
const isActive = (target) => (target === "/" ? current.value === "/" : current.value.startsWith(target));

function logout() {
  userStore.logout();
  router.push("/login");
}
</script>

<style scoped>
.header-wrap {
  position: sticky;
  top: 0;
  z-index: 40;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.85);
  border-bottom: 1px solid var(--border);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 64px;
  gap: 12px;
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-weight: 800;
  color: var(--primary);
}

.nav-desktop {
  display: inline-flex;
  gap: 6px;
  background: var(--surface-soft);
  border-radius: 999px;
  padding: 6px;
}

.nav-item {
  padding: 8px 14px;
  border-radius: 999px;
  color: var(--muted);
  font-size: 14px;
  font-weight: 700;
}

.nav-item.active {
  background: var(--surface);
  color: var(--primary);
  box-shadow: inset 0 0 0 1px var(--border);
}

.header-tools {
  display: flex;
  align-items: center;
  gap: 8px;
}

.publish-entry {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #fff;
  background: linear-gradient(135deg, #ff8b3d, var(--accent));
  border-radius: 10px;
  padding: 8px 12px;
  font-size: 13px;
  font-weight: 700;
}

.auth-entry {
  border-radius: 10px;
  border: 1px solid var(--border);
  background: var(--surface);
  color: var(--primary);
  padding: 8px 12px;
  font-size: 13px;
  font-weight: 700;
}

.logout {
  cursor: pointer;
}

@media (max-width: 720px) {
  .nav-desktop {
    display: none;
  }
}
</style>
