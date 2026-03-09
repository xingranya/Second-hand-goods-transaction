import { defineStore } from "pinia";
import { fetchOrderById } from "@/api/services/orders";

export const useOrderStore = defineStore("order", {
  state: () => ({
    currentOrder: null,
    loading: false,
    demoMode: false
  }),
  actions: {
    async loadOrder(id) {
      this.loading = true;
      try {
        const { data, demoMode } = await fetchOrderById(id);
        this.currentOrder = data;
        this.demoMode = this.demoMode || demoMode;
      } finally {
        this.loading = false;
      }
    }
  }
});
