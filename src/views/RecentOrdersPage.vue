<template>
  <section class="page-container recent-orders-page">
    <article v-if="!authed" class="card panel">
      <h2>最近订单</h2>
      <p>当前未登录，请先登录后查看订单。</p>
      <RouterLink class="btn btn-primary" to="/login">去登录</RouterLink>
    </article>
    <template v-else>
      <header class="head">
        <h2>最近订单</h2>
      </header>
      <article v-if="orderStore.recentLoading" class="card panel">加载中...</article>
      <article v-else-if="!orderStore.recentOrders.length" class="card panel">
        暂无订单，去逛逛商品吧。
      </article>
      <section v-else class="list">
        <article v-for="item in orderStore.recentOrders" :key="item.id" class="card order-item">
          <div class="summary">
            <div>
              <span class="tag">{{ resolveRole(item) }}</span>
              <h3>{{ item.items?.[0]?.title || "未命名商品" }}</h3>
              <p>
                <strong>订单号：</strong>{{ item.id }} ·
                <strong>状态：</strong>{{ item.status }}
              </p>
              <p>
                <strong>对方：</strong>{{ resolvePeer(item) }} ·
                <strong>时间：</strong>{{ item.createTime || "-" }}
              </p>
            </div>
            <div class="right">
              <strong class="price">¥{{ item.totalAmount }}</strong>
              <RouterLink class="btn btn-primary" :to="`/order/${item.id}`">查看详情</RouterLink>
            </div>
          </div>
        </article>
      </section>
    </template>
  </section>
</template>

<script setup>
import { onMounted } from "vue";
import { RouterLink } from "vue-router";
import { hasToken } from "@/api/auth";
import { useOrderStore } from "@/stores/order";
import { useUserStore } from "@/stores/user";

const authed = hasToken();
const orderStore = useOrderStore();
const userStore = useUserStore();

function resolveRole(item) {
  const meId = Number(userStore.profile?.id || 0);
  if (!meId) return "订单";
  if (Number(item.buyer?.id) === meId) return "我买到";
  if (Number(item.seller?.id) === meId) return "我卖出";
  return "订单";
}

function resolvePeer(item) {
  const meId = Number(userStore.profile?.id || 0);
  if (!meId) return item.seller?.name || item.buyer?.name || "未知同学";
  if (Number(item.buyer?.id) === meId) return item.seller?.name || "未知卖家";
  return item.buyer?.name || "未知买家";
}

onMounted(async () => {
  if (!authed) return;
  if (!userStore.profile?.id) {
    await userStore.loadProfile();
  }
  await orderStore.loadRecentOrders();
});
</script>

<style scoped>
.recent-orders-page {
  padding-top: 20px;
  display: grid;
  gap: 12px;
}

.head h2 {
  margin: 0;
}

.panel {
  padding: 16px;
}

.panel h2 {
  margin: 0 0 8px;
}

.panel p {
  margin: 0 0 12px;
  color: var(--muted);
}

.list {
  display: grid;
  gap: 12px;
}

.order-item {
  padding: 14px;
}

.summary {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

h3 {
  margin: 10px 0 8px;
}

p {
  margin: 4px 0;
  color: var(--muted);
}

.right {
  display: grid;
  justify-items: end;
  align-content: space-between;
  gap: 10px;
}

.price {
  color: var(--accent);
  font-size: 22px;
}

@media (max-width: 820px) {
  .summary {
    flex-direction: column;
  }

  .right {
    justify-items: start;
  }
}
</style>
