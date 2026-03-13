<template>
  <section class="page-container order-page">
    <article v-if="!authed" class="card detail">
      <h2>订单详情</h2>
      <p>当前未登录，无法访问真实订单数据。</p>
      <RouterLink class="btn btn-primary" :to="loginLocation">去登录</RouterLink>
    </article>
    <template v-else>
      <OrderStepBar :status="order?.status" />
      <article v-if="order" class="card detail">
        <h2>订单详情</h2>
        <div class="overview-tags">
          <span class="tag">订单状态：{{ order.status }}</span>
          <span class="tag">支付方式：{{ order.payMethod }}</span>
        </div>
        <p><strong>订单编号：</strong>{{ order.id }}</p>
        <p><strong>创建时间：</strong>{{ order.createTime }}</p>
        <p><strong>支付方式：</strong>{{ order.payMethod }}</p>
        <p><strong>卖家：</strong>{{ order.seller?.name }}</p>
        <p><strong>当前状态：</strong>{{ order.status }}</p>
        <div class="items">
          <h3>商品快照</h3>
          <div v-for="item in order.items" :key="item.id" class="item-row">
            <span>{{ item.title }} x{{ item.count }}</span>
            <strong>¥{{ item.price }}</strong>
          </div>
        </div>
        <div class="total">总计：¥{{ order.totalAmount }}</div>
        <p v-if="errorMessage" class="error-tip">{{ errorMessage }}</p>
        <div class="actions">
          <button class="btn btn-accent" :disabled="!canAction" @click="handleAction">
            {{ store.stepSubmitting && actionMode === "next" ? "处理中..." : actionText }}
          </button>
        </div>
      </article>
    </template>
  </section>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { storeToRefs } from "pinia";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { useOrderStore } from "@/stores/order";
import OrderStepBar from "@/components/OrderStepBar.vue";
import { useUserStore } from "@/stores/user";
import { createLoginLocation } from "@/utils/auth";

const route = useRoute();
const router = useRouter();
const store = useOrderStore();
const userStore = useUserStore();
const { isAuthenticated } = storeToRefs(userStore);
const order = computed(() => store.currentOrder);
const authed = computed(() => isAuthenticated.value);
const loginLocation = computed(() => createLoginLocation(route));
const errorMessage = ref("");

const actionMode = computed(() => {
  const status = order.value?.status;
  if (status === "已下单") return "pay";
  if (status === "待评价") return "review";
  if (["已付款", "已发货", "待收货"].includes(status)) return "next";
  return "none";
});

const actionText = computed(() => {
  const map = {
    pay: "去付款",
    review: "去评价",
    next: "下一步"
  };
  return map[actionMode.value] || "订单已完成";
});

const canAction = computed(() => {
  if (!order.value) return false;
  if (actionMode.value === "none") return false;
  if (actionMode.value === "next") return !store.stepSubmitting;
  return true;
});

onMounted(() => {
  if (!authed.value) return;
  store.loadOrder(route.params.id);
});

watch(
  () => route.params.id,
  (id) => {
    if (!authed.value || !id) return;
    store.loadOrder(id);
  }
);

async function handleAction() {
  if (!canAction.value || !order.value) return;
  errorMessage.value = "";
  if (actionMode.value === "pay") {
    router.push(`/order/${order.value.id}/pay`);
    return;
  }
  if (actionMode.value === "review") {
    router.push(`/order/${order.value.id}/review`);
    return;
  }
  if (actionMode.value === "next") {
    try {
      await store.nextCurrentOrderStep();
    } catch (error) {
      errorMessage.value = error?.response?.data?.message || "推进失败，请稍后重试";
    }
  }
}
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

.overview-tags {
  margin: 10px 0 4px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
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

.actions {
  margin-top: 14px;
}

.error-tip {
  margin: 10px 0 0;
  color: #dc2626;
}
</style>
