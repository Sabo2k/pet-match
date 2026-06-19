import { useState } from "react";
import { Button, CloseButton, Dialog, Portal, Text } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import { useDeleteAccount } from "../../hooks/useUserProfile";
import { useAuth } from "../../contexts/useAuth";

export default function DeleteAccountButton() {
    const [open, setOpen] = useState(false);
    const { mutate: deleteAccount, isPending, error } = useDeleteAccount();
    const { setIsAuthenticated } = useAuth();
    const navigate = useNavigate();

    const handleDelete = () => {
        deleteAccount(undefined, {
            onSuccess: () => {
                setIsAuthenticated(false);
                navigate("/login");
            },
        });
    };

    return (
        <Dialog.Root open={open} onOpenChange={(details) => setOpen(details.open)}>
            <Dialog.Trigger asChild>
                <Button colorPalette="red">Delete Account</Button>
            </Dialog.Trigger>
            <Portal>
                <Dialog.Backdrop />
                <Dialog.Positioner>
                    <Dialog.Content>
                        <Dialog.Header>
                            <Dialog.Title>Delete Account</Dialog.Title>
                        </Dialog.Header>
                        <Dialog.Body>
                            <Text>
                                Are you sure you want to delete your account? This action is permanent and cannot be undone.
                            </Text>
                            {error && (
                                <Text color="red.500" fontSize="sm" mt={2}>{error.message}</Text>
                            )}
                        </Dialog.Body>
                        <Dialog.Footer>
                            <Dialog.ActionTrigger asChild>
                                <Button variant="outline">Cancel</Button>
                            </Dialog.ActionTrigger>
                            <Button
                                colorPalette="red"
                                onClick={handleDelete}
                                loading={isPending}
                            >
                                Delete
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
