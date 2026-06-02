import { FileUpload, Icon, Box } from "@chakra-ui/react";
import { LuUpload } from "react-icons/lu";
import { useRef, useEffect } from "react";

interface ImageDropZoneProps {
    onFilesSelected?: (files: File[]) => void;
}

export default function ImageDropZone({ onFilesSelected }: ImageDropZoneProps) {
    const inputRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        const input = inputRef.current;
        if (!input) return;

        const handleChange = () => {
            if (input.files) {
                const filesArray = Array.from(input.files);
                onFilesSelected?.(filesArray);
            }
        };

        input.addEventListener("change", handleChange);
        return () => input.removeEventListener("change", handleChange);
    }, [onFilesSelected]);

    return (
        <FileUpload.Root maxW="xl" alignItems="stretch" maxFiles={10}>
            <FileUpload.HiddenInput ref={inputRef} />
            <FileUpload.Dropzone>
                <Icon size="md" color="fg.muted">
                    <LuUpload />
                </Icon>
                <FileUpload.DropzoneContent>
                    <Box>Drag and drop files here</Box>
                    <Box color="fg.muted">.png, .jpg up to 5MB</Box>
                </FileUpload.DropzoneContent>
            </FileUpload.Dropzone>
            <FileUpload.List clearable/>
        </FileUpload.Root>
    );
}