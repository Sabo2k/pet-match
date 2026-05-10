import { useState } from "react";
import {
    Box,
    Button,
    Heading,
    Input,
    Stack,
    Text,
    Link,
} from "@chakra-ui/react";
import { FormControl, FormLabel } from "@chakra-ui/form-control";
import { useNavigate } from "react-router-dom";
import { Toaster } from "@/components/ui/toaster";
import { toaster } from "@/components/ui/toaster"

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);

        // Basic validation
        if (!email.trim() || !password.trim()) {

            toaster.create({
                title: "Error",
                description: "Email and password are required.",
                type: "error",
                duration: 3000,
            });

            setIsLoading(false);
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/v1/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Login failed");
            }

            // Assuming your backend returns a token or user data
            const data = await response.json();
            console.log("Login successful:", data);

            toaster.create({
                title: "Success!",
                description: "You are now logged in.",
                type: "success",
                duration: 3000,
            });

            // Redirect to home or dashboard
            navigate("/home");
        } 
        catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : "Login failed. Please try again.";
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
        <Box maxW="md" mx="auto" mt={10} p={6} borderWidth={1} borderRadius="lg" boxShadow="lg">
            <form onSubmit={handleSubmit}>
                <Stack gap={4}>
                    <Heading size="lg" textAlign="center">
                        Log into your account
                    </Heading>

                    <Stack gap={4}>
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
                        Log In
                    </Button>

                    <Text textAlign="center">
                        Don't have an account?{" "}
                        <Link color="blue.500" href="/signup">
                            Sign up
                        </Link>
                    </Text>
                </Stack>
            </form>
            <Toaster />
        </Box>
    );
}