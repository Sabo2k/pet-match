import {
    Dialog,
    Button,
    Portal,
    CloseButton,
    Input,
    Textarea,
    Stack,
    Field,
} from "@chakra-ui/react";
import { useState } from "react";
import { toaster } from "@/components/ui/toaster";
import { useUpdateAdvertisement } from "../../hooks/useAdvertisements";
import type { AdvertisementDto } from "../../hooks/useAdvertisements";
import ImageDropZone from "../homepage/ImageDropZone";

interface EditAdvertisementDialogProps {
    advertisement: AdvertisementDto;
    open: boolean;
    onClose: () => void;
}

export default function EditAdvertisementDialog({
    advertisement,
    open,
    onClose,
}: EditAdvertisementDialogProps) {
    const [title, setTitle] = useState(advertisement.title);
    const [description, setDescription] = useState(advertisement.description);
    const [age, setAge] = useState(String(advertisement.age));
    const [price, setPrice] = useState(String(advertisement.price));
    const [location, setLocation] = useState(advertisement.location);
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);

    const { mutate: updateAdvertisement, isPending } = useUpdateAdvertisement();

    const convertFilesToBase64 = async (files: File[]): Promise<{ url: string; isPrimary: boolean }[]> => {
        return Promise.all(
            files.map((file, index) =>
                new Promise<{ url: string; isPrimary: boolean }>((resolve, reject) => {
                    const reader = new FileReader();
                    reader.onload = () => resolve({ url: reader.result as string, isPrimary: index === 0 });
                    reader.onerror = () => reject(reader.error);
                    reader.readAsDataURL(file);
                })
            )
        );
    };

    async function handleSave() {
        if (!title.trim() || !description.trim() || !age.trim() || !price.trim() || !location.trim()) {
            toaster.create({ title: "Missing Fields", description: "Please fill in all fields.", type: "error" });
            return;
        }

        const images = selectedFiles.length > 0 ? await convertFilesToBase64(selectedFiles) : undefined;

        updateAdvertisement(
            {
                id: advertisement.id,
                title: title.trim(),
                description: description.trim(),
                age: parseInt(age),
                price: parseFloat(price),
                location: location.trim(),
                images,
            },
            {
                onSuccess: () => {
                    toaster.create({ title: "Success", description: "Advertisement updated successfully!", type: "success" });
                    setSelectedFiles([]);
                    onClose();
                },
                onError: () => {
                    toaster.create({ title: "Error", description: "Failed to update advertisement.", type: "error" });
                },
            }
        );
    }

    return (
        <Dialog.Root open={open} onOpenChange={(e) => { if (!e.open) onClose(); }}>
            <Portal>
                <Dialog.Backdrop />
                <Dialog.Positioner>
                    <Dialog.Content>
                        <Dialog.Header>
                            <Dialog.Title>Edit Advertisement</Dialog.Title>
                        </Dialog.Header>
                        <Dialog.Body>
                            <Stack gap={4}>
                                <Field.Root>
                                    <Field.Label>Title</Field.Label>
                                    <Input
                                        value={title}
                                        onChange={(e) => setTitle(e.target.value)}
                                    />
                                </Field.Root>
                                <Field.Root>
                                    <Field.Label>Description</Field.Label>
                                    <Textarea
                                        value={description}
                                        onChange={(e) => setDescription(e.target.value)}
                                    />
                                </Field.Root>
                                <Field.Root>
                                    <Field.Label>Age</Field.Label>
                                    <Input
                                        type="number"
                                        value={age}
                                        onChange={(e) => setAge(e.target.value)}
                                    />
                                </Field.Root>
                                <Field.Root>
                                    <Field.Label>Price (€)</Field.Label>
                                    <Input
                                        type="number"
                                        step="0.01"
                                        value={price}
                                        onChange={(e) => setPrice(e.target.value)}
                                    />
                                </Field.Root>
                                <Field.Root>
                                    <Field.Label>Location</Field.Label>
                                    <Input
                                        value={location}
                                        onChange={(e) => setLocation(e.target.value)}
                                    />
                                </Field.Root>
                                <Field.Root>
                                    <Field.Label>Add Images</Field.Label>
                                    <ImageDropZone onFilesSelected={setSelectedFiles} />
                                </Field.Root>
                            </Stack>
                        </Dialog.Body>
                        <Dialog.Footer>
                            <Dialog.ActionTrigger asChild>
                                <Button variant="outline" onClick={onClose}>Cancel</Button>
                            </Dialog.ActionTrigger>
                            <Button colorPalette="purple" onClick={handleSave} loading={isPending}>
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
