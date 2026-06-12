import { Button } from "@chakra-ui/react";
import { BsBookmarkFill } from "react-icons/bs";
import { FiBookmark } from "react-icons/fi";
import { useAuth } from "../contexts/useAuth";
import { useSavedAdvertisements, useSaveAdvertisement } from "../hooks/useSavedAdvertisements";

export default function SaveButton({ advertisementId }: { advertisementId: string }) {
    const { isAuthenticated } = useAuth();
    const { data: savedIds } = useSavedAdvertisements(isAuthenticated);
    const { save, unsave } = useSaveAdvertisement();

    if (!isAuthenticated) return null;

    const isSaved = savedIds?.includes(advertisementId) ?? false;

    function handleClick() {
        if (isSaved) {
            unsave.mutate(advertisementId);
        } else {
            save.mutate(advertisementId);
        }
    }

    return (
        <Button onClick={handleClick} variant="outline" colorPalette="teal">
            {isSaved ? <BsBookmarkFill /> : <FiBookmark />} {isSaved ? "Saved" : "Save"}
        </Button>
    );
}
