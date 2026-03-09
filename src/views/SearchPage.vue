<template>
  <section class="page-container search-page">
    <div class="layout">
      <FilterPanel v-model="filters" @apply="applyFilters" />
      <div>
        <div class="title-row">
          <h2>搜索结果</h2>
          <span>{{ products.length }} 条</span>
        </div>
        <EmptyState v-if="!products.length" title="没有匹配商品" description="试试更宽泛的关键词或清空筛选条件" />
        <section v-else class="grid-cards">
          <ProductCard v-for="item in products" :key="item.id" :product="item" />
        </section>
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

const filters = ref({
  keyword: "",
  campus: "",
  sort: "latest"
});

const products = computed(() => {
  let list = [...store.products];
  if (filters.value.keyword) {
    const text = filters.value.keyword.toLowerCase();
    list = list.filter((item) => item.title.toLowerCase().includes(text));
  }
  if (filters.value.campus) {
    list = list.filter((item) => item.campus === filters.value.campus);
  }
  if (filters.value.sort === "priceAsc") {
    list.sort((a, b) => a.price - b.price);
  } else if (filters.value.sort === "priceDesc") {
    list.sort((a, b) => b.price - a.price);
  }
  return list;
});

function syncFiltersFromRoute() {
  filters.value.keyword = route.query.keyword || "";
  filters.value.campus = route.query.campus || "";
  filters.value.sort = route.query.sort || "latest";
}

async function applyFilters() {
  await router.replace({
    path: "/search",
    query: {
      keyword: filters.value.keyword || undefined,
      campus: filters.value.campus || undefined,
      sort: filters.value.sort || undefined
    }
  });
  store.loadProducts(filters.value);
}

watch(
  () => route.query,
  () => {
    syncFiltersFromRoute();
    store.loadProducts(filters.value);
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

.title-row {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 12px;
}

.title-row h2 {
  margin: 0;
}

.title-row span {
  color: var(--muted);
  font-size: 13px;
}

@media (max-width: 980px) {
  .layout {
    grid-template-columns: 1fr;
  }
}
</style>
