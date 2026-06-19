import { useState } from "react";
import { Dialog, Button, Portal, Stack, CloseButton, Input, Text } from "@chakra-ui/react";
import { useChangeEmail } from "../../hooks/useUserProfile";

export default function ChangeEmailButton() {
    const [newEmail, setNewEmail] = useState("");
    const [open, setOpen] = useState(false);
    const { mutate: changeEmail, isPending, error, reset } = useChangeEmail();

    const handleSave = () => {
        changeEmail(newEmail, {
            onSuccess: () => {
                setOpen(false);
                setNewEmail("");
            },
        });
    };

    const handleOpenChange = (details: { open: boolean }) => {
        setOpen(details.open);
        if (!details.open) {
            setNewEmail("");
            reset();
        }
    };

    return (
        <Dialog.Root open={open} onOpenChange={handleOpenChange}>
            <Dialog.Trigger asChild>
                <Button>Change Email</Button>
            </Dialog.Trigger>
            <Portal>
                <Dialog.Backdrop />
                <Dialog.Positioner>
                    <Dialog.Content>
                        <Dialog.Header>
                            <Dialog.Title>Change Email</Dialog.Title>
                        </Dialog.Header>
                        <Dialog.Body>
                            <Stack>
                                <Input
                                    type="email"
                                    placeholder="New email address"
                                    value={newEmail}
                                    onChange={(e) => setNewEmail(e.target.value)}
                                />
                                {error && (
                                    <Text color="red.500" fontSize="sm">{error.message}</Text>
                                )}
                            </Stack>
                        </Dialog.Body>
                        <Dialog.Footer>
                            <Dialog.ActionTrigger asChild>
                                <Button variant="outline">Cancel</Button>
                            </Dialog.ActionTrigger>
                            <Button
                                colorPalette="purple"
                                onClick={handleSave}
                                loading={isPending}
                                disabled={!newEmail.trim()}
                            >
                                Save
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
