<template>
  <RouterView />
</template>

<script setup>
import { onMounted, onUnmounted } from "vue";
import { RouterView } from "vue-router";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();

async function syncAuthState() {
  userStore.syncAuthState();
  if (!userStore.isAuthenticated) {
    return;
  }
  try {
    await userStore.loadProfile();
  } catch {
    // 登录态失效时由接口层和 store 清理状态
  }
}

onMounted(() => {
  syncAuthState();
  window.addEventListener("auth-changed", syncAuthState);
  window.addEventListener("storage", syncAuthState);
});

onUnmounted(() => {
  window.removeEventListener("auth-changed", syncAuthState);
  window.removeEventListener("storage", syncAuthState);
});
</script>
