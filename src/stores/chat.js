import { defineStore } from "pinia";
import { fetchConversationMessages, fetchConversations, sendMessage } from "@/api/services/messages";

function createDraftConversation(id, peerName = "") {
  const normalizedId = String(id || "");
  const numeric = normalizedId.startsWith("u-") ? normalizedId.slice(2) : normalizedId;
  return {
    id: normalizedId,
    peerUser: {
      id: numeric || "",
      name: peerName || `用户${numeric || ""}`
    },
    lastMessage: "",
    unreadCount: 0,
    messages: []
  };
}

export const useChatStore = defineStore("chat", {
  state: () => ({
    conversations: [],
    activeId: "",
    messages: [],
    loading: false,
    errorMessage: ""
  }),
  getters: {
    activeConversation(state) {
      return state.conversations.find((item) => item.id === state.activeId) || null;
    }
  },
  actions: {
    async loadConversations() {
      this.errorMessage = "";
      try {
        const data = await fetchConversations();
        this.conversations = data;
        if (!this.activeId && data.length) {
          this.activeId = data[0].id;
        }
      } catch (error) {
        this.errorMessage =
          error?.response?.data?.message || "加载会话失败，请确认已登录且后端接口可用";
      }
    },
    ensureConversation(id, peerName = "") {
      const conversationId = String(id || "");
      if (!conversationId) return;
      const exists = this.conversations.some((item) => item.id === conversationId);
      if (!exists) {
        this.conversations = [createDraftConversation(conversationId, peerName), ...this.conversations];
      }
    },
    async switchConversation(id, peerName = "") {
      this.errorMessage = "";
      const conversationId = String(id || "");
      if (!conversationId) return;
      this.ensureConversation(conversationId, peerName);
      this.activeId = conversationId;
      this.loading = true;
      try {
        this.messages = await fetchConversationMessages(conversationId);
      } catch (error) {
        this.messages = [];
        this.errorMessage =
          error?.response?.data?.message || "加载聊天记录失败，请稍后重试";
      } finally {
        this.loading = false;
      }
    },
    async postMessage(content, productId = null) {
      if (!this.activeId || !content) return;
      this.errorMessage = "";
      try {
        const data = await sendMessage(this.activeId, content, productId);
        this.messages = [...this.messages, data];
        this.conversations = this.conversations.map((item) =>
          item.id === this.activeId ? { ...item, lastMessage: content } : item
        );
      } catch (error) {
        this.errorMessage =
          error?.response?.data?.message || "发送失败，请检查会话对象是否存在";
      }
    }
  }
});
