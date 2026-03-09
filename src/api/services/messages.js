import apiClient from "@/api/client";
import { callWithFallback } from "@/api/fallback";
import { mapConversation } from "@/api/mappers";
import { mockConversations, mockMessagesByConversation } from "@/mock/messages";

function toConversationList(payload) {
  const list = Array.isArray(payload) ? payload : payload?.records || payload?.list || [];
  return list.map(mapConversation);
}

export async function fetchConversations() {
  return callWithFallback(
    async () => {
      const resp = await apiClient.get("/messages/conversations");
      return toConversationList(resp.data);
    },
    async () => toConversationList(mockConversations)
  );
}

export async function fetchConversationMessages(conversationId) {
  return callWithFallback(
    async () => {
      const resp = await apiClient.get(`/messages/${conversationId}`);
      return Array.isArray(resp.data?.messages) ? resp.data.messages : [];
    },
    async () => mockMessagesByConversation[conversationId] || []
  );
}

export async function sendMessage(conversationId, content) {
  return callWithFallback(
    async () => {
      const resp = await apiClient.post(`/messages/${conversationId}`, { content });
      return resp.data;
    },
    async () => ({
      id: `local-${Date.now()}`,
      from: "me",
      content,
      time: "刚刚"
    })
  );
}
