<template>
  <section class="page-container pay-page">
    <article v-if="!authed" class="card panel">
      <h2>订单付款</h2>
      <p>当前未登录，请先登录后付款。</p>
      <RouterLink class="btn btn-primary" to="/login">去登录</RouterLink>
    </article>
    <article v-else-if="!order" class="card panel">加载订单中...</article>
    <article v-else class="card panel">
      <h2>付款详情</h2>
      <p><strong>订单号：</strong>{{ order.id }}</p>
      <p><strong>商品：</strong>{{ order.items?.[0]?.title || "未命名商品" }}</p>
      <p><strong>金额：</strong><span class="amount">¥{{ order.totalAmount }}</span></p>
      <p><strong>状态：</strong>{{ order.status }}</p>

      <section class="methods">
        <h3>选择付款方式</h3>
        <label v-for="item in payMethods" :key="item.value" class="method-card">
          <input v-model="selectedMethod" type="radio" :value="item.value" />
          <div>
            <strong>{{ item.label }}</strong>
            <p>{{ item.desc }}</p>
          </div>
        </label>
      </section>

      <section v-if="selectedMethod === 'wechat'" class="detail-box">
        <h4>微信支付</h4>
        <p>请使用微信扫码完成付款，付款后点击“确认已付款”。</p>
      </section>
      <section v-else-if="selectedMethod === 'alipay'" class="detail-box">
        <h4>支付宝</h4>
        <p>请在支付宝完成付款，付款后点击“确认已付款”。</p>
      </section>
      <section v-else class="detail-box">
        <h4>银行卡支付</h4>
        <div class="row">
          <input v-model.trim="bankCardNo" class="input" placeholder="请输入银行卡号" />
          <input v-model.trim="bankHolder" class="input" placeholder="请输入持卡人姓名" />
        </div>
      </section>

      <p v-if="errorMessage" class="error-tip">{{ errorMessage }}</p>
      <div class="actions">
        <RouterLink class="btn btn-muted" :to="`/order/${order.id}`">返回订单</RouterLink>
        <button class="btn btn-accent" :disabled="!canPay" @click="confirmPay">
          {{ store.stepSubmitting ? "处理中..." : payButtonText }}
        </button>
      </div>
    </article>
  </section>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { useOrderStore } from "@/stores/order";
import { hasToken } from "@/api/auth";

const route = useRoute();
const router = useRouter();
const store = useOrderStore();
const authed = hasToken();
const order = computed(() => store.currentOrder);
const selectedMethod = ref("wechat");
const bankCardNo = ref("");
const bankHolder = ref("");
const errorMessage = ref("");

const payMethods = [
  { value: "wechat", label: "微信支付", desc: "推荐，到账快" },
  { value: "alipay", label: "支付宝", desc: "支持余额与花呗" },
  { value: "bank", label: "银行卡", desc: "支持储蓄卡/信用卡" }
];

const canPay = computed(() => {
  if (!order.value) return false;
  if (store.stepSubmitting) return false;
  if (order.value.status !== "已下单") return false;
  if (selectedMethod.value !== "bank") return true;
  return !!bankCardNo.value && !!bankHolder.value;
});

const payButtonText = computed(() => {
  if (!order.value) return "确认已付款";
  if (order.value.status !== "已下单") return "当前状态不可付款";
  return "确认已付款";
});

onMounted(() => {
  if (!authed) return;
  store.loadOrder(route.params.id);
});

watch(
  () => route.params.id,
  (id) => {
    if (!authed || !id) return;
    store.loadOrder(id);
  }
);

async function confirmPay() {
  if (!canPay.value) return;
  errorMessage.value = "";
  try {
    await store.nextCurrentOrderStep();
    router.push(`/order/${order.value.id}`);
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || "付款提交失败，请稍后重试";
  }
}
</script>

<style scoped>
.pay-page {
  padding-top: 20px;
}

.panel {
  padding: 16px;
}

h2,
h3,
h4 {
  margin: 0 0 10px;
}

p {
  margin: 6px 0;
}

.amount {
  color: var(--accent);
  font-size: 24px;
  font-weight: 800;
}

.methods {
  margin-top: 14px;
}

.method-card {
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 10px;
  margin-top: 10px;
  display: grid;
  grid-template-columns: 20px 1fr;
  gap: 10px;
  cursor: pointer;
}

.method-card p {
  color: var(--muted);
  margin: 4px 0 0;
  font-size: 13px;
}

.detail-box {
  margin-top: 14px;
  border: 1px dashed var(--border);
  border-radius: 12px;
  padding: 12px;
  background: var(--surface-soft);
}

.row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.actions {
  margin-top: 14px;
  display: flex;
  gap: 10px;
}

.error-tip {
  margin-top: 10px;
  color: #dc2626;
}

@media (max-width: 760px) {
  .row {
    grid-template-columns: 1fr;
  }
}
</style>
