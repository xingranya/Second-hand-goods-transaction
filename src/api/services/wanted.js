import { wantedEndpoints } from "@/api/endpoints";
import { mapWantedPost } from "@/api/mappers";
import { requestWithCandidates, unwrapList, unwrapPayload } from "@/api/compat";

function normalizeWanted(payload) {
  const list = unwrapList(payload);
  return list.map(mapWantedPost);
}

function toCreatePayload(payload = {}) {
  return {
    title: payload.title || "",
    expectedPrice: payload.expectedPrice || 0,
    deadline: payload.deadline || "",
    description: payload.description || "",
    campus: payload.campus || ""
  };
}

export async function fetchWantedPosts() {
  const { data } = await requestWithCandidates(wantedEndpoints.list);
  return normalizeWanted(data);
}

export async function createWantedPost(payload) {
  const { data } = await requestWithCandidates(
    wantedEndpoints.create.map((item) => ({ ...item, data: toCreatePayload(payload) }))
  );
  return mapWantedPost(unwrapPayload(data));
}
