import { defineStore } from "pinia";
import { advanceOrderStep, createOrder, fetchMyOrders, fetchOrderById } from "@/api/services/orders";

export const useOrderStore = defineStore("order", {
  state: () => ({
    currentOrder: null,
    recentOrders: [],
    loading: false,
    recentLoading: false,
    stepSubmitting: false
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
    async loadRecentOrders() {
      this.recentLoading = true;
      try {
        this.recentOrders = await fetchMyOrders();
      } finally {
        this.recentLoading = false;
      }
    },
    async submitOrder(productId) {
      const created = await createOrder({ productId });
      this.currentOrder = created;
      return created;
    },
    async nextCurrentOrderStep() {
      if (!this.currentOrder?.id || this.stepSubmitting) return null;
      this.stepSubmitting = true;
      try {
        const updated = await advanceOrderStep(this.currentOrder.id);
        this.currentOrder = updated;
        this.recentOrders = this.recentOrders.map((item) =>
          item.id === updated.id ? updated : item
        );
        return updated;
      } finally {
        this.stepSubmitting = false;
      }
    }
  }
});
