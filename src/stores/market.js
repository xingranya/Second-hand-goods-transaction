import { defineStore } from "pinia";
import { createProduct, fetchProductById, fetchProducts } from "@/api/services/products";

export const useMarketStore = defineStore("market", {
  state: () => ({
    products: [],
    currentProduct: null,
    loading: false
  }),
  actions: {
    async loadProducts(params = {}) {
      this.loading = true;
      try {
        this.products = await fetchProducts(params);
      } finally {
        this.loading = false;
      }
    },
    async loadProduct(id) {
      this.currentProduct = await fetchProductById(id);
    },
    async publishProduct(payload) {
      const data = await createProduct(payload);
      this.products = [data, ...this.products];
      return data;
    }
  }
});
