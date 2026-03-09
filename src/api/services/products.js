import apiClient from "@/api/client";
import { callWithFallback } from "@/api/fallback";
import { mapProduct } from "@/api/mappers";
import { productEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapList, unwrapPayload } from "@/api/compat";
import { mockProducts, mockProductDetail } from "@/mock/products";

function normalizeProducts(payload) {
  const list = unwrapList(payload);
  return list.map(mapProduct);
}

export async function fetchProducts(params = {}) {
  return callWithFallback(
    async () => {
      const candidates = productEndpoints.list.map((item) => ({ ...item, params }));
      const { data } = await requestWithCandidates(candidates);
      return normalizeProducts(data);
    },
    async () => normalizeProducts(mockProducts)
  );
}

export async function fetchProductById(id) {
  return callWithFallback(
    async () => {
      const { data } = await requestWithCandidates(productEndpoints.detail(id));
      const payload = unwrapPayload(data);
      return { ...mapProduct(payload), ...payload };
    },
    async () => ({ ...mockProductDetail, id })
  );
}

export async function createProduct(payload) {
  return callWithFallback(
    async () => {
      const { data } = await requestWithCandidates(
        productEndpoints.create.map((item) => ({ ...item, data: payload }))
      );
      return mapProduct(unwrapPayload(data));
    },
    async () => ({
      id: `mock-${Date.now()}`,
      ...payload,
      publishTime: "刚刚"
    })
  );
}
