<template>
  <nav class="mobile-tabs">
    <RouterLink v-for="item in tabs" :key="item.to" :to="item.to" class="tab-item" :class="{ active: isActive(item.to) }">
      <span class="material-symbols-outlined">{{ item.icon }}</span>
      <span>{{ item.label }}</span>
    </RouterLink>
  </nav>
</template>

<script setup>
import { computed } from "vue";
import { RouterLink, useRoute } from "vue-router";

const route = useRoute();
const tabs = [
  { label: "首页", to: "/", icon: "home" },
  { label: "搜索", to: "/search", icon: "search" },
  { label: "发布", to: "/publish", icon: "add_circle" },
  { label: "消息", to: "/messages", icon: "chat_bubble" },
  { label: "我的", to: "/profile", icon: "account_circle" }
];

const current = computed(() => route.path);
const isActive = (target) => (target === "/" ? current.value === "/" : current.value.startsWith(target));
</script>

<style scoped>
.mobile-tabs {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 62px;
  z-index: 45;
  border-top: 1px solid var(--border);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  display: none;
  grid-template-columns: repeat(5, 1fr);
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  color: var(--muted);
  gap: 2px;
}

.tab-item .material-symbols-outlined {
  font-size: 20px;
}

.tab-item.active {
  color: var(--primary);
  font-weight: 700;
}

@media (max-width: 720px) {
  .mobile-tabs {
    display: grid;
  }
}
</style>
