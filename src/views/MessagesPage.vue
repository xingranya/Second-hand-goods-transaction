<template>
  <section class="page-container messages-page">
    <article v-if="!authed" class="card auth-card">
      <h2>消息中心</h2>
      <p>当前未登录，无法拉取真实会话数据。请先登录后再访问。</p>
    </article>
    <div v-else class="layout">
      <MessageConversationList
        :conversations="chatStore.conversations"
        :active-id="chatStore.activeId"
        @select="changeConversation"
      />
      <section class="card chat-box">
        <header>
          <h2>{{ chatStore.activeConversation?.peerUser?.name || "请选择会话" }}</h2>
        </header>
        <article class="product-brief">
          <p class="brief-title">关联商品</p>
          <p v-if="loadingProduct" class="brief-loading">商品信息加载中...</p>
          <div v-else-if="productContextEnabled && relatedProduct" class="brief-content">
            <img :src="relatedProduct.images?.[0] || fallbackImage" :alt="relatedProduct.title" />
            <div class="brief-info">
              <strong>{{ relatedProduct.title }}</strong>
              <span>¥{{ relatedProduct.price }}</span>
              <small>卖家：{{ relatedProduct.seller?.name || "未知卖家" }}</small>
              <div class="brief-actions">
                <button class="btn btn-muted" @click="toProductDetail">查看商品</button>
                <button class="btn btn-accent" :disabled="purchasing" @click="buyNow">
                  {{ purchasing ? "下单中..." : "直接去购买" }}
                </button>
              </div>
            </div>
          </div>
          <div v-else class="brief-empty">
            <p class="brief-loading">当前会话未关联商品，请从商品详情页发起聊天以自动带入。</p>
            <div class="brief-actions">
              <button class="btn btn-muted" @click="router.push('/search')">去逛商品</button>
              <button class="btn btn-accent" disabled>直接去购买</button>
            </div>
          </div>
        </article>
        <p v-if="purchaseError" class="error-tip">{{ purchaseError }}</p>
        <p v-if="chatStore.errorMessage" class="error-tip">{{ chatStore.errorMessage }}</p>
        <p v-if="!chatStore.loading && !chatStore.messages.length" class="empty-tip">
          {{ chatStore.activeId ? "暂无历史消息，发送一条消息开始聊天。" : "暂无会话，请从商品或求购详情发起聊天。" }}
        </p>
        <div class="messages">
          <p v-for="msg in chatStore.messages" :key="msg.id" :class="['message', msg.from]">
            {{ msg.content }}
          </p>
        </div>
        <footer>
          <input v-model.trim="draft" class="input" placeholder="输入消息..." @keydown.enter="send" />
          <button class="btn btn-primary" @click="send">发送</button>
        </footer>
      </section>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import MessageConversationList from "@/components/MessageConversationList.vue";
import { useChatStore } from "@/stores/chat";
import { useMarketStore } from "@/stores/market";
import { useOrderStore } from "@/stores/order";
import { hasToken } from "@/api/auth";

const route = useRoute();
const router = useRouter();
const chatStore = useChatStore();
const marketStore = useMarketStore();
const orderStore = useOrderStore();
const draft = ref("");
const authed = hasToken();
const loadingProduct = ref(false);
const purchasing = ref(false);
const purchaseError = ref("");
const fallbackImage = "https://images.unsplash.com/photo-1521017432531-fbd92d768814?w=1200";

const conversationQueryId = computed(() => {
  const raw = route.query.peerUserId || route.query.conversation;
  return typeof raw === "string" ? raw : "";
});

const relatedProductId = computed(() => {
  const raw = route.query.productId;
  if (typeof raw !== "string") return null;
  const id = Number(raw);
  return Number.isFinite(id) && id > 0 ? id : null;
});

const productContextEnabled = computed(() => {
  if (!relatedProductId.value) return false;
  if (!conversationQueryId.value) return false;
  return chatStore.activeId === conversationQueryId.value;
});

const relatedProduct = computed(() => {
  if (!relatedProductId.value) return null;
  const current = marketStore.currentProduct;
  if (!current) return null;
  return Number(current.id) === relatedProductId.value ? current : null;
});

