import apiClient from "../api/apiClient";
export const getCourses = async () => {
    const response = await apiClient.get("/api/courses");
    return response.data;
};
export const enrollCourse = async (courseId) => {
    const response = await apiClient.post(`/api/courses/${courseId}/enroll`);
    return response.data;
};
export const getMyCourses = async () => {
    const response = await apiClient.get("/api/my-courses");
    return response.data;
};
export const getCourseContents = async (courseId) => {
    const response = await apiClient.get(`/api/courses/${courseId}/contents`);
    return response.data
};