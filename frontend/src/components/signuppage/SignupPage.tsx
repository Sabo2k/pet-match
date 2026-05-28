import { useState } from "react";
import {
    Box,
    Button,
    Heading,
    Input,
    Stack,
    Text,
    Link
} from "@chakra-ui/react";
import { FormControl, FormLabel } from "@chakra-ui/form-control";
import { useNavigate } from "react-router-dom";
import { toaster } from "@/components/ui/toaster";

export default function SignupPage() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    console.log("Submitting:", { username, email, password });
    // Basic validation
    if(!username.trim() || !email.trim() || !password.trim()) {

        toaster.create({
            title: "Error",
            description: "All fields are required.",
            type: "error",
            duration: 3000,
        });

        setIsLoading(false);
        return;
    }

    try {
        console.log("Request body:", JSON.stringify({ username, email, password }));
        // Include credentials: 'include' to allow the browser to handle cookies
        const response = await fetch("http://localhost:8080/api/v1/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: 'include',  // Important: allows cookies to be sent and received
            body: JSON.stringify({ username, email, password }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Signup failed");
        }

        navigate("/login");
        
        toaster.create({
            title: "Account created!",
            description: "You can now log in.",
            type: "success",
            duration: 3000,
        });

    } 
    catch(error: unknown) {
        const errorMessage = error instanceof Error ? error.message : "Failed to create account. Please try again.";

        toaster.create({
            title: "Error",
            description: errorMessage,
            type: "error",
            duration: 3000,
        });
    } 
    finally {
        setIsLoading(false);
    }
};

    return (
        <>

        <Box maxW="md" mx="auto" mt={10} p={6} borderWidth={1} borderRadius="lg" boxShadow="lg">
            <form onSubmit={handleSubmit}>
            <Stack gap={4}>
                <Heading size="lg" textAlign="center">
                    Create your account
                </Heading>

                <Stack gap={4}>
                    <FormControl isRequired id="username">
                        <FormLabel>Username</FormLabel>
                        <Input
                            type="text"
                            placeholder="Enter your username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </FormControl>

                    <FormControl isRequired id="email">
                        <FormLabel>Email</FormLabel>
                        <Input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </FormControl>

                    <FormControl isRequired id="password">
                        <FormLabel>Password</FormLabel>
                        <Input
                            type="password"
                            placeholder="Enter your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </FormControl>
                </Stack>

                <Button
                    size="lg"
                    colorPalette="purple"
                    type="submit"
                    loading={isLoading}
                >
                    Sign Up
                </Button>

                <Text textAlign="center">
                    Already have an account?{" "}
                    <Link color="blue.500" href="/login">
                        Log in
                    </Link>
                </Text>
            </Stack>
            </form>
        </Box>
        </>
    );
}