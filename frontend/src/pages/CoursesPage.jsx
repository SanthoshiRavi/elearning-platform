import { useEffect, useState } from "react";
import { getCourses, enrollCourse } from "../services/courseService";
import Navbar from "../components/Navbar";

function CoursesPage() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [enrollingId, setEnrollingId] = useState(null);

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
        try {
            const data = await getCourses();
            setCourses(data);
        } catch (error) {
            alert("Failed to load courses");
        } finally {
            setLoading(false);
        }
    };

    const handleEnroll = async (courseId) => {
        setEnrollingId(courseId);
        try {
            const response = await enrollCourse(courseId);
            alert(response.message);
        } catch (error) {
            alert(error?.response?.data?.message || "Enrollment failed");
        } finally {
            setEnrollingId(null);
        }
    };

    return (
        <>
            <Navbar />
            <div className="page">
                <div className="page-header">
                    <h1>Available Courses</h1>
                </div>

                {loading && <div className="loading-state">Loading courses...</div>}

                {!loading && courses.length === 0 && (
                    <div className="empty-state">No courses available yet.</div>
                )}

                {!loading && courses.length > 0 && (
                    <div className="course-grid">
                        {courses.map((course) => (
                            <div key={course.id} className="card">
                                <span className="badge">{course.category}</span>
                                <h3>{course.title}</h3>
                                <div className="card-actions">
                                    <button
                                        className="btn btn-primary"
                                        onClick={() => handleEnroll(course.id)}
                                        disabled={enrollingId === course.id}
                                    >
                                        {enrollingId === course.id ? "Enrolling..." : "Enroll"}
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </>
    );
}

export default CoursesPage;
