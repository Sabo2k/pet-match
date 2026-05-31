import { Box, Spinner, Text } from "@chakra-ui/react";
import AdvertisementFeedCard from "../AdvertisementFeedCard";
import Navbar from "../navbar/Navbar";
import { useAdvertisements } from "../../hooks/useAdvertisements";
import CreateAdvertisementButton from "./CreateAdvertisementButton";
/**
 * 
 * @returns JSX element
 */
export default function HomePage() {
    const { data: advertisements, isLoading, error } = useAdvertisements();
    
    return (
        <>
            <Navbar/>
            <CreateAdvertisementButton/>
            <Box p={6}>
                {isLoading && (
                    <Box display="flex" justifyContent="center" alignItems="center" minH="400px">
                        <Spinner size="lg" />
                    </Box>
                )}
                
                {error && (
                    <Text color="red.500" textAlign="center">
                        Failed to load advertisements. Please try again later.
                    </Text>
                )}
                
                {advertisements && advertisements.length > 0 ? (
                    <Box display="flex" flexWrap="wrap" gap={6}>
                        {advertisements.map((ad) => (
                            <AdvertisementFeedCard 
                                key={ad.id} 
                                advertisement={ad}
                            />
                        ))}
                    </Box>
                ) : (
                    !isLoading && (
                        <Text textAlign="center" color="gray.500">
                            No advertisements found.
                        </Text>
                    )
                )}
            </Box>
        </>
    );
}