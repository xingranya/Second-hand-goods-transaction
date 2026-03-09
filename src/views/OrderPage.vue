<template>
  <section class="page-container order-page">
    <article v-if="!authed" class="card detail">
      <h2>订单详情</h2>
      <p>当前未登录，无法访问真实订单数据。</p>
    </article>
    <template v-else>
      <OrderStepBar :status="order?.status" />
      <article v-if="order" class="card detail">
      <h2>订单详情</h2>
      <p><strong>订单编号：</strong>{{ order.id }}</p>
      <p><strong>创建时间：</strong>{{ order.createTime }}</p>
      <p><strong>支付方式：</strong>{{ order.payMethod }}</p>
      <p><strong>卖家：</strong>{{ order.seller?.name }}</p>
      <div class="items">
        <h3>商品快照</h3>
        <div v-for="item in order.items" :key="item.id" class="item-row">
          <span>{{ item.title }} x{{ item.count }}</span>
          <strong>¥{{ item.price }}</strong>
        </div>
      </div>
      <div class="total">总计：¥{{ order.totalAmount }}</div>
      </article>
    </template>
  </section>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useOrderStore } from "@/stores/order";
import OrderStepBar from "@/components/OrderStepBar.vue";
import { hasToken } from "@/api/auth";

const route = useRoute();
const store = useOrderStore();
const order = computed(() => store.currentOrder);
const authed = hasToken();

onMounted(() => {
  if (!authed) return;
  store.loadOrder(route.params.id);
});
</script>

<style scoped>
.order-page {
  padding-top: 20px;
  display: grid;
  gap: 12px;
}

.detail {
  padding: 16px;
}

h2,
h3 {
  margin: 0 0 8px;
}

p {
  margin: 6px 0;
}

.items {
  margin-top: 10px;
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 12px;
}

.item-row {
  display: flex;
  justify-content: space-between;
  margin-top: 6px;
}

.total {
  margin-top: 14px;
  color: var(--accent);
  font-size: 22px;
  font-weight: 800;
}
</style>
