import { useEffect, useRef, useState } from "react";
import Navbar from "../components/Navbar";
import {
    createCourse,
    getAllCourses,
    uploadCourseContent,
} from "../services/adminService";
import { getErrorMessage } from "../utils/errorMessage";

const initialCourseForm = { title: "", description: "", category: "" };
const initialUploadForm = {
    courseId: "",
    contentTitle: "",
    contentType: "PDF",
    sequenceOrder: 1,
    file: null,
};

function AdminPage() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [loadError, setLoadError] = useState(null);
    const [creating, setCreating] = useState(false);
    const [uploading, setUploading] = useState(false);

    const [courseForm, setCourseForm] = useState(initialCourseForm);
    const [uploadForm, setUploadForm] = useState(initialUploadForm);
    const fileInputRef = useRef(null);

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
        setLoading(true);
        setLoadError(null);
        try {
            const data = await getAllCourses();
            setCourses(data);
        } catch (error) {
            setLoadError(getErrorMessage(error, "Failed to load courses"));
        } finally {
            setLoading(false);
        }
    };

    const handleCreateCourse = async (e) => {
        e.preventDefault();
        setCreating(true);
        try {
            await createCourse(courseForm);
            alert("Course created successfully");
            setCourseForm(initialCourseForm);
            loadCourses();
        } catch (error) {
            alert(getErrorMessage(error, "Failed to create course"));
        } finally {
            setCreating(false);
        }
    };

    const handleUpload = async (e) => {
        e.preventDefault();
        setUploading(true);
        try {
            const formData = new FormData();
            formData.append("contentTitle", uploadForm.contentTitle);
            formData.append("contentType", uploadForm.contentType);
            formData.append("sequenceOrder", uploadForm.sequenceOrder);
            formData.append("file", uploadForm.file);

            await uploadCourseContent(uploadForm.courseId, formData);

            alert("Content uploaded successfully");
            setUploadForm(initialUploadForm);
            // Native file inputs are uncontrolled, so clearing state alone
            // doesn't clear the displayed filename — reset the input directly.
            if (fileInputRef.current) {
                fileInputRef.current.value = "";
            }
        } catch (error) {
            alert(getErrorMessage(error, "Upload failed"));
        } finally {
            setUploading(false);
        }
    };

    return (
        <>
            <Navbar />
            <div className="page">
                <div className="page-header">
                    <h1>Admin Dashboard</h1>
                </div>

                <h2>Create Course</h2>
                <form className="form" onSubmit={handleCreateCourse}>
                    <div className="form-field">
                        <label htmlFor="title">Title</label>
                        <input
                            id="title"
                            placeholder="Course title"
                            value={courseForm.title}
                            onChange={(e) => setCourseForm({ ...courseForm, title: e.target.value })}
                            required
                        />
                    </div>
                    <div className="form-field">
                        <label htmlFor="description">Description</label>
                        <input
                            id="description"
                            placeholder="Course description"
                            value={courseForm.description}
                            onChange={(e) => setCourseForm({ ...courseForm, description: e.target.value })}
                            required
                        />
                    </div>
                    <div className="form-field">
                        <label htmlFor="category">Category</label>
                        <input
                            id="category"
                            placeholder="e.g. Backend, DevOps"
                            value={courseForm.category}
                            onChange={(e) => setCourseForm({ ...courseForm, category: e.target.value })}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary" disabled={creating}>
                        {creating ? "Creating..." : "Create Course"}
                    </button>
                </form>

                <hr className="section-divider" />

                <h2>Upload Course Content</h2>
                <form className="form" onSubmit={handleUpload}>
                    <div className="form-field">
                        <label htmlFor="courseId">Course</label>
                        <select
                            id="courseId"
                            value={uploadForm.courseId}
                            onChange={(e) => setUploadForm({ ...uploadForm, courseId: e.target.value })}
                            required
                        >
                            <option value="">
                                {courses.length === 0 ? "No courses available" : "Select Course"}
                            </option>
                            {courses.map((course) => (
                                <option key={course.id} value={course.id}>
                                    {course.title}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-field">
                        <label htmlFor="contentTitle">Content Title</label>
                        <input
                            id="contentTitle"
                            placeholder="e.g. Lecture 1"
                            value={uploadForm.contentTitle}
                            onChange={(e) => setUploadForm({ ...uploadForm, contentTitle: e.target.value })}
                            required
                        />
                    </div>

                    <div className="form-row">
                        <div className="form-field">
                            <label htmlFor="contentType">Content Type</label>
                            <select
                                id="contentType"
                                value={uploadForm.contentType}
                                onChange={(e) => setUploadForm({ ...uploadForm, contentType: e.target.value })}
                            >
                                <option value="PDF">PDF</option>
                                <option value="VIDEO">VIDEO</option>
                            </select>
                        </div>

                        <div className="form-field">
                            <label htmlFor="sequenceOrder">Sequence Order</label>
                            <input
                                id="sequenceOrder"
                                type="number"
                                min="1"
                                value={uploadForm.sequenceOrder}
                                onChange={(e) =>
                                    setUploadForm({ ...uploadForm, sequenceOrder: Number(e.target.value) })
                                }
                            />
                        </div>
                    </div>

                    <div className="form-field">
                        <label htmlFor="file">File</label>
                        <input
                            id="file"
                            type="file"
                            ref={fileInputRef}
                            onChange={(e) => setUploadForm({ ...uploadForm, file: e.target.files[0] })}
                            required
                        />
                    </div>

                    <button type="submit" className="btn btn-primary" disabled={uploading}>
                        {uploading ? "Uploading..." : "Upload Content"}
                    </button>
                </form>

                <hr className="section-divider" />

                <h2>Available Courses</h2>
                {loading && <div className="loading-state">Loading courses...</div>}
                {!loading && loadError && (
                    <div className="alert-banner">
                        <p>{loadError}</p>
                        <button className="btn btn-secondary" onClick={loadCourses}>
                            Retry
                        </button>
                    </div>
                )}
                {!loading && !loadError && courses.length === 0 && (
                    <div className="empty-state">No courses yet — create one above.</div>
                )}
                {!loading && courses.length > 0 && (
                    <div className="course-grid">
                        {courses.map((course) => (
                            <div key={course.id} className="card">
                                <span className="badge">{course.category}</span>
                                <h3>{course.title}</h3>
                                <p>{course.description}</p>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </>
    );
}

export default AdminPage;
