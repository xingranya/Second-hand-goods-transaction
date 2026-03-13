<template>
  <section class="page-container profile-page">
    <article v-if="!authed" class="card panel">
      <h3>我的</h3>
      <p>当前未登录，请先登录后查看个人信息、交易统计和后台入口。</p>
      <RouterLink class="btn btn-primary" :to="loginLocation">去登录</RouterLink>
    </article>
    <template v-else>
      <UserProfileCard :profile="userStore.profile" />
      <section class="summary-grid">
        <article class="card summary-card">
          <h3>最近订单</h3>
          <p>最近一笔订单编号：{{ userStore.profile?.latestOrderId || "暂无" }}</p>
          <RouterLink class="btn btn-muted" to="/orders/recent">查看全部订单</RouterLink>
        </article>
        <article class="card summary-card">
          <h3>账号状态</h3>
          <p>实名认证：{{ userStore.profile?.verified ? "已完成" : "待完成" }}</p>
          <p>未读消息：{{ userStore.profile?.unreadMessageCount ?? 0 }}</p>
          <RouterLink class="btn btn-primary" to="/verify">去完善信息</RouterLink>
        </article>
      </section>
      <section class="card panel">
        <h3>交易管理</h3>
        <div class="actions">
          <RouterLink class="btn btn-primary" to="/publish">发布闲置</RouterLink>
          <RouterLink class="btn btn-muted" to="/wanted">发布求购</RouterLink>
          <RouterLink class="btn btn-muted" to="/messages">消息中心</RouterLink>
          <RouterLink class="btn btn-muted" to="/orders/recent">最近订单</RouterLink>
        </div>
      </section>
      <section v-if="userStore.isAdmin" class="card panel admin-panel">
        <h3>管理中心</h3>
        <p>当前账号具备管理权限，可查看平台统计数据并处理商品、用户与订单信息。</p>
        <RouterLink class="btn btn-accent" to="/admin/dashboard">进入管理后台</RouterLink>
      </section>
    </template>
  </section>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { RouterLink, useRoute } from "vue-router";
import UserProfileCard from "@/components/UserProfileCard.vue";
import { useUserStore } from "@/stores/user";
import { createLoginLocation } from "@/utils/auth";

const route = useRoute();
const userStore = useUserStore();
const { isAuthenticated } = storeToRefs(userStore);
const authed = computed(() => isAuthenticated.value);
const loginLocation = computed(() => createLoginLocation(route));

onMounted(() => {
  if (!authed.value) return;
  userStore.loadProfile();
});
</script>

<style scoped>
.profile-page {
  padding-top: 20px;
  display: grid;
  gap: 14px;
}

.panel {
  padding: 18px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

.summary-card {
  padding: 18px;
}

h3 {
  margin: 0 0 10px;
}

p {
  margin: 0 0 10px;
  color: var(--muted);
  line-height: 1.7;
}

.actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.admin-panel {
  background: linear-gradient(160deg, rgba(255, 248, 240, 0.9), rgba(255, 255, 255, 0.95));
}

@media (max-width: 760px) {
  .summary-grid {
    grid-template-columns: 1fr;
  }
}
</style>
