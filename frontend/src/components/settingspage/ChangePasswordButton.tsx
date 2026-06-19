import { useState } from "react";
import { Button, Dialog, Text, Portal, Stack, CloseButton } from "@chakra-ui/react";
import { PasswordInput } from "@/components/ui/password-input";
import { useChangePassword } from "../../hooks/useUserProfile";

export default function ChangePasswordButton() {
    const [open, setOpen] = useState(false);
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const { mutate: changePassword, isPending, error, reset } = useChangePassword();

    const passwordsMatch = newPassword === confirmPassword;
    const canSubmit = currentPassword.trim() && newPassword.trim() && confirmPassword.trim() && passwordsMatch;

    const handleSave = () => {
        changePassword({ currentPassword, newPassword }, {
            onSuccess: () => {
                setOpen(false);
                setCurrentPassword("");
                setNewPassword("");
                setConfirmPassword("");
            },
        });
    };

    const handleOpenChange = (details: { open: boolean }) => {
        setOpen(details.open);
        if (!details.open) {
            setCurrentPassword("");
            setNewPassword("");
            setConfirmPassword("");
            reset();
        }
    };

    return (
        <Dialog.Root open={open} onOpenChange={handleOpenChange}>
            <Dialog.Trigger asChild>
                <Button>Change Password</Button>
            </Dialog.Trigger>
            <Portal>
                <Dialog.Backdrop />
                <Dialog.Positioner>
                    <Dialog.Content>
                        <Dialog.Header>
                            <Dialog.Title>Change Password</Dialog.Title>
                        </Dialog.Header>
                        <Dialog.Body>
                            <Stack>
                                <Text>Current Password</Text>
                                <PasswordInput
                                    value={currentPassword}
                                    onChange={(e) => setCurrentPassword(e.target.value)}
                                />
                                <Text>New Password</Text>
                                <PasswordInput
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}
                                />
                                <Text>Confirm New Password</Text>
                                <PasswordInput
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                />
                                {confirmPassword && !passwordsMatch && (
                                    <Text color="red.500" fontSize="sm">Passwords do not match</Text>
                                )}
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
                                disabled={!canSubmit}
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
