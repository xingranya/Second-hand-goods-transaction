import { defineStore } from "pinia";
import { fetchConversationMessages, fetchConversations, sendMessage } from "@/api/services/messages";

export const useChatStore = defineStore("chat", {
  state: () => ({
    conversations: [],
    activeId: "",
    messages: [],
    loading: false,
    demoMode: false
  }),
  getters: {
    activeConversation(state) {
      return state.conversations.find((item) => item.id === state.activeId) || null;
    }
  },
  actions: {
    async loadConversations() {
      const { data, demoMode } = await fetchConversations();
      this.conversations = data;
      if (!this.activeId && data.length) {
        this.activeId = data[0].id;
      }
      this.demoMode = this.demoMode || demoMode;
    },
    async switchConversation(id) {
      this.activeId = id;
      this.loading = true;
      try {
        const { data, demoMode } = await fetchConversationMessages(id);
        this.messages = data;
        this.demoMode = this.demoMode || demoMode;
      } finally {
        this.loading = false;
      }
    },
    async postMessage(content) {
      if (!this.activeId || !content) return;
      const { data, demoMode } = await sendMessage(this.activeId, content);
      this.messages = [...this.messages, data];
      this.demoMode = this.demoMode || demoMode;
    }
  }
});
