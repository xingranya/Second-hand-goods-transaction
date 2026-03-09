const FALLBACK_STATUS = new Set([404, 501]);

export function shouldFallback(error) {
  if (!error) return false;
  if (!error.response) return true;
  return FALLBACK_STATUS.has(error.response.status);
}

export async function callWithFallback(remoteFn, fallbackFn) {
  try {
    const data = await remoteFn();
    return { data, demoMode: false };
  } catch (error) {
    if (!shouldFallback(error)) {
      throw error;
    }
    const data = await fallbackFn();
    return { data, demoMode: true };
  }
}
