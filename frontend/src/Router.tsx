import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import NotFoundPage from "./components/notfoundpage/NotFoundPage";
import type { ReactElement } from "react";
import HomePage from "./components/homepage/HomePage";
import SignupPage from "./components/signuppage/SignupPage";
import LoginPage from "./components/loginpage/LoginPage";

const routes: Array<{ path: string; element: ReactElement }> = [
  { path: "/", element: <HomePage/> },
  { path: "/home", element: <HomePage/> },
  { path: "/login", element: <LoginPage/> },
  { path: "/signup", element: <SignupPage/> },
  { path: "*", element: <NotFoundPage/> }, // Catch-all for 404
];

export default function AppRouter() {
    return (
        <Router>
            <Routes> 
                {routes.map(({ path, element }) => (
                    <Route 
                        key={path} 
                        path={path} 
                        element={element} 
                    />
                ))}
            </Routes>
        </Router>
    );
}