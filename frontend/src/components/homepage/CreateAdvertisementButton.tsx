import {
    Box,
    Dialog,
    Button,
    Portal,
    CloseButton,
    Textarea,
    Input,
} from "@chakra-ui/react";
import { CgMathPlus } from "react-icons/cg";
import { toaster } from "@/components/ui/toaster";
import { useAuth } from "../../contexts/useAuth";
import { useState, useRef } from "react";

export default function CreateAdvertisementButton() {
    const { isAuthenticated } = useAuth();
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [age, setAge] = useState("");
    const [price, setPrice] = useState("");
    const [location, setLocation] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const dialogRef = useRef<HTMLDivElement>(null);

    if (!isAuthenticated) {
        return null;
    }

    const handleCreate = async () => {
        if (
            !title.trim() ||
            !description.trim() ||
            !age.trim() ||
            !price.trim() ||
            !location.trim()
        ) {
            toaster.create({
                title: "Missing Fields",
                description: "Please fill in all fields.",
                type: "error",
            });
            return;
        }

        setIsLoading(true);
        try {
            const apiUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";
            const response = await fetch(`${apiUrl}/api/v1/advertisements`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify({
                    title: title.trim(),
                    description: description.trim(),
                    age: parseInt(age),
                    price: parseFloat(price),
                    location: location.trim(),
                    images: [],
                }),
            });

            if (!response.ok) {
                throw new Error("Failed to create advertisement");
            }

            toaster.create({
                title: "Success",
                description: "Advertisement created successfully!",
                type: "success",
            });

            // Reset form
            setTitle("");
            setDescription("");
            setAge("");
            setPrice("");
            setLocation("");

            // Close dialog
            const backdrop = dialogRef.current?.parentElement?.querySelector(
                '[data-scope="dialog"]',
            );

            if (backdrop) {
                const closeButton = backdrop.querySelector(
                    '[data-part="close-trigger"]',
                ) as HTMLButtonElement;
                closeButton?.click();
            }
        } 
        catch (error) {
            toaster.create({
                title: "Error",
                description:
                    error instanceof Error
                        ? error.message
                        : "Failed to create advertisement",
                type: "error",
            });
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <Box display="flex" justifyContent="flex-end" p={4}>
            <Dialog.Root>
                <Dialog.Trigger asChild>
                    <Button colorPalette="purple" size="sm">
                        <CgMathPlus /> Create Advertisement
                    </Button>
                </Dialog.Trigger>
                <Portal>
                    <Dialog.Backdrop />
                    <Dialog.Positioner>
                        <Dialog.Content ref={dialogRef}>
                            <Dialog.Header>
                                <Dialog.Title>New Advertisement</Dialog.Title>
                            </Dialog.Header>
                            <Dialog.Body>
                                <Input
                                    placeholder="Title"
                                    mb={4}
                                    value={title}
                                    onChange={(e) => setTitle(e.target.value)}
                                />
                                <Textarea
                                    placeholder="Description"
                                    mb={4}
                                    value={description}
                                    onChange={(e) =>
                                        setDescription(e.target.value)
                                    }
                                />
                                <Input
                                    placeholder="Age"
                                    type="number"
                                    mb={4}
                                    value={age}
                                    onChange={(e) => setAge(e.target.value)}
                                />
                                <Input
                                    placeholder="Price"
                                    type="number"
                                    step="0.01"
                                    mb={4}
                                    value={price}
                                    onChange={(e) => setPrice(e.target.value)}
                                />
                                <Input
                                    placeholder="Location"
                                    mb={4}
                                    value={location}
                                    onChange={(e) =>
                                        setLocation(e.target.value)
                                    }
                                />
                            </Dialog.Body>
                            <Dialog.Footer>
                                <Dialog.ActionTrigger asChild>
                                    <Button variant="outline">Cancel</Button>
                                </Dialog.ActionTrigger>
                                <Button
                                    colorPalette="purple"
                                    onClick={handleCreate}
                                    loading={isLoading}
                                >
                                    Create
                                </Button>
                            </Dialog.Footer>
                            <Dialog.CloseTrigger asChild>
                                <CloseButton size="sm" />
                            </Dialog.CloseTrigger>
                        </Dialog.Content>
                    </Dialog.Positioner>
                </Portal>
            </Dialog.Root>
        </Box>
    );
}
