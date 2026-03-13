import { defineStore } from "pinia";
import { clearToken, getToken } from "@/api/auth";
import { fetchCurrentUser, loginByPassword, submitVerify } from "@/api/services/users";

export const useUserStore = defineStore("user", {
  state: () => ({
    token: getToken(),
    profile: null,
    profileLoaded: false
  }),
  getters: {
    isAuthenticated: (state) => Boolean(state.token),
    isAdmin: (state) => state.profile?.role === "ADMIN"
  },
  actions: {
    syncAuthState() {
      this.token = getToken();
      if (!this.token) {
        this.profile = null;
        this.profileLoaded = false;
      }
      return this.token;
    },
    async loadProfile(force = false) {
      if (!this.isAuthenticated) {
        this.profile = null;
        this.profileLoaded = false;
        return null;
      }
      if (!force && this.profileLoaded && this.profile) {
        return this.profile;
      }
      try {
        this.profile = await fetchCurrentUser();
        this.profileLoaded = true;
        return this.profile;
      } catch (error) {
        if (error?.response?.status === 401) {
          this.syncAuthState();
          this.profile = null;
          this.profileLoaded = false;
        }
        throw error;
      }
    },
    async login(payload) {
      const result = await loginByPassword(payload);
      this.syncAuthState();
      await this.loadProfile(true);
      return result;
    },
    logout() {
      clearToken();
      this.syncAuthState();
      this.profile = null;
      this.profileLoaded = false;
    },
    async submitVerifyForm(payload) {
      const result = await submitVerify(payload);
      if (this.isAuthenticated) {
        await this.loadProfile(true);
      }
      return result;
    }
  }
});
