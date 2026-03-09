import { callWithFallback } from "@/api/fallback";
import { mapOrder } from "@/api/mappers";
import { orderEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapPayload } from "@/api/compat";
import { mockOrders } from "@/mock/orders";

export async function fetchOrderById(id) {
  return callWithFallback(
    async () => {
      const { data } = await requestWithCandidates(orderEndpoints.detail(id));
      return mapOrder(unwrapPayload(data));
    },
    async () => mapOrder(mockOrders[id] || mockOrders["o-4001"])
  );
}
