import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import { getMyCourses } from "../services/courseService";

function MyCoursesPage() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
        try {
            const data = await getMyCourses();
            setCourses(data);
        } catch (error) {
            alert(error?.response?.data?.message || "Failed to load courses");
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <Navbar />
            <div className="page">
                <div className="page-header">
                    <h1>My Courses</h1>
                </div>

                {loading && <div className="loading-state">Loading courses...</div>}

                {!loading && courses.length === 0 && (
                    <div className="empty-state">
                        You haven't enrolled in any courses yet.{" "}
                        <Link to="/courses">Browse courses</Link>
                    </div>
                )}

                {!loading && courses.length > 0 && (
                    <div className="course-grid">
                        {courses.map((course) => (
                            <div key={course.enrollmentId} className="card">
                                <h3>{course.title}</h3>
                                <p>{course.description}</p>
                                <div className="card-actions">
                                    <Link
                                        to={`/courses/${course.courseId}/contents`}
                                        className="btn btn-secondary"
                                    >
                                        View Contents
                                    </Link>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </>
    );
}

export default MyCoursesPage;