async function changeConversation(id) {
  await router.replace({
    path: "/messages",
    query: { conversation: id }
  });
  await chatStore.switchConversation(id);
}

async function send() {
  if (!draft.value) return;
  const productId = productContextEnabled.value ? relatedProductId.value : null;
  await chatStore.postMessage(draft.value, productId);
  draft.value = "";
}

async function loadRelatedProduct() {
  const id = relatedProductId.value;
  if (!id) return;
  loadingProduct.value = true;
  try {
    await marketStore.loadProduct(id);
  } finally {
    loadingProduct.value = false;
  }
}

function toProductDetail() {
  if (!relatedProductId.value) return;
  router.push(`/product/${relatedProductId.value}`);
}

async function buyNow() {
  if (!relatedProductId.value || purchasing.value) return;
  purchasing.value = true;
  purchaseError.value = "";
  try {
    const order = await orderStore.submitOrder(relatedProductId.value);
    router.push(`/order/${order.id}`);
  } catch (error) {
    purchaseError.value = error?.response?.data?.message || "下单失败，请稍后重试";
  } finally {
    purchasing.value = false;
  }
}

onMounted(async () => {
  if (!authed) return;
  await loadRelatedProduct();
  await chatStore.loadConversations();
  const peerUserId = route.query.peerUserId || route.query.conversation;
  const peerName = typeof route.query.peerName === "string" ? route.query.peerName : "";
  if (typeof peerUserId === "string") {
    await chatStore.switchConversation(peerUserId, peerName);
    return;
  }
  if (chatStore.activeId) {
    await chatStore.switchConversation(chatStore.activeId);
  }
});

watch(
  () => route.query.productId,
  () => {
    loadRelatedProduct();
  }
);
</script>

<style scoped>
.messages-page {
  padding-top: 20px;
}

.auth-card {
  padding: 18px;
}

.auth-card h2 {
  margin: 0 0 8px;
}

.auth-card p {
  margin: 0;
  color: var(--muted);
}

.layout {
  display: grid;
  grid-template-columns: 340px 1fr;
  gap: 12px;
}

.chat-box {
  min-height: 560px;
  display: flex;
  flex-direction: column;
}

header {
  padding: 14px;
  border-bottom: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

h2 {
  margin: 0;
  font-size: 18px;
}

.product-brief {
  border-bottom: 1px solid var(--border);
  padding: 12px 14px;
  background: var(--surface-soft);
}

.brief-title {
  margin: 0 0 8px;
  font-size: 13px;
  color: var(--muted);
}

.brief-loading {
  margin: 0;
  color: var(--muted);
  font-size: 13px;
}

.brief-content {
  display: grid;
  grid-template-columns: 88px 1fr;
  gap: 10px;
}

.brief-empty {
  display: grid;
  gap: 8px;
}

.brief-content img {
  width: 88px;
  height: 88px;
  border-radius: 12px;
  object-fit: cover;
}

.brief-info {
  display: grid;
  gap: 4px;
}

.brief-info strong {
  line-height: 1.4;
}

.brief-info span {
  color: var(--accent);
  font-size: 20px;
  font-weight: 700;
}

.brief-info small {
  color: var(--muted);
}

.brief-actions {
  margin-top: 4px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.messages {
  flex: 1;
  padding: 14px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.message {
  margin: 0;
  max-width: 68%;
  border-radius: 12px;
  padding: 8px 10px;
  line-height: 1.5;
}

.message.peer {
  background: var(--surface-soft);
}

.message.me {
  margin-left: auto;
  background: #dbeafe;
}

footer {
  border-top: 1px solid var(--border);
  padding: 12px;
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 8px;
}

.error-tip {
  margin: 8px 14px 0;
  color: #dc2626;
  font-size: 13px;
}

.empty-tip {
  margin: 10px 14px 0;
  color: var(--muted);
  font-size: 13px;
}

@media (max-width: 980px) {
  .layout {
    grid-template-columns: 1fr;
  }
}
</style>
