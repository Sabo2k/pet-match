import { Box, Heading, Spinner, Flex, Text, VStack, Card } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import Navbar from "../navbar/Navbar";
import { useConversations } from "../../hooks/useConversations";
import { useCurrentUser } from "../../hooks/useUserProfile";

export default function NotificationsPage() {
    const { data: conversations, isLoading } = useConversations();
    const { data: currentUser } = useCurrentUser();
    const navigate = useNavigate();

    return (
        <>
            <Navbar />
            <Box maxW="800px" mx="auto" p={8}>
                <Heading mb={6} color="gray.700">Messages</Heading>

                {isLoading && (
                    <Flex justify="center" mt={8}>
                        <Spinner size="lg" />
                    </Flex>
                )}

                {conversations && conversations.length === 0 && (
                    <Text color="gray.500">No conversations yet.</Text>
                )}

                <VStack gap={3} align="stretch">
                    {conversations?.map((conv) => {
                        const other = conv.participants.find((p) => p.id !== currentUser?.id);
                        return (
                            <Card.Root
                                key={conv.id}
                                cursor="pointer"
                                _hover={{ borderColor: "purple.400" }}
                                onClick={() => navigate(`/chat/${conv.id}`)}
                            >
                                <Card.Body>
                                    <Text fontWeight="semibold">{other?.username ?? "Unknown"}</Text>
                                    {conv.advertisementTitle && (
                                        <Text fontSize="sm" color="gray.500">
                                            Re: {conv.advertisementTitle}
                                        </Text>
                                    )}
                                </Card.Body>
                            </Card.Root>
                        );
                    })}
                </VStack>
            </Box>
        </>
    );
}
