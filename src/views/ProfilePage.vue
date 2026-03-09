<template>
  <section class="page-container profile-page">
    <UserProfileCard :profile="userStore.profile" />
    <section class="card panel">
      <h3>交易管理</h3>
      <div class="actions">
        <RouterLink class="btn btn-primary" to="/publish">我发布的</RouterLink>
        <RouterLink class="btn btn-muted" to="/order/o-4001">我买到的</RouterLink>
        <RouterLink class="btn btn-muted" to="/messages">消息中心</RouterLink>
      </div>
    </section>
    <section class="card panel">
      <h3>账户服务</h3>
      <div class="actions">
        <RouterLink class="btn btn-primary" to="/verify">实名认证</RouterLink>
      </div>
    </section>
  </section>
</template>

<script setup>
import { onMounted } from "vue";
import { RouterLink } from "vue-router";
import UserProfileCard from "@/components/UserProfileCard.vue";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();

onMounted(() => {
  userStore.loadProfile();
});
</script>

<style scoped>
.profile-page {
  padding-top: 20px;
  display: grid;
  gap: 12px;
}

.panel {
  padding: 14px;
}

h3 {
  margin: 0 0 10px;
}

.actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>
