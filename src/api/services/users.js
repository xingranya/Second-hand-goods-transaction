import { userEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapPayload } from "@/api/compat";
import { setToken } from "@/api/auth";

export async function fetchCurrentUser() {
  const { data } = await requestWithCandidates(userEndpoints.me);
  return unwrapPayload(data);
}

export async function submitVerify(payload) {
  const { data } = await requestWithCandidates(
    userEndpoints.verify.map((item) => ({ ...item, data: payload }))
  );
  return unwrapPayload(data);
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
