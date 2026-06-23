import { Menu, Button, Portal } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import { useDeleteAdvertisement } from "../../hooks/useAdvertisements";

export default function EditMenu({ advertisementId }: { advertisementId: string }) {
    const navigate = useNavigate();
    const { mutate: deleteAdvertisement } = useDeleteAdvertisement();

    function handleDelete() {
        if (!window.confirm("Are you sure you want to delete this advertisement?")) return;
        deleteAdvertisement(advertisementId, {
            onSuccess: () => navigate("/"),
        });
    }

    return (
        <Menu.Root>
            <Menu.Trigger asChild>
                <Button colorPalette="purple">Edit</Button>
            </Menu.Trigger>
            <Portal>
                <Menu.Positioner>
                    <Menu.Content>
                        <Menu.Item value="edit">Edit</Menu.Item>
                        <Menu.Item value="delete" color="red.500" onClick={handleDelete}>
                            Delete
                        </Menu.Item>
                    </Menu.Content>
                </Menu.Positioner>
            </Portal>
        </Menu.Root>
    );
}
