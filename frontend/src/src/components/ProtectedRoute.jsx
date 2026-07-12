import { Navigate } from "react-router-dom";
import { isLoggedIn } from "../services/authService";

/**
 * Wraps a route and redirects to /login if the user isn't authenticated.
 * Pass requireAdmin to also require the ADMIN role, redirecting non-admins
 * to /courses instead.
 */
function ProtectedRoute({ children, requireAdmin = false }) {
    if (!isLoggedIn()) {
        return <Navigate to="/login" replace />;
    }

    if (requireAdmin && localStorage.getItem("userRole") !== "ADMIN") {
        return <Navigate to="/courses" replace />;
    }

    return children;
}

export default ProtectedRoute;
