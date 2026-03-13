<template>
  <section class="dashboard-page">
    <header class="page-head">
      <div>
        <p class="eyebrow">管理中心</p>
        <h1>平台运行概览</h1>
        <p class="subhead">集中查看当前平台的用户、商品、交易和认证相关数据。</p>
      </div>
    </header>
    <p v-if="adminStore.errorMessage" class="error">{{ adminStore.errorMessage }}</p>
    <section class="stats-grid">
      <article v-for="item in statCards" :key="item.label" class="card stat-card">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
        <small>{{ item.tip }}</small>
      </article>
    </section>
    <section class="card quick-panel">
      <h2>常用管理入口</h2>
      <div class="quick-links">
        <RouterLink class="btn btn-primary" to="/admin/products">管理商品</RouterLink>
        <RouterLink class="btn btn-muted" to="/admin/users">查看用户</RouterLink>
        <RouterLink class="btn btn-muted" to="/admin/orders">查看订单</RouterLink>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { RouterLink } from "vue-router";
import { useAdminStore } from "@/stores/admin";

const adminStore = useAdminStore();

const statCards = computed(() => {
  const stats = adminStore.stats || {};
  return [
    { label: "用户总数", value: stats.userCount ?? "--", tip: "平台内已注册账号数量" },
    { label: "商品总数", value: stats.productCount ?? "--", tip: "当前系统中已发布的商品数量" },
    { label: "求购信息", value: stats.wantedCount ?? "--", tip: "用户主动发布的求购需求数量" },
    { label: "订单总数", value: stats.orderCount ?? "--", tip: "已产生的交易订单数量" },
    { label: "已完成订单", value: stats.completedOrderCount ?? "--", tip: "已完成收货或评价的订单数量" },
    { label: "已认证用户", value: stats.verifiedUserCount ?? "--", tip: "已完善身份信息的用户数量" },
    { label: "在售商品", value: stats.availableProductCount ?? "--", tip: "当前仍可继续交易的商品数量" }
  ];
});

onMounted(async () => {
  await adminStore.loadStats();
});
</script>

<style scoped>
.dashboard-page {
  display: grid;
  gap: 18px;
}

.page-head h1,
.quick-panel h2 {
  margin: 0;
  font-family: var(--font-display);
}

.subhead {
  margin: 10px 0 0;
  color: var(--muted);
  line-height: 1.7;
}

.eyebrow {
  margin: 0 0 8px;
  color: var(--primary);
  letter-spacing: 0.18em;
  text-transform: uppercase;
  font-size: 12px;
  font-weight: 800;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 14px;
}

.stat-card {
  padding: 18px;
  display: grid;
  gap: 10px;
}

.stat-card span {
  color: var(--muted);
  font-size: 13px;
}

.stat-card strong {
  font-size: 34px;
  color: var(--primary-strong);
}

.stat-card small {
  color: var(--muted);
  line-height: 1.6;
}

.quick-panel {
  padding: 20px;
}

.quick-links {
  margin-top: 14px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.error {
  margin: 0;
  color: var(--danger);
}
</style>
