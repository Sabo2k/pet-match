import { useParams } from "react-router-dom";
import { Box, Button, Card, Flex, Heading, HStack, Link, SimpleGrid, Spinner, Stack, Text } from "@chakra-ui/react";
import { LuCalendar, LuEuro, LuMapPin, LuUser } from "react-icons/lu";
import Navbar from "../navbar/Navbar";
import ImageCarousel from "./ImageCarousel";
import { useAdvertisementById } from "../../hooks/useAdvertisements";
import { useCurrentUser } from "../../hooks/useUserProfile";
import SaveButton from "../SaveButton";
import EditMenu from "./EditMenu";

function InfoCard({ label, value, icon: IconComponent }: { label: string; value: string | number; icon: React.ElementType }) {
    return (
        <Card.Root>
            <Card.Body>
                <Flex align="center" gap={3}>
                    <Box color="purple.500" fontSize="2xl">
                        <IconComponent />
                    </Box>
                    <Box>
                        <Text 
                            fontSize="xs" 
                            color="gray.500" 
                            fontWeight="semibold" 
                            textTransform="uppercase" 
                            letterSpacing="wide"
                        >
                            {label}
                        </Text>
                        <Text fontWeight="semibold" fontSize="md" color="gray.700">
                            {value}
                        </Text>
                    </Box>
                </Flex>
            </Card.Body>
        </Card.Root>
    );
}

export default function AdvertisementPage() {
    const { id } = useParams<{ id: string }>();
    const { data: advertisement, isLoading, error } = useAdvertisementById(id || "");
    const { data: currentUser } = useCurrentUser();
    const isAuthor = !!currentUser && !!advertisement && currentUser.id === advertisement.author.id;

    return (
        <>
            <Navbar />
            <Box p={8} maxW="1200px" mx="auto">
                {isLoading && (
                    <Flex justifyContent="center" alignItems="center" minH="400px">
                        <Spinner size="lg" />
                    </Flex>
                )}

                {error && (
                    <Text color="red.500" textAlign="center">
                        Failed to load advertisement. Please try again later.
                    </Text>
                )}

                {advertisement && (
                    <Stack gap={6}>
                        <HStack align="start" gap={8}>
                            <Box flex="1">
                                <ImageCarousel images={advertisement.images} />
                            </Box>
                            <Stack flex="1" gap={4}>
                                <Heading fontWeight="bold" color="gray.700" size="2xl">
                                    {advertisement.title}
                                </Heading>
                                <Card.Root>
                                    <Card.Body>
                                        <Card.Title color="gray.600" mb={2}>Description</Card.Title>
                                        <Card.Description>{advertisement.description}</Card.Description>
                                    </Card.Body>
                                </Card.Root>
                            </Stack>
                        </HStack>

                        <SimpleGrid columns={4} gap={4}>
                            <InfoCard label="Age" value={advertisement.age} icon={LuCalendar} />
                            <InfoCard label="Price" value={`€ ${advertisement.price}`} icon={LuEuro} />
                            <InfoCard label="Location" value={advertisement.location} icon={LuMapPin} />
                            <Link href={`/profile/${advertisement.author.id}`}>
                                <InfoCard label="Author" value={advertisement.author.username} icon={LuUser} />
                            </Link>
                        </SimpleGrid>

                        <Flex gap={3} justify="flex-end">
                            <SaveButton advertisementId={id || ""} />
                            <Button colorPalette="purple">Contact</Button>
                            {isAuthor && <EditMenu advertisement={advertisement} />}
                        </Flex>
                    </Stack>
                )}
            </Box>
        </>
    );
}
