import apiClient from "../api/apiClient";

export const signup = async (data) => {
    const response = await apiClient.post("/api/auth/signup",data);
    return response.data;
};
export const login = async (data) => {
    const response = await apiClient.post("/api/auth/login",data);
    localStorage.setItem("token", response.data.token);
    localStorage.setItem("userRole",response.data.role);
    localStorage.setItem("username",response.data.name);
    return response.data;
};
export const logout = () => {
    localStorage.clear();
};
export const isLoggedIn = () => {
    return !!localStorage.getItem("token");
}