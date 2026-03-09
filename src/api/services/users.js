import apiClient from "@/api/client";
import { callWithFallback } from "@/api/fallback";
import { mockCurrentUser } from "@/mock/users";

export async function fetchCurrentUser() {
  return callWithFallback(
    async () => {
      const resp = await apiClient.get("/users/me");
      return resp.data;
    },
    async () => mockCurrentUser
  );
}

export async function submitVerify(payload) {
  return callWithFallback(
    async () => {
      const resp = await apiClient.post("/users/verify", payload);
      return resp.data;
    },
    async () => ({
      success: true,
      message: "演示模式下已标记为提交成功"
    })
  );
}
