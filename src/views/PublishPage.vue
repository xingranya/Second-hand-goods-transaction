<template>
  <section class="page-container publish-page">
    <article v-if="!authed" class="card form">
      <h2>发布新物品</h2>
      <p class="tip">当前未检测到登录令牌，请先登录后再发布。</p>
      <RouterLink class="btn btn-primary" :to="loginLocation">去登录</RouterLink>
    </article>
    <form v-else class="card form" @submit.prevent="submitForm">
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
        <span>分类</span>
        <select v-model="form.category" class="input">
          <option value="数码">数码</option>
          <option value="书籍">书籍</option>
          <option value="生活用品">生活用品</option>
          <option value="服饰">服饰</option>
          <option value="家具">家具</option>
          <option value="其他">其他</option>
        </select>
      </label>
      <label>
        <span>封面图链接</span>
        <input v-model.trim="form.imageUrl" class="input" placeholder="可填写图片 URL，留空则使用默认封面" />
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
import { computed, reactive } from "vue";
import { storeToRefs } from "pinia";
import { RouterLink, useRoute, useRouter } from "vue-router";
import { useMarketStore } from "@/stores/market";
import { useUserStore } from "@/stores/user";
import { createLoginLocation } from "@/utils/auth";

const route = useRoute();
const router = useRouter();
const store = useMarketStore();
const userStore = useUserStore();
const { isAuthenticated } = storeToRefs(userStore);
const authed = computed(() => isAuthenticated.value);
const loginLocation = computed(() => createLoginLocation(route));

const form = reactive({
  title: "",
  price: 0,
  originPrice: 0,
  condition: "95新",
  campus: "主校区",
  category: "数码",
  imageUrl: "",
  description: ""
});

async function submitForm() {
  await store.publishProduct({
    ...form,
    images: [
      form.imageUrl || "https://images.unsplash.com/photo-1484704849700-f032a568e944?w=1200"
    ],
    tags: [form.category]
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

.tip {
  margin: 6px 0 0;
  color: var(--muted);
}

@media (max-width: 700px) {
  .price-row {
    grid-template-columns: 1fr;
  }
}
</style>
