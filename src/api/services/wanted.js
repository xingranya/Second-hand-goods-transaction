import apiClient from "@/api/client";
import { callWithFallback } from "@/api/fallback";
import { mapWantedPost } from "@/api/mappers";
import { mockWantedPosts } from "@/mock/wanted";

function normalizeWanted(payload) {
  const list = Array.isArray(payload) ? payload : payload?.records || payload?.list || [];
  return list.map(mapWantedPost);
}

export async function fetchWantedPosts() {
  return callWithFallback(
    async () => {
      const resp = await apiClient.get("/wanted");
      return normalizeWanted(resp.data);
    },
    async () => normalizeWanted(mockWantedPosts)
  );
}

export async function createWantedPost(payload) {
  return callWithFallback(
    async () => {
      const resp = await apiClient.post("/wanted", payload);
      return mapWantedPost(resp.data);
    },
    async () => ({
      id: `w-${Date.now()}`,
      ...payload,
      publishTime: "刚刚"
    })
  );
}
