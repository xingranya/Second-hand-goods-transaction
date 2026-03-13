<template>
  <section class="page-container home-page">
    <section class="hero card">
      <div class="hero-copy">
        <span class="tag">校园二手交易平台</span>
        <h1>让校园闲置物品更快找到下一位需要它的人。</h1>
        <p>支持商品发布、求购信息、即时沟通、订单跟进与实名认证，帮助校园交易更高效、更安心。</p>
        <div class="hero-actions">
          <RouterLink class="btn btn-primary" to="/search">去逛好物</RouterLink>
          <RouterLink class="btn btn-accent" to="/publish">发布闲置</RouterLink>
        </div>
      </div>
      <div class="hero-side">
        <article v-for="item in summaryCards" :key="item.label" class="summary-card">
          <span>{{ item.label }}</span>
          <strong>{{ item.value }}</strong>
        </article>
      </div>
    </section>

    <section class="feature-grid">
      <article class="card feature-card">
        <h2>交易流程清晰</h2>
        <p>从浏览商品、沟通细节到下单交易、订单跟进，校园二手交易流程更加顺畅。</p>
      </article>
      <article class="card feature-card">
        <h2>贴合校园场景</h2>
        <p>围绕校区、学生身份、线下面交和二手议价等真实校园需求进行设计。</p>
      </article>
      <article class="card feature-card">
        <h2>信息管理有序</h2>
        <p>平台提供商品、用户和订单等基础管理能力，帮助维护更稳定的交易环境。</p>
      </article>
    </section>

    <section class="section-head">
      <div>
        <h2>精选推荐</h2>
        <p>优先展示最近发布、信息完整、关注度更高的校园闲置商品。</p>
      </div>
      <RouterLink to="/search">查看全部</RouterLink>
    </section>

    <p v-if="store.errorMessage" class="error-tip">{{ store.errorMessage }}</p>
    <EmptyState
      v-if="!store.loading && !store.products.length"
      title="暂无在售商品"
      description="请先发布一条商品，或检查后端接口是否可用。"
    />
    <section v-else class="grid-cards">
      <ProductCard v-for="item in featuredProducts" :key="item.id" :product="item" />
    </section>

    <section class="process card">
      <h2>平台交易流程</h2>
      <div class="process-grid">
        <div v-for="item in processItems" :key="item.title">
          <strong>{{ item.title }}</strong>
          <p>{{ item.desc }}</p>
        </div>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { RouterLink } from "vue-router";
import ProductCard from "@/components/ProductCard.vue";
import EmptyState from "@/components/EmptyState.vue";
import { useMarketStore } from "@/stores/market";
import { fetchSystemSummary } from "@/api/services/system";

const store = useMarketStore();
const summary = ref(null);

const featuredProducts = computed(() => store.products.slice(0, 8));
const summaryCards = computed(() => {
  const data = summary.value || {};
  return [
    { label: "平台用户", value: data.userCount ?? "--" },
    { label: "在售商品", value: data.productCount ?? "--" },
    { label: "求购帖子", value: data.wantedCount ?? "--" },
    { label: "交易订单", value: data.orderCount ?? "--" }
  ];
});

const processItems = [
  { title: "01 浏览检索", desc: "通过首页推荐、搜索与分类入口，快速找到想要的商品。" },
  { title: "02 沟通确认", desc: "通过消息沟通了解商品详情，提前确认交易方式与时间。" },
  { title: "03 下单跟进", desc: "订单会随着支付、发货、收货等环节逐步更新状态。" },
  { title: "04 完成交易", desc: "交易完成后可进行评价反馈，帮助沉淀更可信的校园交易环境。" }
];

onMounted(async () => {
  store.loadProducts({ sort: "latest", status: "AVAILABLE" });
  try {
    summary.value = await fetchSystemSummary();
  } catch {
    summary.value = null;
  }
});
</script>

<style scoped>
.home-page {
  padding-top: 20px;
  display: grid;
  gap: 18px;
}

.hero {
  padding: 28px;
  display: grid;
  grid-template-columns: 1.3fr 0.9fr;
  gap: 18px;
  background:
    linear-gradient(135deg, rgba(36, 83, 199, 0.08), rgba(234, 109, 39, 0.08)),
    var(--surface);
}

.hero-copy h1,
.feature-card h2,
.section-head h2,
.process h2 {
  margin: 0;
  font-family: var(--font-display);
}

.hero-copy h1 {
  margin-top: 14px;
  font-size: clamp(32px, 4vw, 48px);
  line-height: 1.22;
}

.hero-copy p {
  margin: 12px 0 0;
  color: var(--muted);
  line-height: 1.8;
}

.hero-actions {
  margin-top: 18px;
  display: flex;
  gap: 12px;
}

.hero-side {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

.summary-card {
  padding: 18px 16px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.82);
  border: 1px solid rgba(132, 159, 210, 0.16);
  display: grid;
  gap: 8px;
}

.summary-card span {
  color: var(--muted);
  font-size: 13px;
}

.summary-card strong {
  font-size: 32px;
  color: var(--primary-strong);
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
}

.feature-card {
  padding: 18px;
}

.feature-card p,
.section-head p,
.process p {
  margin: 10px 0 0;
  color: var(--muted);
  line-height: 1.7;
}

.section-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: end;
}

.section-head a {
  color: var(--primary);
  font-size: 14px;
  font-weight: 700;
}

.process {
  padding: 22px;
}

.process-grid {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
}

.process-grid div {
  padding: 16px;
  border-radius: 16px;
  background: var(--surface-soft);
}

.process-grid strong {
  display: block;
}

.error-tip {
  margin: 0;
  color: var(--danger);
}

@media (max-width: 980px) {
  .hero,
  .feature-grid,
  .process-grid {
    grid-template-columns: 1fr;
  }

  .hero-side {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 640px) {
  .hero-side {
    grid-template-columns: 1fr;
  }

  .section-head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
