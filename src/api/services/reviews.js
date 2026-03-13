import { reviewEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapPayload } from "@/api/compat";

export async function submitReview(payload) {
  const body = {
    productId: payload.productId,
    rating: payload.rating,
    comment: payload.comment || ""
  };
  const { data } = await requestWithCandidates(
    reviewEndpoints.create.map((item) => ({ ...item, data: body }))
  );
  return unwrapPayload(data);
}
