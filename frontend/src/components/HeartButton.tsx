import { Box } from "@chakra-ui/react";
import { FaHeart, FaRegHeart } from "react-icons/fa";
import { useAuth } from "../contexts/useAuth";
import { useSavedAdvertisements, useSaveAdvertisement } from "../hooks/useSavedAdvertisements";

export default function HeartButton({ advertisementId, isCardHovered = false }: { advertisementId: string; isCardHovered?: boolean }) {
    const { isAuthenticated } = useAuth();
    const { data: savedIds } = useSavedAdvertisements(isAuthenticated);
    const { save, unsave } = useSaveAdvertisement();

    if (!isAuthenticated) return null;

    const isSaved = savedIds?.includes(advertisementId) ?? false;

    function handleClick(e: React.MouseEvent) {
        e.preventDefault();
        e.stopPropagation();
        if (isSaved) {
            unsave.mutate(advertisementId);
        } else {
            save.mutate(advertisementId);
        }
    }

    return (
        <Box
            onClick={handleClick}
            position="absolute"
            top={2}
            right={2}
            bg="white"
            borderRadius="full"
            p={1.5}
            cursor="pointer"
            color={isSaved ? "purple.500" : "gray.400"}
            _hover={{ color: "purple.500", transform: "scale(1.1)" }}
            transition="all 0.15s"
            opacity={isSaved || isCardHovered ? 1 : 0}
            shadow="md"
            zIndex={1}
            lineHeight={0}
        >
            {isSaved ? <FaHeart size={16} /> : <FaRegHeart size={16} />}
        </Box>
    );
}
