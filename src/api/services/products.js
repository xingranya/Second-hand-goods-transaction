import { mapProduct } from "@/api/mappers";
import { productEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapList, unwrapPayload } from "@/api/compat";

function normalizeProducts(payload) {
  const list = unwrapList(payload);
  return list.map(mapProduct);
}

function toCreatePayload(payload = {}) {
  const image = Array.isArray(payload.images) ? payload.images[0] : "";
  const tags = Array.isArray(payload.tags) ? payload.tags : [];
  return {
    name: payload.title || "",
    price: payload.price || 0,
    originalPrice: payload.originPrice || 0,
    description: payload.description || "",
    condition: payload.condition || "",
    campus: payload.campus || "主校区",
    imageUrl: image || "",
    category: tags[0] || "",
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
