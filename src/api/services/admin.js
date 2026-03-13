import { adminEndpoints } from "@/api/endpoints";
import { mapOrder, mapProduct } from "@/api/mappers";
import { requestWithCandidates, unwrapList, unwrapPayload } from "@/api/compat";

export async function fetchAdminStats() {
  const { data } = await requestWithCandidates(adminEndpoints.stats);
  return unwrapPayload(data);
}

export async function fetchAdminProducts(params = {}) {
  const { data } = await requestWithCandidates(
    adminEndpoints.products.map((item) => ({ ...item, params }))
  );
  return unwrapList(data).map(mapProduct);
}

export async function toggleAdminProductStatus(id, status) {
  const { data } = await requestWithCandidates(adminEndpoints.productStatus(id, status));
  return mapProduct(unwrapPayload(data));
}

export async function removeAdminProduct(id) {
  return requestWithCandidates(adminEndpoints.productDelete(id));
}

export async function fetchAdminUsers(params = {}) {
  const { data } = await requestWithCandidates(
    adminEndpoints.users.map((item) => ({ ...item, params }))
  );
  return unwrapList(data);
}

export async function toggleAdminUserEnabled(id, enabled) {
  const { data } = await requestWithCandidates(adminEndpoints.userEnabled(id, enabled));
  return unwrapPayload(data);
}

export async function toggleAdminUserVerify(id, verified) {
  const { data } = await requestWithCandidates(adminEndpoints.userVerify(id, verified));
  return unwrapPayload(data);
}

export async function fetchAdminOrders(params = {}) {
  const { data } = await requestWithCandidates(
    adminEndpoints.orders.map((item) => ({ ...item, params }))
  );
  return unwrapList(data).map(mapOrder);
}
