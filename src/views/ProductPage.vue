<template>
  <section class="page-container product-page" v-if="product">
    <div class="grid">
      <div class="card media">
        <img :src="product.images?.[0]" :alt="product.title" />
      </div>
      <article class="card detail">
        <span class="tag">{{ product.condition }}</span>
        <h1>{{ product.title }}</h1>
        <div class="price">
          <strong>¥{{ product.price }}</strong>
          <span v-if="product.originPrice">¥{{ product.originPrice }}</span>
        </div>
        <p class="desc">{{ product.description }}</p>
        <div class="seller">
          <h3>卖家信息</h3>
          <p>{{ product.seller?.name || "未知卖家" }} · 信用 {{ product.seller?.credit ?? 0 }}</p>
        </div>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
        <div class="actions">
          <button class="btn btn-primary" @click="toMessage">发起聊天</button>
          <button class="btn btn-accent" :disabled="submittingOrder" @click="toOrder">
            {{ submittingOrder ? "下单中..." : "立即下单" }}
          </button>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useMarketStore } from "@/stores/market";
import { useOrderStore } from "@/stores/order";
import { hasToken } from "@/api/auth";

const route = useRoute();
const router = useRouter();
const marketStore = useMarketStore();
const orderStore = useOrderStore();
const submittingOrder = ref(false);
const errorMessage = ref("");

const product = computed(() => marketStore.currentProduct);

onMounted(() => {
  marketStore.loadProduct(route.params.id);
});

function toMessage() {
  if (!hasToken()) {
    router.push("/login");
    return;
  }
  if (!product.value?.sellerId) return;
  router.push({
    path: "/messages",
    query: {
      peerUserId: `u-${product.value.sellerId}`,
      peerName: product.value.seller?.name || "",
      productId: route.params.id
    }
  });
}

async function toOrder() {
  if (!hasToken()) {
    router.push("/login");
    return;
  }
  if (!product.value?.id || submittingOrder.value) return;
  submittingOrder.value = true;
  errorMessage.value = "";
  try {
    const order = await orderStore.submitOrder(Number(product.value.id));
    router.push(`/order/${order.id}`);
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || "下单失败，请稍后重试";
  } finally {
    submittingOrder.value = false;
  }
}
</script>

<style scoped>
.product-page {
  padding-top: 20px;
}

.grid {
  display: grid;
  grid-template-columns: 1.1fr 1fr;
  gap: 16px;
}

.media {
  overflow: hidden;
}

.media img {
  width: 100%;
  aspect-ratio: 4 / 3;
  object-fit: cover;
}

.detail {
  padding: 18px;
}

h1 {
  margin: 12px 0;
  line-height: 1.3;
}

.price {
  display: flex;
  align-items: baseline;
  gap: 10px;
}

.price strong {
  color: var(--accent);
  font-size: 34px;
}

.price span {
  color: var(--muted);
  text-decoration: line-through;
}

.desc {
  margin-top: 12px;
  color: var(--muted);
  line-height: 1.7;
}

.seller {
  margin-top: 12px;
  padding: 12px;
  border: 1px solid var(--border);
  border-radius: 12px;
  background: var(--surface-soft);
}

.seller h3 {
  margin: 0 0 6px;
}

.seller p {
  margin: 0;
}

.actions {
  margin-top: 16px;
  display: flex;
  gap: 10px;
}

.error {
  margin: 10px 0 0;
  color: #dc2626;
}

@media (max-width: 900px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>
