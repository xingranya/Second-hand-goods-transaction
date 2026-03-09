import { mapProduct } from "@/api/mappers";
import { productEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapList, unwrapPayload } from "@/api/compat";

function normalizeProducts(payload) {
  const list = unwrapList(payload);
  return list.map(mapProduct);
}

function toCreatePayload(payload = {}) {
  const image = Array.isArray(payload.images) ? payload.images[0] : "";
  return {
    name: payload.title || "",
    price: payload.price || 0,
    description: payload.description || "",
    condition: payload.condition || "",
    imageUrl: image || "",
    category: Array.isArray(payload.tags) ? payload.tags.join(",") : "",
    status: "AVAILABLE"
  };
}

export async function fetchProducts(params = {}) {
  const candidates = productEndpoints.list.map((item) => ({ ...item, params }));
  const { data } = await requestWithCandidates(candidates);
  return normalizeProducts(data);
}

export async function fetchProductById(id) {
  const { data } = await requestWithCandidates(productEndpoints.detail(id));
  const payload = unwrapPayload(data);
  return { ...mapProduct(payload), ...payload };
}

export async function createProduct(payload) {
  const { data } = await requestWithCandidates(
    productEndpoints.create.map((item) => ({ ...item, data: toCreatePayload(payload) }))
  );
  return mapProduct(unwrapPayload(data));
}
