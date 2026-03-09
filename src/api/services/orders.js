import { mapOrder } from "@/api/mappers";
import { orderEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapPayload } from "@/api/compat";

export async function fetchOrderById(id) {
  const { data } = await requestWithCandidates(orderEndpoints.detail(id));
  return mapOrder(unwrapPayload(data));
}

export async function createOrder(payload) {
  const { data } = await requestWithCandidates(
    orderEndpoints.create.map((item) => ({ ...item, data: payload }))
  );
  return mapOrder(unwrapPayload(data));
}
