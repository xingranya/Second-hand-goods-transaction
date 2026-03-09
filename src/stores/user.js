import { defineStore } from "pinia";
import { fetchCurrentUser, loginByPassword, submitVerify } from "@/api/services/users";

export const useUserStore = defineStore("user", {
  state: () => ({
    profile: null
  }),
  actions: {
    async loadProfile() {
      this.profile = await fetchCurrentUser();
    },
    async login(payload) {
      return loginByPassword(payload);
    },
    async submitVerifyForm(payload) {
      return submitVerify(payload);
    }
  }
});
