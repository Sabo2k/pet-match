import { Box, Flex, Button, Image, Spacer, Link, Avatar, AvatarGroup } from "@chakra-ui/react";
import logo from "../assets/logo.png";

import { useNavigate } from "react-router-dom";
import { useAuth } from "@/contexts/useAuth";


function Navbar() {
  const { token, setToken } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    setToken(null);
    navigate("/login");
  };

  return (
    <Box bg="white" boxShadow="md" p={4}>
      <Flex align="center" maxW="1200px" mx="auto">
        {/* Logo */}
        <Link href="/home">
          <Image src={logo} alt="Logo" h="40px" />
        </Link>

        <Spacer />

        {/* Conditionally render Avatar or Login/Sign Up Buttons */}
        {token ? (
          <>
            <AvatarGroup>
                <Avatar.Root>
                    <Avatar.Fallback />
                    <Avatar.Image />
                </Avatar.Root>
            </AvatarGroup>
            
            <Button onClick={handleLogout} colorPalette="red">
              Logout
            </Button>
          </>
        ) : (
          <>
            <Link href="/login" style={{ textDecoration: "none" }}>
              <Button variant="outline" mr={3}>
                Login
              </Button>
            </Link>
            <Link href="/signup" style={{ textDecoration: "none" }}>
              <Button colorScheme="purple">
                Sign Up
              </Button>
            </Link>
          </>
        )}
      </Flex>
    </Box>
  );
}

export default Navbar;