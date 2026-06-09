import { useParams } from "react-router-dom";
import { Box, Spinner, Text } from "@chakra-ui/react";
import Navbar from "../navbar/Navbar";
import ImageCarousel from "./ImageCarousel";
import { useAdvertisementById } from "../../hooks/useAdvertisements";

export default function AdvertisementPage() {
    const { id } = useParams<{ id: string }>();
    const { data: advertisement, isLoading, error } = useAdvertisementById(id || "");

    return (
        <>
            <Navbar/>
            <Box p={6}>
                {isLoading && (
                    <Box display="flex" justifyContent="center" alignItems="center" minH="400px">
                        <Spinner size="lg" />
                    </Box>
                )}

                {error && (
                    <Text color="red.500" textAlign="center">
                        Failed to load advertisement. Please try again later.
                    </Text>
                )}

                {advertisement && (
                    <Box>
                        <ImageCarousel images={advertisement.images} />
                        <Box mt={6}>
                            <Text fontSize="2xl" fontWeight="bold">
                                {advertisement.title}
                            </Text>
                            <Text mt={2} color="gray.600">
                                {advertisement.description}
                            </Text>
                            <Box mt={4} display="flex" gap={4}>
                                <Text>
                                    <strong>Age:</strong> {advertisement.age}
                                </Text>
                                <Text>
                                    <strong>Price:</strong> ${advertisement.price}
                                </Text>
                                <Text>
                                    <strong>Location:</strong> {advertisement.location}
                                </Text>
                            </Box>
                            <Text mt={4}>
                                <strong>Posted by:</strong> {advertisement.author.username}
                            </Text>
                        </Box>
                    </Box>
                )}
            </Box>
        </>
    );
}