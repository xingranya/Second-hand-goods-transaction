import { defineStore } from "pinia";
import { createProduct, fetchProductById, fetchProducts } from "@/api/services/products";

export const useMarketStore = defineStore("market", {
  state: () => ({
    products: [],
    currentProduct: null,
    loading: false,
    errorMessage: ""
  }),
  actions: {
    async loadProducts(params = {}) {
      this.loading = true;
      this.errorMessage = "";
      try {
        this.products = await fetchProducts(params);
      } catch (error) {
        this.errorMessage = error?.response?.data?.message || "加载商品列表失败";
        this.products = [];
      } finally {
        this.loading = false;
      }
    },
    async loadProduct(id) {
      this.errorMessage = "";
      try {
        this.currentProduct = await fetchProductById(id);
      } catch (error) {
        this.currentProduct = null;
        this.errorMessage = error?.response?.data?.message || "加载商品详情失败";
        throw error;
      }
    },
    async publishProduct(payload) {
      const data = await createProduct(payload);
      this.products = [data, ...this.products];
      return data;
    }
  }
});
