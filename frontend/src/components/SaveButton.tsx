import { Button } from "@chakra-ui/react";
import { useAuth } from "../contexts/useAuth";
import { FaRegHeart } from "react-icons/fa";
import { FaHeart } from "react-icons/fa";
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
        <Button 
            onClick={handleClick} 
            variant="outline" 
            colorPalette="purple"
        >
            {isSaved ? <FaHeart /> : <FaRegHeart />} {isSaved ? "Saved" : "Save"}
        </Button>
    );
}
