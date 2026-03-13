<template>
  <section class="page-container search-page">
    <div class="layout">
      <FilterPanel v-model="filters" @apply="applyFilters" />
      <div class="result-panel">
        <div class="title-row">
          <div>
            <h2>搜索结果</h2>
            <p>支持按关键词、校区和价格排序查看校园二手商品。</p>
          </div>
          <span>{{ products.length }} 条</span>
        </div>

        <div class="active-filters">
          <span v-for="item in activeFilterLabels" :key="item" class="tag">{{ item }}</span>
        </div>

        <p v-if="store.errorMessage" class="error-tip">{{ store.errorMessage }}</p>
        <article v-if="store.loading" class="card skeleton-card">
          <p>正在加载商品列表，请稍候...</p>
        </article>
        <EmptyState
          v-else-if="!products.length"
          title="没有匹配商品"
          description="试试更宽泛的关键词、切换校区，或查看全部商品。"
        />
        <template v-else>
          <section class="grid-cards">
            <ProductCard v-for="item in visibleProducts" :key="item.id" :product="item" />
          </section>
          <div v-if="visibleProducts.length < products.length" class="load-more">
            <button class="btn btn-muted" @click="visibleCount += 8">加载更多</button>
          </div>
        </template>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import FilterPanel from "@/components/FilterPanel.vue";
import ProductCard from "@/components/ProductCard.vue";
import EmptyState from "@/components/EmptyState.vue";
import { useMarketStore } from "@/stores/market";

const route = useRoute();
const router = useRouter();
const store = useMarketStore();
const visibleCount = ref(8);

const filters = ref({
  keyword: "",
  campus: "",
  sort: "latest"
});

const products = computed(() => store.products);
const visibleProducts = computed(() => products.value.slice(0, visibleCount.value));
const activeFilterLabels = computed(() => {
  const labels = [];
  if (filters.value.keyword) labels.push(`关键词：${filters.value.keyword}`);
  if (filters.value.campus) labels.push(`校区：${filters.value.campus}`);
  if (filters.value.sort === "priceAsc") labels.push("价格从低到高");
  if (filters.value.sort === "priceDesc") labels.push("价格从高到低");
  if (!labels.length) labels.push("默认推荐");
  return labels;
});

function syncFiltersFromRoute() {
  filters.value.keyword = route.query.keyword || "";
  filters.value.campus = route.query.campus || "";
  filters.value.sort = route.query.sort || "latest";
}

async function applyFilters() {
  visibleCount.value = 8;
  await router.replace({
    path: "/search",
    query: {
      keyword: filters.value.keyword || undefined,
      campus: filters.value.campus || undefined,
      sort: filters.value.sort || undefined
    }
  });
  await store.loadProducts(filters.value);
}

watch(
  () => route.query,
  async () => {
    syncFiltersFromRoute();
    visibleCount.value = 8;
    await store.loadProducts(filters.value);
  }
);

onMounted(async () => {
  syncFiltersFromRoute();
  await store.loadProducts(filters.value);
});
</script>

<style scoped>
.search-page {
  padding-top: 20px;
}

.layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 16px;
}

.result-panel {
  display: grid;
  gap: 14px;
}

.title-row {
  display: flex;
  justify-content: space-between;
  align-items: end;
  gap: 12px;
}

.title-row h2 {
  margin: 0;
  font-family: var(--font-display);
}

.title-row p,
.title-row span {
  margin: 6px 0 0;
  color: var(--muted);
  font-size: 13px;
}

.active-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.skeleton-card {
  padding: 18px;
}

.skeleton-card p,
.error-tip {
  margin: 0;
  color: var(--muted);
}

.error-tip {
  color: var(--danger);
}

.load-more {
  display: flex;
  justify-content: center;
}

@media (max-width: 980px) {
  .layout {
    grid-template-columns: 1fr;
  }

  .title-row {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
