import { FileUpload, Icon, Box } from "@chakra-ui/react";
import { LuUpload } from "react-icons/lu";

export default function ImageDropZone() {
    return (
        <FileUpload.Root maxW="xl" alignItems="stretch" maxFiles={10}>
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
            <FileUpload.List clearable/>
        </FileUpload.Root>
    );
}