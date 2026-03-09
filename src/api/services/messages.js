import { mapConversation } from "@/api/mappers";
import { messageEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapList, unwrapPayload } from "@/api/compat";

function toConversationList(payload) {
  const list = unwrapList(payload);
  return list.map(mapConversation);
}

export async function fetchConversations() {
  const { data } = await requestWithCandidates(messageEndpoints.conversations);
  return toConversationList(data);
}

export async function fetchConversationMessages(peerUserId) {
  const { data } = await requestWithCandidates(messageEndpoints.detail(peerUserId));
  const payload = unwrapPayload(data);
  return Array.isArray(payload) ? payload : Array.isArray(payload?.messages) ? payload.messages : [];
}

export async function sendMessage(peerUserId, content, productId = null) {
  const body = productId ? { content, productId } : { content };
  const { data } = await requestWithCandidates(messageEndpoints.send(peerUserId, body));
  return unwrapPayload(data);
}
