import { defineStore } from "pinia";
import { fetchCurrentUser, submitVerify } from "@/api/services/users";

export const useUserStore = defineStore("user", {
  state: () => ({
    profile: null,
    demoMode: false
  }),
  actions: {
    async loadProfile() {
      const { data, demoMode } = await fetchCurrentUser();
      this.profile = data;
      this.demoMode = this.demoMode || demoMode;
    },
    async submitVerifyForm(payload) {
      const { data, demoMode } = await submitVerify(payload);
      this.demoMode = this.demoMode || demoMode;
      return data;
    }
  }
});
