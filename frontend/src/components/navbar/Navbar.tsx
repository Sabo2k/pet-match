import { Box, Flex, Button, Image, Spacer, Link } from "@chakra-ui/react";
import logo from "../assets/logo.png";

function Navbar() {
  return (
    <Box bg="white" boxShadow="md" p={4}>
        <Flex align="center" maxW="1200px" mx="auto">
            {/* Logo */}
        <Link href="/home">
            <Image
                src={logo}
                alt="Logo"
                h="40px"
            />
        </Link>

        <Spacer /> {/* Pushes buttons to the right */}

        {/* Login & Sign Up Buttons */}

        <Link href="/login" style={{ textDecoration: "none" }}>
            <Button variant="outline" mr={3}>
                Login
            </Button>
        </Link>

        <Link href="/signup" style={{ textDecoration: "none" }}>
            <Button colorPalette="purple"
>
                Sign Up
            </Button>
        </Link>
      </Flex>
    </Box>
  );
}

export default Navbar;