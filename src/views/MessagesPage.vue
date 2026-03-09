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
          <RouterLink class="btn btn-muted" to="/order/o-4001">查看订单</RouterLink>
        </header>
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
import { onMounted, ref } from "vue";
import { RouterLink, useRoute } from "vue-router";
import MessageConversationList from "@/components/MessageConversationList.vue";
import { useChatStore } from "@/stores/chat";
import { hasToken } from "@/api/auth";

const route = useRoute();
const chatStore = useChatStore();
const draft = ref("");
const authed = hasToken();

async function changeConversation(id) {
  await chatStore.switchConversation(id);
}

async function send() {
  if (!draft.value) return;
  await chatStore.postMessage(draft.value);
  draft.value = "";
}

onMounted(async () => {
  if (!authed) return;
  await chatStore.loadConversations();
  const fromQuery = route.query.conversation;
  if (typeof fromQuery === "string") {
    await chatStore.switchConversation(fromQuery);
    return;
  }
  if (chatStore.activeId) {
    await chatStore.switchConversation(chatStore.activeId);
  }
});
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

@media (max-width: 980px) {
  .layout {
    grid-template-columns: 1fr;
  }
}
</style>
