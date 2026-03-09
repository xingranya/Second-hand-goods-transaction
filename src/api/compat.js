import apiClient from "@/api/client";

const RETRY_STATUS = new Set([400, 404, 405]);

function extractFromEnvelope(payload) {
  if (payload == null) return payload;
  if (Array.isArray(payload)) return payload;
  if (typeof payload !== "object") return payload;

  const candidates = [
    payload.data,
    payload.result,
    payload.body,
    payload.payload,
    payload.rows,
    payload.records,
    payload.list
  ];

  for (const item of candidates) {
    if (item !== undefined && item !== null) {
      return item;
    }
  }
  return payload;
}

export function unwrapPayload(payload) {
  let current = payload;
  let next = extractFromEnvelope(current);
  while (next !== current) {
    current = next;
    next = extractFromEnvelope(current);
  }
  return current;
}

export function unwrapList(payload) {
  const unwrapped = unwrapPayload(payload);
  if (Array.isArray(unwrapped)) return unwrapped;
  if (Array.isArray(unwrapped?.records)) return unwrapped.records;
  if (Array.isArray(unwrapped?.rows)) return unwrapped.rows;
  if (Array.isArray(unwrapped?.content)) return unwrapped.content;
  if (Array.isArray(unwrapped?.list)) return unwrapped.list;
  return [];
}

function canRetry(error) {
  if (!error?.response) return false;
  return RETRY_STATUS.has(error.response.status);
}

export async function requestWithCandidates(candidates) {
  let lastError = null;

  for (const candidate of candidates) {
    try {
      const response = await apiClient.request(candidate);
      return {
        data: response.data,
        matched: candidate.url
      };
    } catch (error) {
      lastError = error;
      if (!canRetry(error)) {
        throw error;
      }
    }
  }

  throw lastError;
}
