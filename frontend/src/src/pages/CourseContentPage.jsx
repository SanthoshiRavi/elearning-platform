import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import { getCourseContents } from "../services/courseService";
import { API_BASE_URL } from "../api/apiClient";

function CourseContentPage() {
    const { courseId } = useParams();
    const [contents, setContents] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadContents();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [courseId]);

    const loadContents = async () => {
        setLoading(true);
        try {
            const data = await getCourseContents(courseId);
            setContents(data);
        } catch (error) {
            alert(error?.response?.data?.message || "Failed to load contents");
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <Navbar />
            <div className="page">
                <div className="page-header">
                    <h1>Course Contents</h1>
                    <Link to="/my-courses" className="btn btn-secondary">
                        Back
                    </Link>
                </div>

                {loading && <div className="loading-state">Loading contents...</div>}

                {!loading && contents.length === 0 && (
                    <div className="empty-state">No content has been added to this course yet.</div>
                )}

                {!loading && contents.length > 0 && (
                    <div className="form">
                        {contents.map((content) => (
                            <div key={content.id} className="card">
                                <span className="badge">{content.contentType}</span>
                                <h3>{content.contentTitle}</h3>
                                <div className="card-actions">
                                    <a
                                        href={`${API_BASE_URL}${content.contentUrl}`}
                                        target="_blank"
                                        rel="noreferrer"
                                        className="btn btn-primary"
                                    >
                                        Open Content
                                    </a>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </>
    );
}

export default CourseContentPage;
