import { Link, useNavigate } from "react-router-dom";
import { logout } from "../services/authService";

function Navbar() {
    const navigate = useNavigate();
    const isAdmin = localStorage.getItem("userRole") === "ADMIN";
    const username = localStorage.getItem("username");

    const handleLogout = () => {
        logout();
        navigate("/login", { replace: true });
    };

    return (
        <nav className="navbar">
            <div className="navbar-links">
                <Link to="/courses" className="navbar-link">Courses</Link>
                <Link to="/my-courses" className="navbar-link">My Courses</Link>
                {isAdmin && (
                    <Link to="/admin" className="navbar-link">Admin</Link>
                )}
            </div>
            <div className="navbar-links">
                {username && <span className="navbar-user">{username}</span>}
                <button className="btn btn-secondary" onClick={handleLogout}>
                    Logout
                </button>
            </div>
        </nav>
    );
}

export default Navbar;
