import {
    Box,
    Flex,
    Button,
    Image,
    Spacer,
    Link,
    Avatar,
    AvatarGroup,
    Menu,
    Portal,
} from "@chakra-ui/react";
import logo from "../assets/logo.png";

import { useNavigate } from "react-router-dom";
import { useAuth } from "@/contexts/useAuth";
import { toaster } from "@/components/ui/toaster";

function Navbar() {
    const { isAuthenticated, isAuthLoading, setIsAuthenticated } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            // Call logout endpoint to clear the httpOnly cookie on the backend
            await fetch("http://localhost:8080/api/v1/auth/logout", {
                method: "POST",
                credentials: "include", // Important: send the cookie to the server
            });

            // Update the authentication state
            setIsAuthenticated(false);

            // Redirect to login page
            navigate("/login");

            toaster.create({
                title: "Logged out",
                description: "You have been successfully logged out.",
                type: "success",
                duration: 3000,
            });

        } 
        catch(error) {
            
            console.error("Logout failed:", error);

            toaster.create({
                title: "Logout Error",
                description: "Failed to logout. Please try again.",
                type: "error",
                duration: 3000,
            });
        }
    };

    return (
        <Box bg="white" boxShadow="md" p={4}>
            <Flex align="center" maxW="1200px" mx="auto">
                {/* Logo */}
                <Link href="/">
                    <Image src={logo} alt="Logo" h="40px" />
                </Link>
                <Spacer />
                {/* Conditionally render Avatar or Login/Sign Up Buttons */}
                {!isAuthLoading && (
                    isAuthenticated ? (
                        <>
                            <Menu.Root>
                                <Menu.Trigger>
                                    <AvatarGroup>
                                        <Avatar.Root>
                                            <Avatar.Fallback />
                                            <Avatar.Image />
                                        </Avatar.Root>
                                    </AvatarGroup>
                                </Menu.Trigger>
                                <Portal>
                                    <Menu.Positioner>
                                    <Menu.Content>
                                        <Menu.Item value="profile" onClick={() => navigate("/profile")}>Profile</Menu.Item>
                                        <Menu.Item value="settings">Settings</Menu.Item>
                                        <Menu.Item value="logout" onClick={handleLogout} color="red.500">Logout</Menu.Item>
                                    </Menu.Content>
                                    </Menu.Positioner>
                                </Portal>
                            </Menu.Root>
                        </>
                    ) : (
                        <>
                            <Link href="/login" style={{ textDecoration: "none" }}>
                                <Button variant="outline" mr={3}>
                                    Login
                                </Button>
                            </Link>
                            <Link href="/signup" style={{ textDecoration: "none" }}>
                                <Button colorPalette="purple">
                                    Sign Up
                                </Button>
                            </Link>
                        </>
                    )
                )}
            </Flex>
        </Box>
    );
}

export default Navbar;
