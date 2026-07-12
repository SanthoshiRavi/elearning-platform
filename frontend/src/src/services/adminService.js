import apiClient from "../api/apiClient";
export const createCourse = async (payload) => {
    const response = await apiClient.post("/api/admin/courses",payload);
    return response.data;
};
export const getAllCourses = async () => {
    const response = await apiClient.get("/api/admin/courses");
    return response.data;
}
export const uploadCourseContent = async (courseId,formData) => {
    const response = await apiClient.post(`/api/admin/courses/${courseId}/contents`,formData,{
        headers: {
            "Content-Type": "multipart/form-data",
            
        },
    });
    return response.data;
};