import { Card, Image, Text, Heading, Box, Link } from "@chakra-ui/react";
import DropDownButton from "./DropDownButton";
import type { AdvertisementDto } from "../hooks/useAdvertisements";

export default function AdvertisementFeedCard({ 
    advertisement 
}: { 
    advertisement: AdvertisementDto 
}) {
    const { title, description } = advertisement;
    
    return (
        <Card.Root 
            width="300px" 
            overflow="hidden"             
            _hover={{
                boxShadow: "lg",
                transition: "box-shadow 0.1s",
            }}
        >
            <Link href="/advertisement">
                <Image
                    src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRN2z0ERwXQUqH29urPuzWueLXKhJAY6SMyAA&s"
                    alt={title}
                    objectFit="cover"
                    height="180px"
                    width="100%"
                />
            </Link>
            <Card.Body>
                <Box 
                    display="flex" 
                    justifyContent="space-between" 
                    alignItems="flex-start"
                >
                    <Box flex="1" mr={2}>
                        <Link href="/advertisement">
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