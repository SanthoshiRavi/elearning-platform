import {
    BrowserRouter,
    Routes,
    Route,
    Navigate,
} from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import CoursesPage from "./pages/CoursesPage";
import MyCoursesPage from "./pages/MyCoursesPage";
import AdminPage from "./pages/AdminPage";
import CourseContentPage from "./pages/CourseContentPage";
import NotFoundPage from "./pages/NotFoundPage";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
    return (
        <BrowserRouter>
            <div className="app-shell">
                <Routes>
                    <Route path="/" element={<Navigate to="/login" replace />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/signup" element={<SignupPage />} />

                    <Route
                        path="/courses"
                        element={
                            <ProtectedRoute>
                                <CoursesPage />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/my-courses"
                        element={
                            <ProtectedRoute>
                                <MyCoursesPage />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/courses/:courseId/contents"
                        element={
                            <ProtectedRoute>
                                <CourseContentPage />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/admin"
                        element={
                            <ProtectedRoute requireAdmin>
                                <AdminPage />
                            </ProtectedRoute>
                        }
                    />

                    <Route path="*" element={<NotFoundPage />} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
