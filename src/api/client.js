import axios from "axios";
import { clearToken, getToken } from "@/api/auth";

const apiBaseURL = import.meta.env.VITE_API_BASE_URL || "/api";

const apiClient = axios.create({
  baseURL: apiBaseURL,
  timeout: 8000
});

apiClient.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    if (status === 401) {
      clearToken();
    }
    return Promise.reject(error);
  }
);

export default apiClient;
