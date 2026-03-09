<template>
  <div class="app-shell">
    <AppHeader />
    <StatusBanner :visible="hasDemoFallback" />
    <main class="main-content">
      <RouterView />
    </main>
    <MobileTabBar />
  </div>
</template>

<script setup>
import { computed } from "vue";
import { RouterView } from "vue-router";
import AppHeader from "@/components/AppHeader.vue";
import MobileTabBar from "@/components/MobileTabBar.vue";
import StatusBanner from "@/components/StatusBanner.vue";
import { useMarketStore } from "@/stores/market";
import { useChatStore } from "@/stores/chat";
import { useOrderStore } from "@/stores/order";
import { useUserStore } from "@/stores/user";

const marketStore = useMarketStore();
const chatStore = useChatStore();
const orderStore = useOrderStore();
const userStore = useUserStore();

const hasDemoFallback = computed(
  () => marketStore.demoMode || chatStore.demoMode || orderStore.demoMode || userStore.demoMode
);
</script>

<style scoped>
.main-content {
  flex: 1;
  padding-bottom: 20px;
}

@media (max-width: 720px) {
  .main-content {
    padding-bottom: 76px;
  }
}
</style>
