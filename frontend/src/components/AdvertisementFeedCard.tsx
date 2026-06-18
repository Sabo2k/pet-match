import { Card, Image, Text, Heading, Box, Link } from "@chakra-ui/react";
import { useState } from "react";
import DropDownButton from "./DropDownButton";
import HeartButton from "./HeartButton";
import type { AdvertisementDto } from "../hooks/useAdvertisements";

const DEFAULT_IMAGE = "https://t4.ftcdn.net/jpg/19/64/05/65/360_F_1964056578_wad19lbTilPQIGgB4W1cvavaSVGAFWtn.jpg";

export default function AdvertisementFeedCard({
    advertisement
}: {
    advertisement: AdvertisementDto
}) {
    const { title, description, images } = advertisement;
    const [isHovered, setIsHovered] = useState(false);

    // Get the primary image or fall back to the first image or use fallback
    const displayImage = images?.find(img => img.isPrimary)?.imageUrl
        || images?.[0]?.imageUrl
        || DEFAULT_IMAGE;

    return (
        <Card.Root
            width="300px"
            overflow="hidden"
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
            _hover={{
                boxShadow: "lg",
                transition: "box-shadow 0.1s",
            }}
        >
            <Box position="relative">
                <Link href={`/advertisement/${advertisement.id}`}>
                    <Image
                        src={displayImage}
                        alt={title}
                        objectFit="cover"
                        height="180px"
                        width="100%"
                    />
                </Link>
                <HeartButton advertisementId={advertisement.id} isCardHovered={isHovered} />
            </Box>
            <Card.Body>
                <Box
                    display="flex"
                    justifyContent="space-between"
                    alignItems="flex-start"
                >
                    <Box flex="1" mr={2}>
                        <Link href={`/advertisement/${advertisement.id}`}>
                            <Heading size="md" mb={1}>{title}</Heading>
                        </Link>
                        <Text color="gray.600" fontSize="sm" lineClamp="2">
                            {description}
                        </Text>
                        
                    </Box>
                    <DropDownButton />
                </Box>
            </Card.Body>
        </Card.Root>
    );
}