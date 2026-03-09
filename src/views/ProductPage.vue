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
          <span>¥{{ product.originPrice }}</span>
        </div>
        <p class="desc">{{ product.description }}</p>
        <div class="seller">
          <h3>卖家信息</h3>
          <p>{{ product.seller?.name }} · 信用 {{ product.seller?.credit }}</p>
        </div>
        <div class="actions">
          <button class="btn btn-primary" @click="toMessage">发起聊天</button>
          <button class="btn btn-accent" @click="toOrder">立即下单</button>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useMarketStore } from "@/stores/market";

const route = useRoute();
const router = useRouter();
const store = useMarketStore();

const product = computed(() => store.currentProduct);

onMounted(() => {
  store.loadProduct(route.params.id);
});

function toMessage() {
  router.push({
    path: "/messages",
    query: {
      conversation: "c-3001",
      productId: route.params.id
    }
  });
}

function toOrder() {
  router.push("/order/o-4001");
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

@media (max-width: 900px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>
