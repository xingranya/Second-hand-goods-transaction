import apiClient from "@/api/client";
import { callWithFallback } from "@/api/fallback";
import { mapProduct } from "@/api/mappers";
import { mockProducts, mockProductDetail } from "@/mock/products";

function normalizeProducts(payload) {
  const list = Array.isArray(payload) ? payload : payload?.records || payload?.list || [];
  return list.map(mapProduct);
}

export async function fetchProducts(params = {}) {
  return callWithFallback(
    async () => {
      const resp = await apiClient.get("/products", { params });
      return normalizeProducts(resp.data);
    },
    async () => normalizeProducts(mockProducts)
  );
}

export async function fetchProductById(id) {
  return callWithFallback(
    async () => {
      const resp = await apiClient.get(`/products/${id}`);
      return { ...mapProduct(resp.data), ...resp.data };
    },
    async () => ({ ...mockProductDetail, id })
  );
}

export async function createProduct(payload) {
  return callWithFallback(
    async () => {
      const resp = await apiClient.post("/products", payload);
      return mapProduct(resp.data);
    },
    async () => ({
      id: `mock-${Date.now()}`,
      ...payload,
      publishTime: "刚刚"
    })
  );
}
