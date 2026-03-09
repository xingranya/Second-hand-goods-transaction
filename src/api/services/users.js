import { callWithFallback } from "@/api/fallback";
import { userEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapPayload } from "@/api/compat";
import { setToken } from "@/api/auth";
import { mockCurrentUser } from "@/mock/users";

export async function fetchCurrentUser() {
  return callWithFallback(
    async () => {
      const { data } = await requestWithCandidates(userEndpoints.me);
      return unwrapPayload(data);
    },
    async () => mockCurrentUser
  );
}

export async function submitVerify(payload) {
  return callWithFallback(
    async () => {
      const { data } = await requestWithCandidates(
        userEndpoints.verify.map((item) => ({ ...item, data: payload }))
      );
      return unwrapPayload(data);
    },
    async () => ({
      success: true,
      message: "演示模式下已标记为提交成功"
    })
  );
}

export async function loginByPassword(payload) {
  const { data } = await requestWithCandidates(
    userEndpoints.login.map((item) => ({ ...item, data: payload }))
  );
  const content = unwrapPayload(data);
  const token =
    content?.token || content?.accessToken || content?.jwt || content?.authorization || "";
  if (token) {
    setToken(token);
  }
  return content;
}
