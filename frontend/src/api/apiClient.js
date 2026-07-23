import axios from "axios";

export const API_BASE_URL = "/api";

const apiClient = axios.create({baseURL: API_BASE_URL});
apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if(token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config
});
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.code === "ERR_NETWORK") {
            // The request never reached the server: backend down, wrong port,
            // or blocked by CORS. Axios can't tell these apart, so we surface
            // a message that points at the likely causes.
            error.friendlyMessage =
                "Could not reach the server. Check that the backend is running on " +
                API_BASE_URL +
                " and that it allows CORS requests from this origin (" +
                window.location.origin +
                ").";
        } else if (error.response?.status === 401) {
            // Token missing/expired/rejected — clear stale auth state and
            // bounce to login rather than silently failing every call.
            localStorage.clear();
            if (window.location.pathname !== "/login") {
                window.location.href = "/login";
            }
        }
        console.error("API error:", error?.response?.status, error?.response?.data || error.message);
        return Promise.reject(error);
    }
);

export default apiClient;