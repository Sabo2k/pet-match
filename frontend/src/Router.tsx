import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import NotFoundPage from "./components/notfoundpage/NotFoundPage";
import type { ReactElement } from "react";
import HomePage from "./components/homepage/Homepage";
import SignupPage from "./components/signuppage/SignupPage";
import LoginPage from "./components/loginpage/LoginPage";
import AdvertisementPage from "./components/advertisementpage/AdvertisementPage";
import UserProfilePage from "./components/userprofilepage/UserProfilePage";
import ProfilePage from "./components/profilepage/ProfilePage";
import SettingsPage from "./components/settingspage/SettingsPage";
import NotificationsPage from "./components/notificationspage/NotificationsPage";
import ChatPage from "./components/chatpage/ChatPage";

const routes: Array<{ path: string; element: ReactElement }> = [
    { path: "/", element: <HomePage/> },
    { path: "/login", element: <LoginPage/> },
    { path: "/signup", element: <SignupPage/> },
    { path: "/advertisement/:id", element: <AdvertisementPage/> },
    { path: "/userprofile", element: <UserProfilePage/> },
    { path: "/profile/:userId", element: <ProfilePage/>},
    { path: "/settings", element: <SettingsPage/>},
    { path: "/notifications", element: <NotificationsPage/>},
    { path: "/chat/:id", element: <ChatPage/>},
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