import { useState, useRef, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import {
    Box, Flex, Heading, Text, Input, Button, Spinner, Stack, VStack,
} from "@chakra-ui/react";
import Navbar from "../navbar/Navbar";
import { useConversation, useSendMessage } from "../../hooks/useConversations";
import { useCurrentUser } from "../../hooks/useUserProfile";

export default function ChatPage() {
    const { id } = useParams<{ id: string }>();
    const { data: conversation, isLoading } = useConversation(id || "");
    const { data: currentUser } = useCurrentUser();
    const { mutate: sendMessage, isPending } = useSendMessage(id || "");
    const [input, setInput] = useState("");
    const bottomRef = useRef<HTMLDivElement>(null);
    const isSendingRef = useRef(false);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [conversation?.messages]);

    const handleSend = () => {
        const content = input.trim();
        if (!content || isSendingRef.current) return;
        isSendingRef.current = true;
        setInput("");
        sendMessage(content, {
            onSettled: () => { isSendingRef.current = false; },
            onError: () => setInput(content),
        });
    };

    const otherParticipant = conversation?.participants.find(
        (p) => p.id !== currentUser?.id
    );

    return (
        <>
            <Navbar />
            <Box maxW="800px" mx="auto" p={4} h="calc(100vh - 72px)" display="flex" flexDir="column">
                {isLoading && (
                    <Flex justify="center" align="center" flex="1">
                        <Spinner size="lg" />
                    </Flex>
                )}

                {conversation && (
                    <>
                        <Box borderBottom="1px solid" borderColor="gray.200" pb={3} mb={4}>
                            <Heading size="md" color="gray.700">
                                {otherParticipant?.username ?? "Chat"}
                            </Heading>
                            {conversation.advertisementTitle && (
                                <Text fontSize="sm" color="gray.500">
                                    Re:{" "}
                                    <Link to={`/advertisement/${conversation.advertisementId}`}>
                                        {conversation.advertisementTitle}
                                    </Link>
                                </Text>
                            )}
                        </Box>

                        <VStack flex="1" overflowY="auto" gap={3} align="stretch" pb={2}>
                            {conversation.messages.map((msg) => {
                                const isMine = msg.senderId === currentUser?.id;
                                return (
                                    <Flex key={msg.id} justify={isMine ? "flex-end" : "flex-start"}>
                                        <Box
                                            bg={isMine ? "purple.500" : "gray.100"}
                                            color={isMine ? "white" : "gray.800"}
                                            px={4}
                                            py={2}
                                            borderRadius="lg"
                                            maxW="70%"
                                        >
                                            {!isMine && (
                                                <Text fontSize="xs" fontWeight="semibold" mb={1} color="gray.500">
                                                    {msg.senderUsername}
                                                </Text>
                                            )}
                                            <Text>{msg.content}</Text>
                                            <Text fontSize="xs" color={isMine ? "purple.100" : "gray.400"} mt={1} textAlign="right">
                                                {new Date(msg.sentAt).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}
                                            </Text>
                                        </Box>
                                    </Flex>
                                );
                            })}
                            <div ref={bottomRef} />
                        </VStack>

                        <Stack direction="row" mt={4} gap={2}>
                            <Input
                                placeholder="Type a message..."
                                value={input}
                                onChange={(e) => setInput(e.target.value)}
                                onKeyDown={(e) => { if (e.key === "Enter" && !e.shiftKey) { e.preventDefault(); handleSend(); } }}
                                disabled={isPending}
                            />
                            <Button
                                colorPalette="purple"
                                onClick={handleSend}
                                disabled={!input.trim() || isPending}
                                loading={isPending}
                            >
                                Send
                            </Button>
                        </Stack>
                    </>
                )}
            </Box>
        </>
    );
}
