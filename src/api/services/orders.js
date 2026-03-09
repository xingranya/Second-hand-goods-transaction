import apiClient from "@/api/client";
import { callWithFallback } from "@/api/fallback";
import { mapOrder } from "@/api/mappers";
import { mockOrders } from "@/mock/orders";

export async function fetchOrderById(id) {
  return callWithFallback(
    async () => {
      const resp = await apiClient.get(`/orders/${id}`);
      return mapOrder(resp.data);
    },
    async () => mapOrder(mockOrders[id] || mockOrders["o-4001"])
  );
}
