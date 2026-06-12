import { Avatar, Box, Flex, Spinner, Tabs, Text } from "@chakra-ui/react";
import Navbar from "../navbar/Navbar";
import AdvertisementFeedCard from "../AdvertisementFeedCard";
import { useCurrentUser, useCreatedAdvertisements, useSavedAdvertisementsDetails } from "../../hooks/useUserProfile";

export default function UserProfilePage() {
    const { data: user, isLoading: userLoading } = useCurrentUser();
    const { data: createdAds, isLoading: createdLoading } = useCreatedAdvertisements();
    const { data: savedAds, isLoading: savedLoading } = useSavedAdvertisementsDetails();

    return (
        <>
            <Navbar />
            <Box maxW="1200px" mx="auto" px={8} py={10}>
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

                <Tabs.Root
                    defaultValue="created"
                    variant="plain"
                    css={{
                        "--tabs-indicator-bg": "colors.gray.subtle",
                        "--tabs-indicator-shadow": "shadows.xs",
                        "--tabs-trigger-radius": "radii.full",
                    }}
                >
                    <Tabs.List mb={6}>
                        <Tabs.Trigger value="created">Created Advertisements</Tabs.Trigger>
                        <Tabs.Trigger value="saved">Saved Advertisements</Tabs.Trigger>
                        <Tabs.Indicator />
                    </Tabs.List>

                    <Tabs.Content value="created">
                        {createdLoading ? (
                            <Flex justify="center" mt={8}><Spinner /></Flex>
                        ) : createdAds && createdAds.length > 0 ? (
                            <Flex flexWrap="wrap" gap={6}>
                                {createdAds.map((ad) => (
                                    <AdvertisementFeedCard key={ad.id} advertisement={ad} />
                                ))}
                            </Flex>
                        ) : (
                            <Text textAlign="center" color="gray.500" mt={8}>
                                No created advertisements yet.
                            </Text>
                        )}
                    </Tabs.Content>

                    <Tabs.Content value="saved">
                        {savedLoading ? (
                            <Flex justify="center" mt={8}><Spinner /></Flex>
                        ) : savedAds && savedAds.length > 0 ? (
                            <Flex flexWrap="wrap" gap={6}>
                                {savedAds.map((ad) => (
                                    <AdvertisementFeedCard key={ad.id} advertisement={ad} />
                                ))}
                            </Flex>
                        ) : (
                            <Text textAlign="center" color="gray.500" mt={8}>
                                No saved advertisements yet.
                            </Text>
                        )}
                    </Tabs.Content>
                </Tabs.Root>
            </Box>
        </>
    );
}
