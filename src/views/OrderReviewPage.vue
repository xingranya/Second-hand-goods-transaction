<template>
  <section class="page-container review-page">
    <article v-if="!authed" class="card panel">
      <h2>订单评价</h2>
      <p>当前未登录，请先登录后评价。</p>
      <RouterLink class="btn btn-primary" to="/login">去登录</RouterLink>
    </article>
    <article v-else-if="!order" class="card panel">加载订单中...</article>
    <article v-else class="card panel">
      <h2>订单评价</h2>
      <p><strong>订单号：</strong>{{ order.id }}</p>
      <p><strong>商品：</strong>{{ order.items?.[0]?.title || "未命名商品" }}</p>
      <p><strong>当前状态：</strong>{{ order.status }}</p>

      <template v-if="submitted">
        <p class="success-tip">评价提交成功，感谢你的反馈。</p>
        <RouterLink class="btn btn-primary" :to="`/order/${order.id}`">返回订单</RouterLink>
      </template>
      <template v-else-if="order.status !== '待评价'">
        <p class="error-tip">当前状态不可评价，请在订单完成后再操作。</p>
        <RouterLink class="btn btn-muted" :to="`/order/${order.id}`">返回订单</RouterLink>
      </template>
      <template v-else>
        <section class="rating-box">
          <h3>评分</h3>
          <div class="stars">
            <button
              v-for="item in [1, 2, 3, 4, 5]"
              :key="item"
              type="button"
              class="star-btn"
              :class="{ active: item <= rating }"
              @click="rating = item"
            >
              ★
            </button>
          </div>
        </section>

        <section class="comment-box">
          <h3>评价内容</h3>
          <textarea
            v-model.trim="comment"
            class="input text-area"
            rows="5"
            placeholder="写下真实体验，帮助其他同学判断（选填）"
          />
        </section>

        <p v-if="errorMessage" class="error-tip">{{ errorMessage }}</p>
        <div class="actions">
          <RouterLink class="btn btn-muted" :to="`/order/${order.id}`">暂不评价</RouterLink>
          <button class="btn btn-accent" :disabled="submitting" @click="submit">
            {{ submitting ? "提交中..." : "提交评价" }}
          </button>
        </div>
      </template>
    </article>
  </section>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { RouterLink, useRoute } from "vue-router";
import { useOrderStore } from "@/stores/order";
import { useUserStore } from "@/stores/user";
import { submitReview } from "@/api/services/reviews";
import { hasToken } from "@/api/auth";

const route = useRoute();
const orderStore = useOrderStore();
const userStore = useUserStore();
const authed = hasToken();
const order = computed(() => orderStore.currentOrder);
const rating = ref(5);
const comment = ref("");
const submitting = ref(false);
const submitted = ref(false);
const errorMessage = ref("");

onMounted(async () => {
  if (!authed) return;
  await orderStore.loadOrder(route.params.id);
  if (!userStore.profile?.id) {
    await userStore.loadProfile();
  }
});

watch(
  () => route.params.id,
  async (id) => {
    if (!authed || !id) return;
    await orderStore.loadOrder(id);
  }
);

async function submit() {
  if (!order.value || submitting.value) return;
  submitting.value = true;
  errorMessage.value = "";
  try {
    const productId = Number(order.value.items?.[0]?.id || 0);
    const reviewerId = Number(userStore.profile?.id || 0);
    if (!productId || !reviewerId) {
      throw new Error("缺少评价参数");
    }

    await submitReview({
      product: { id: productId },
      reviewer: { id: reviewerId },
      rating: rating.value,
      comment: comment.value
    });
    submitted.value = true;
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || error?.message || "评价提交失败，请稍后重试";
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.review-page {
  padding-top: 20px;
}

.panel {
  padding: 16px;
}

h2,
h3 {
  margin: 0 0 10px;
}

p {
  margin: 6px 0;
}

.rating-box,
.comment-box {
  margin-top: 14px;
}

.stars {
  display: flex;
  gap: 6px;
}

.star-btn {
  border: 1px solid var(--border);
  border-radius: 10px;
  background: #fff;
  color: #94a3b8;
  font-size: 24px;
  width: 44px;
  height: 44px;
  cursor: pointer;
}

.star-btn.active {
  color: #f59e0b;
  border-color: #f59e0b;
  background: #fffbeb;
}

.text-area {
  resize: vertical;
}

.actions {
  margin-top: 14px;
  display: flex;
  gap: 10px;
}

.error-tip {
  color: #dc2626;
}

.success-tip {
  color: #16a34a;
  font-weight: 700;
}
</style>
