import { reviewEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapPayload } from "@/api/compat";

export async function submitReview(payload) {
  const { data } = await requestWithCandidates(
    reviewEndpoints.create.map((item) => ({ ...item, data: payload }))
  );
  return unwrapPayload(data);
}
