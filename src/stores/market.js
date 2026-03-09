import { defineStore } from "pinia";
import { createProduct, fetchProductById, fetchProducts } from "@/api/services/products";

export const useMarketStore = defineStore("market", {
  state: () => ({
    products: [],
    currentProduct: null,
    loading: false,
    demoMode: false
  }),
  actions: {
    async loadProducts(params = {}) {
      this.loading = true;
      try {
        const { data, demoMode } = await fetchProducts(params);
        this.products = data;
        this.demoMode = this.demoMode || demoMode;
      } finally {
        this.loading = false;
      }
    },
    async loadProduct(id) {
      const { data, demoMode } = await fetchProductById(id);
      this.currentProduct = data;
      this.demoMode = this.demoMode || demoMode;
    },
    async publishProduct(payload) {
      const { data, demoMode } = await createProduct(payload);
      this.products = [data, ...this.products];
      this.demoMode = this.demoMode || demoMode;
      return data;
    }
  }
});
