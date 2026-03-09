<template>
  <div class="app-shell">
    <AppHeader />
    <main class="main-content">
      <RouterView />
    </main>
    <MobileTabBar />
  </div>
</template>

<script setup>
import { RouterView } from "vue-router";
import AppHeader from "@/components/AppHeader.vue";
import MobileTabBar from "@/components/MobileTabBar.vue";
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
