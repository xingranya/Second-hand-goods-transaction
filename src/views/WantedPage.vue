<template>
  <section class="page-container wanted-page">
    <header class="head">
      <div>
        <h2>校园求购广场</h2>
        <p>展示同学们的即时需求，方便快速撮合校园内的二手交易。</p>
      </div>
      <RouterLink class="btn btn-muted" :to="{ path: '/search', query: { keyword: '求购' } }">去搜索商品</RouterLink>
    </header>

    <section class="summary-grid">
      <article class="card summary-card">
        <strong>{{ wantedList.length }}</strong>
        <span>当前求购帖</span>
      </article>
      <article class="card summary-card">
        <strong>{{ authed ? "已登录" : "未登录" }}</strong>
        <span>{{ authed ? "可以直接发布和联系" : "登录后可发布求购并沟通" }}</span>
      </article>
    </section>

    <form v-if="authed" class="card publish-form" @submit.prevent="submitWanted">
      <h3>发布求购</h3>
      <div class="row">
        <input v-model.trim="form.title" class="input" placeholder="求购标题" required />
        <input v-model.number="form.expectedPrice" class="input" type="number" min="0" placeholder="期望价格" />
      </div>
      <div class="row">
        <input v-model.trim="form.deadline" class="input" type="date" />
        <select v-model="form.campus" class="input">
          <option value="主校区">主校区</option>
          <option value="东校区">东校区</option>
          <option value="西校区">西校区</option>
        </select>
      </div>
      <textarea
        v-model.trim="form.description"
        class="input text-area"
        rows="3"
        placeholder="补充说明（可选）"
      />
      <div class="actions">
        <button class="btn btn-primary" type="submit" :disabled="submitting">
          {{ submitting ? "发布中..." : "发布求购" }}
        </button>
      </div>
    </form>

    <section class="list">
      <WantedCard v-for="item in wantedList" :key="item.id" :item="item" @contact="contactUser" />
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { storeToRefs } from "pinia";
import { useRouter, RouterLink } from "vue-router";
import WantedCard from "@/components/WantedCard.vue";
import { createWantedPost, fetchWantedPosts } from "@/api/services/wanted";
import { useUserStore } from "@/stores/user";
import { createLoginLocation } from "@/utils/auth";

const router = useRouter();
const userStore = useUserStore();
const { isAuthenticated } = storeToRefs(userStore);
const wantedList = ref([]);
const submitting = ref(false);
const authed = computed(() => isAuthenticated.value);
const form = reactive({
  title: "",
  expectedPrice: "",
  deadline: "",
  description: "",
  campus: "主校区"
});

async function loadData() {
  wantedList.value = await fetchWantedPosts();
}

async function submitWanted() {
  if (submitting.value) return;
  submitting.value = true;
  try {
    const created = await createWantedPost(form);
    wantedList.value = [created, ...wantedList.value];
    form.title = "";
    form.expectedPrice = "";
    form.deadline = "";
    form.description = "";
    form.campus = "主校区";
  } finally {
    submitting.value = false;
  }
}

function contactUser(item) {
  if (!authed.value) {
    router.push(createLoginLocation({ fullPath: "/wanted" }));
    return;
  }
  if (!item?.publisher?.id) return;
  router.push({
    path: "/messages",
    query: {
      peerUserId: `u-${item.publisher.id}`,
      peerName: item.publisher.name || "",
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

.head p {
  margin: 6px 0 0;
  color: var(--muted);
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 12px;
}

.summary-card {
  padding: 16px;
  display: grid;
  gap: 6px;
}

.summary-card strong {
  font-size: 30px;
  color: var(--primary-strong);
}

.summary-card span {
  color: var(--muted);
}

.publish-form {
  padding: 14px;
  margin-bottom: 12px;
  display: grid;
  gap: 10px;
}

.publish-form h3 {
  margin: 0;
}

.row {
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

.list {
  display: grid;
  gap: 12px;
}

@media (max-width: 720px) {
  .head {
    flex-direction: column;
    align-items: flex-start;
  }

  .summary-grid {
    grid-template-columns: 1fr;
  }

  .row {
    grid-template-columns: 1fr;
  }
}
</style>
