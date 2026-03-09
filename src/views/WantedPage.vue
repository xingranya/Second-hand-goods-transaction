<template>
  <section class="page-container wanted-page">
    <header class="head">
      <h2>校园求购广场</h2>
      <RouterLink class="btn btn-muted" :to="{ path: '/search', query: { keyword: '求购' } }">去搜索商品</RouterLink>
    </header>
    <section class="list">
      <WantedCard v-for="item in wantedList" :key="item.id" :item="item" @contact="contactUser" />
    </section>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter, RouterLink } from "vue-router";
import WantedCard from "@/components/WantedCard.vue";
import { fetchWantedPosts } from "@/api/services/wanted";
import { useMarketStore } from "@/stores/market";

const router = useRouter();
const marketStore = useMarketStore();
const wantedList = ref([]);

async function loadData() {
  const { data, demoMode } = await fetchWantedPosts();
  wantedList.value = data;
  marketStore.demoMode = marketStore.demoMode || demoMode;
}

function contactUser(item) {
  router.push({
    path: "/messages",
    query: {
      conversation: "c-3001",
      wantedId: item.id
    }
  });
}

onMounted(loadData);
</script>

<style scoped>
.wanted-page {
  padding-top: 20px;
}

.head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

h2 {
  margin: 0;
}

.list {
  display: grid;
  gap: 12px;
}
</style>
