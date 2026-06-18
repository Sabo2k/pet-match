import { useParams } from "react-router-dom";
import { Avatar, Box, Flex, Spinner, Text } from "@chakra-ui/react";
import Navbar from "../navbar/Navbar";
import AdvertisementFeedCard from "../AdvertisementFeedCard";
import { useUserById, useUserAdvertisements } from "../../hooks/useUserProfile";

export default function ProfilePage() {
    const { userId } = useParams<{ userId: string }>();
    const { data: user, isLoading: userLoading, error: userError } = useUserById(userId ?? "");
    const { data: advertisements, isLoading: adsLoading } = useUserAdvertisements(userId ?? "");

    return (
        <>
            <Navbar />
            <Box maxW="1200px" mx="auto" px={8} py={10}>
                {userError && (
                    <Text color="red.500" textAlign="center">
                        User not found.
                    </Text>
                )}

                <Flex direction="column" align="center" mb={10} gap={3}>
                    <Avatar.Root size="2xl">
                        <Avatar.Fallback name={user?.username} />
                        <Avatar.Image />
                    </Avatar.Root>
                    {userLoading ? (
                        <Spinner size="sm" />
                    ) : (
                        <Text fontWeight="bold" fontSize="xl" color="gray.700">
                            {user?.username}
                        </Text>
                    )}
                </Flex>

                <Text fontWeight="semibold" fontSize="lg" color="gray.600" mb={5}>
                    Advertisements
                </Text>

                {adsLoading ? (
                    <Flex justify="center" mt={8}>
                        <Spinner />
                    </Flex>
                ) : advertisements && advertisements.length > 0 ? (
                    <Flex flexWrap="wrap" gap={6}>
                        {advertisements.map((ad) => (
                            <AdvertisementFeedCard key={ad.id} advertisement={ad} />
                        ))}
                    </Flex>
                ) : (
                    <Text textAlign="center" color="gray.500" mt={8}>
                        No advertisements yet.
                    </Text>
                )}
            </Box>
        </>
    );
}
