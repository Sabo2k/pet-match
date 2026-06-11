import { Button } from "@chakra-ui/react";
import { useState } from "react";
import { BsBookmarkFill } from "react-icons/bs";
import { FiBookmark } from "react-icons/fi";

export default function SaveButton() {
    const [isFavorite, setIsFavorite] = useState(false);
 
    function handleFavoriteClick() {
        setIsFavorite(!isFavorite);
    }
 
    return (
        <Button onClick={handleFavoriteClick} variant="outline" colorScheme="teal">
            {isFavorite ? <BsBookmarkFill /> : <FiBookmark />} {isFavorite ? "Saved" : "Save"}
        </Button>
    );
}