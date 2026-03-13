import { defineStore } from "pinia";
import {
  fetchAdminOrders,
  fetchAdminProducts,
  fetchAdminStats,
  fetchAdminUsers,
  removeAdminProduct,
  toggleAdminProductStatus,
  toggleAdminUserEnabled,
  toggleAdminUserVerify
} from "@/api/services/admin";

export const useAdminStore = defineStore("admin", {
  state: () => ({
    stats: null,
    products: [],
    users: [],
    orders: [],
    loading: false,
    errorMessage: ""
  }),
  actions: {
    async loadStats() {
      this.loading = true;
      this.errorMessage = "";
      try {
        this.stats = await fetchAdminStats();
      } catch (error) {
        this.errorMessage = error?.response?.data?.message || "加载后台统计失败";
      } finally {
        this.loading = false;
      }
    },
    async loadProducts(params = {}) {
      this.loading = true;
      this.errorMessage = "";
      try {
        this.products = await fetchAdminProducts(params);
      } catch (error) {
        this.errorMessage = error?.response?.data?.message || "加载商品管理数据失败";
      } finally {
        this.loading = false;
      }
    },
    async updateProductStatus(id, status) {
      const updated = await toggleAdminProductStatus(id, status);
      this.products = this.products.map((item) => (item.id === updated.id ? updated : item));
      return updated;
    },
    async deleteProduct(id) {
      await removeAdminProduct(id);
      this.products = this.products.filter((item) => item.id !== id);
    },
    async loadUsers(params = {}) {
      this.loading = true;
      this.errorMessage = "";
      try {
        this.users = await fetchAdminUsers(params);
      } catch (error) {
        this.errorMessage = error?.response?.data?.message || "加载用户管理数据失败";
      } finally {
        this.loading = false;
      }
    },
    async updateUserEnabled(id, enabled) {
      const updated = await toggleAdminUserEnabled(id, enabled);
      this.users = this.users.map((item) => (item.id === updated.id ? updated : item));
      return updated;
    },
    async updateUserVerify(id, verified) {
      const updated = await toggleAdminUserVerify(id, verified);
      this.users = this.users.map((item) => (item.id === updated.id ? updated : item));
      return updated;
    },
    async loadOrders(params = {}) {
      this.loading = true;
      this.errorMessage = "";
      try {
        this.orders = await fetchAdminOrders(params);
      } catch (error) {
        this.errorMessage = error?.response?.data?.message || "加载订单管理数据失败";
      } finally {
        this.loading = false;
      }
    }
  }
});
