import { systemEndpoints } from "@/api/endpoints";
import { requestWithCandidates, unwrapPayload } from "@/api/compat";

export async function fetchSystemSummary() {
  const { data } = await requestWithCandidates(systemEndpoints.summary);
  return unwrapPayload(data);
}
