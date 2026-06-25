import { FileUpload, Icon, Box } from "@chakra-ui/react";
import { LuUpload } from "react-icons/lu";

interface ImageDropZoneProps {
    onFilesSelected?: (files: File[]) => void;
}

export default function ImageDropZone({ onFilesSelected }: ImageDropZoneProps) {
    return (
        <FileUpload.Root
            maxW="xl"
            alignItems="stretch"
            maxFiles={10}
            onFileChange={(details) => onFilesSelected?.(details.acceptedFiles)}
        >
            <FileUpload.HiddenInput />
            <FileUpload.Dropzone>
                <Icon size="md" color="fg.muted">
                    <LuUpload />
                </Icon>
                <FileUpload.DropzoneContent>
                    <Box>Drag and drop files here</Box>
                    <Box color="fg.muted">.png, .jpg up to 5MB</Box>
                </FileUpload.DropzoneContent>
            </FileUpload.Dropzone>
            <FileUpload.List clearable />
        </FileUpload.Root>
    );
}
