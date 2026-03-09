import { callWithFallback } from "@/api/fallback";
import { mapConversation } from "@/api/mappers";
import { messageEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapList, unwrapPayload } from "@/api/compat";
import { mockConversations, mockMessagesByConversation } from "@/mock/messages";

function toConversationList(payload) {
  const list = unwrapList(payload);
  return list.map(mapConversation);
}

export async function fetchConversations() {
  return callWithFallback(
    async () => {
      const { data } = await requestWithCandidates(messageEndpoints.conversations);
      return toConversationList(data);
    },
    async () => toConversationList(mockConversations)
  );
}

export async function fetchConversationMessages(conversationId) {
  return callWithFallback(
    async () => {
      const { data } = await requestWithCandidates(messageEndpoints.detail(conversationId));
      const payload = unwrapPayload(data);
      return Array.isArray(payload?.messages)
        ? payload.messages
        : Array.isArray(payload)
          ? payload
          : [];
    },
    async () => mockMessagesByConversation[conversationId] || []
  );
}

export async function sendMessage(conversationId, content) {
  return callWithFallback(
    async () => {
      const { data } = await requestWithCandidates(messageEndpoints.send(conversationId, content));
      return unwrapPayload(data);
    },
    async () => ({
      id: `local-${Date.now()}`,
      from: "me",
      content,
      time: "刚刚"
    })
  );
}
