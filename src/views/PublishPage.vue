<template>
  <section class="page-container publish-page">
    <form class="card form" @submit.prevent="submitForm">
      <h2>发布新物品</h2>
      <label>
        <span>物品标题</span>
        <input v-model.trim="form.title" class="input" required placeholder="例如：iPad Air 4 64G 银色" />
      </label>
      <div class="price-row">
        <label>
          <span>当前价格</span>
          <input v-model.number="form.price" type="number" class="input" min="1" required />
        </label>
        <label>
          <span>原价</span>
          <input v-model.number="form.originPrice" type="number" class="input" min="0" />
        </label>
      </div>
      <label>
        <span>成色</span>
        <select v-model="form.condition" class="input">
          <option value="全新">全新</option>
          <option value="95新">95新</option>
          <option value="9成新">9成新</option>
        </select>
      </label>
      <label>
        <span>校区</span>
        <select v-model="form.campus" class="input">
          <option value="主校区">主校区</option>
          <option value="东校区">东校区</option>
          <option value="西校区">西校区</option>
        </select>
      </label>
      <label>
        <span>详情描述</span>
        <textarea v-model.trim="form.description" class="input text-area" rows="5" />
      </label>
      <div class="actions">
        <button class="btn btn-accent" type="submit">立即发布</button>
      </div>
    </form>
  </section>
</template>

<script setup>
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { useMarketStore } from "@/stores/market";

const router = useRouter();
const store = useMarketStore();

const form = reactive({
  title: "",
  price: 0,
  originPrice: 0,
  condition: "95新",
  campus: "主校区",
  description: ""
});

async function submitForm() {
  await store.publishProduct({
    ...form,
    images: ["https://images.unsplash.com/photo-1484704849700-f032a568e944?w=1200"],
    tags: ["新发布"]
  });
  router.push({
    path: "/",
    query: {
      published: "1"
    }
  });
}
</script>

<style scoped>
.publish-page {
  padding-top: 20px;
}

.form {
  width: min(820px, 100%);
  margin: 0 auto;
  padding: 18px;
  display: grid;
  gap: 12px;
}

h2 {
  margin: 0 0 2px;
}

label {
  display: grid;
  gap: 6px;
  color: var(--muted);
  font-size: 13px;
}

.price-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.text-area {
  resize: vertical;
}

.actions {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 700px) {
  .price-row {
    grid-template-columns: 1fr;
  }
}
</style>
