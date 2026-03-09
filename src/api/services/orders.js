import { mapOrder } from "@/api/mappers";
import { orderEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapList, unwrapPayload } from "@/api/compat";

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

export async function fetchMyOrders() {
  const { data } = await requestWithCandidates(orderEndpoints.my);
  return unwrapList(data).map(mapOrder);
}

export async function advanceOrderStep(id) {
  const { data } = await requestWithCandidates(orderEndpoints.nextStep(id));
  return mapOrder(unwrapPayload(data));
}
