import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Dialog, Button, Portal, CloseButton, Textarea } from "@chakra-ui/react";
import { useStartConversation } from "../../hooks/useConversations";
import { toaster } from "../ui/toaster";

interface ContactDialogProps {
    open: boolean;
    onClose: () => void;
    recipientId: string;
    advertisementId: string;
}

export default function ContactDialog({ open, onClose, recipientId, advertisementId }: ContactDialogProps) {
    const [message, setMessage] = useState("");
    const { mutate: startConversation, isPending } = useStartConversation();
    const navigate = useNavigate();

    const handleSend = () => {
        if (!message.trim()) return;
        startConversation(
            { recipientId, advertisementId, message: message.trim() },
            {
                onSuccess: (conversation) => {
                    setMessage("");
                    onClose();
                    navigate(`/chat/${conversation.id}`);
                },
                onError: () => {
                    toaster.create({
                        title: "Failed to send message",
                        description: "Please try again.",
                        type: "error",
                        duration: 3000,
                    });
                },
            }
        );
    };

    return (
        <Dialog.Root open={open} onOpenChange={({ open: isOpen }) => { if (!isOpen) onClose(); }}>
            <Portal>
                <Dialog.Backdrop />
                <Dialog.Positioner>
                    <Dialog.Content>
                        <Dialog.Header>
                            <Dialog.Title>Contact request</Dialog.Title>
                        </Dialog.Header>
                        <Dialog.Body>
                            <Textarea
                                placeholder="Your message..."
                                value={message}
                                onChange={(e) => setMessage(e.target.value)}
                                rows={4}
                            />
                        </Dialog.Body>
                        <Dialog.Footer>
                            <Dialog.ActionTrigger asChild>
                                <Button variant="outline" onClick={onClose} disabled={isPending}>
                                    Cancel
                                </Button>
                            </Dialog.ActionTrigger>
                            <Button
                                colorPalette="purple"
                                onClick={handleSend}
                                disabled={!message.trim() || isPending}
                                loading={isPending}
                            >
                                Send
                            </Button>
                        </Dialog.Footer>
                        <Dialog.CloseTrigger asChild>
                            <CloseButton size="sm" />
                        </Dialog.CloseTrigger>
                    </Dialog.Content>
                </Dialog.Positioner>
            </Portal>
        </Dialog.Root>
    );
}
