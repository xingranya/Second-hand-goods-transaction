import { defineStore } from "pinia";
import { createOrder, fetchOrderById } from "@/api/services/orders";

export const useOrderStore = defineStore("order", {
  state: () => ({
    currentOrder: null,
    loading: false
  }),
  actions: {
    async loadOrder(id) {
      this.loading = true;
      try {
        this.currentOrder = await fetchOrderById(id);
      } finally {
        this.loading = false;
      }
    },
    async submitOrder(productId) {
      return createOrder({ productId });
    }
  }
});
