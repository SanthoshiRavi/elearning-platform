import apiClient from "../api/apiClient";
export const getCourses = async () => {
    const response = await apiClient.get("/courses");
    return response.data;
};
export const enrollCourse = async (courseId) => {
    const response = await apiClient.post(`/courses/${courseId}/enroll`);
    return response.data;
};
export const getMyCourses = async () => {
    const response = await apiClient.get("/my-courses");
    return response.data;
};
export const getCourseContents = async (courseId) => {
    const response = await apiClient.get(`/courses/${courseId}/contents`);
    return response.data;
};