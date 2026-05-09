import {
    Box,
    Button,
    Heading,
    Input,
    Stack,
    Text,
    Link,
} from "@chakra-ui/react";

import {
  FormControl,
  FormLabel,
} from "@chakra-ui/form-control";

/**
 *
 * @returns JSX element
 */
export default function SignupPage() {
    return (
        <Box
            maxW="md"
            mx="auto"
            mt={10}
            p={6}
            borderWidth={1}
            borderRadius="lg"
            boxShadow="lg"
        >
            <Stack>
                <Heading size="lg" textAlign="center">
                    Create your account
                </Heading>

                <Stack>
                    <FormControl isRequired id="email">
                        <FormLabel>Email</FormLabel>
                        <Input type="email" placeholder="Enter your email" />
                    </FormControl>

                    <FormControl isRequired id="password">
                        <FormLabel>Password</FormLabel>
                        <Input type="password" placeholder="Enter your password" />
                    </FormControl>
                </Stack>

                <Button  
                    size="lg" 
                    colorPalette="purple"
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
        </Box>
    );
}