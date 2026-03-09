<template>
  <section class="page-container profile-page">
    <article v-if="!authed" class="card panel">
      <h3>我的</h3>
      <p>当前未登录，请先登录后查看个人信息与订单。</p>
      <RouterLink class="btn btn-primary" to="/login">去登录</RouterLink>
    </article>
    <template v-else>
      <UserProfileCard :profile="userStore.profile" />
      <section class="card panel">
        <h3>交易管理</h3>
        <div class="actions">
          <RouterLink class="btn btn-primary" to="/publish">我发布的</RouterLink>
          <RouterLink class="btn btn-muted" :to="latestOrderLink">最近订单</RouterLink>
          <RouterLink class="btn btn-muted" to="/messages">消息中心</RouterLink>
        </div>
      </section>
      <section class="card panel">
        <h3>账户服务</h3>
        <div class="actions">
          <RouterLink class="btn btn-primary" to="/verify">实名认证</RouterLink>
        </div>
      </section>
    </template>
  </section>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { RouterLink } from "vue-router";
import UserProfileCard from "@/components/UserProfileCard.vue";
import { useUserStore } from "@/stores/user";
import { hasToken } from "@/api/auth";

const userStore = useUserStore();
const authed = hasToken();
const latestOrderLink = computed(() => {
  const orderId = userStore.profile?.latestOrderId;
  return orderId ? `/order/${orderId}` : "/search";
});

onMounted(() => {
  if (!authed) return;
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

p {
  margin: 0 0 10px;
  color: var(--muted);
}

.actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>
