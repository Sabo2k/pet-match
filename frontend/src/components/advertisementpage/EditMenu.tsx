import { Menu, Button, Portal } from "@chakra-ui/react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDeleteAdvertisement } from "../../hooks/useAdvertisements";
import type { AdvertisementDto } from "../../hooks/useAdvertisements";
import EditAdvertisementDialog from "./EditAdvertisementDialog";

export default function EditMenu({ advertisement }: { advertisement: AdvertisementDto }) {
    const navigate = useNavigate();
    const { mutate: deleteAdvertisement } = useDeleteAdvertisement();
    const [editOpen, setEditOpen] = useState(false);

    function handleDelete() {
        if (!window.confirm("Are you sure you want to delete this advertisement?")) return;
        deleteAdvertisement(advertisement.id, {
            onSuccess: () => navigate("/"),
        });
    }

    return (
        <>
            <Menu.Root>
                <Menu.Trigger asChild>
                    <Button colorPalette="purple">Edit</Button>
                </Menu.Trigger>
                <Portal>
                    <Menu.Positioner>
                        <Menu.Content>
                            <Menu.Item value="edit" onClick={() => setEditOpen(true)}>Edit</Menu.Item>
                            <Menu.Item value="delete" color="red.500" onClick={handleDelete}>
                                Delete
                            </Menu.Item>
                        </Menu.Content>
                    </Menu.Positioner>
                </Portal>
            </Menu.Root>

            <EditAdvertisementDialog
                advertisement={advertisement}
                open={editOpen}
                onClose={() => setEditOpen(false)}
            />
        </>
    );
}
