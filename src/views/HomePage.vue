<template>
  <section class="page-container home-page">
    <section class="hero card">
      <div>
        <span class="tag">校园二手交易平台</span>
        <h1>低成本买到靠谱好物，让闲置流动起来</h1>
        <p>覆盖发布、搜索、咨询、下单、订单管理全流程，支持真实接口优先联调。</p>
        <div class="hero-actions">
          <RouterLink class="btn btn-primary" to="/search">去逛好物</RouterLink>
          <RouterLink class="btn btn-accent" to="/publish">发布闲置</RouterLink>
        </div>
      </div>
      <div class="hero-block">
        <p>热门分类</p>
        <div class="chips">
          <RouterLink class="tag" :to="{ path: '/search', query: { keyword: '考研' } }">考研资料</RouterLink>
          <RouterLink class="tag" :to="{ path: '/search', query: { keyword: '自行车' } }">自行车</RouterLink>
          <RouterLink class="tag" :to="{ path: '/search', query: { keyword: '显示器' } }">显示器</RouterLink>
        </div>
      </div>
    </section>

    <section class="section-head">
      <h2>猜你喜欢</h2>
      <RouterLink to="/search">查看全部</RouterLink>
    </section>

    <EmptyState
      v-if="!store.loading && !store.products.length"
      title="暂无在售商品"
      description="请先发布一条商品，或检查后端接口是否可用。"
    />
    <section v-else class="grid-cards">
      <ProductCard v-for="item in store.products" :key="item.id" :product="item" />
    </section>
  </section>
</template>

<script setup>
import { onMounted } from "vue";
import { RouterLink } from "vue-router";
import ProductCard from "@/components/ProductCard.vue";
import EmptyState from "@/components/EmptyState.vue";
import { useMarketStore } from "@/stores/market";

const store = useMarketStore();

onMounted(() => {
  store.loadProducts({ page: 1, pageSize: 12 });
});
</script>

<style scoped>
.home-page {
  padding-top: 20px;
}

.hero {
  padding: 24px;
  display: grid;
  grid-template-columns: 1.5fr 1fr;
  gap: 18px;
  background:
    linear-gradient(135deg, rgba(37, 99, 235, 0.07), rgba(249, 115, 22, 0.07)),
    var(--surface);
}

h1 {
  margin: 12px 0 10px;
  font-size: 34px;
  line-height: 1.25;
}

p {
  margin: 0;
  color: var(--muted);
}

.hero-actions {
  margin-top: 16px;
  display: flex;
  gap: 12px;
}

.hero-block {
  border-radius: 14px;
  border: 1px dashed var(--border);
  padding: 16px;
  background: rgba(255, 255, 255, 0.7);
}

.hero-block p {
  margin-bottom: 10px;
  font-weight: 700;
}

.chips {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.section-head {
  margin: 20px 0 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

h2 {
  margin: 0;
}

.section-head a {
  color: var(--primary);
  font-size: 14px;
  font-weight: 700;
}

@media (max-width: 900px) {
  .hero {
    grid-template-columns: 1fr;
  }

  h1 {
    font-size: 28px;
  }
}
</style>
