import { useQuery } from "@tanstack/react-query";
import type { AdvertisementDto } from "./useAdvertisements";

export interface UserProfileDto {
    id: string;
    username: string;
}

const fetchCurrentUser = async (): Promise<UserProfileDto> => {
    const response = await fetch("http://localhost:8080/api/v1/users/me", {
        credentials: "include",
    });
    if (!response.ok) throw new Error("Failed to fetch user profile");
    return response.json();
};

const fetchCreatedAdvertisements = async (): Promise<AdvertisementDto[]> => {
    const response = await fetch("http://localhost:8080/api/v1/users/me/created-advertisements", {
        credentials: "include",
    });
    if (!response.ok) throw new Error("Failed to fetch created advertisements");
    return response.json();
};

const fetchSavedAdvertisementsDetails = async (): Promise<AdvertisementDto[]> => {
    const response = await fetch("http://localhost:8080/api/v1/users/me/saved-advertisements/details", {
        credentials: "include",
    });
    if (!response.ok) throw new Error("Failed to fetch saved advertisements");
    return response.json();
};

export const useCurrentUser = () => {
    return useQuery({
        queryKey: ["currentUser"],
        queryFn: fetchCurrentUser,
        staleTime: 10 * 60 * 1000,
    });
};

export const useCreatedAdvertisements = () => {
    return useQuery({
        queryKey: ["createdAdvertisements"],
        queryFn: fetchCreatedAdvertisements,
        staleTime: 5 * 60 * 1000,
    });
};

export const useSavedAdvertisementsDetails = () => {
    return useQuery({
        queryKey: ["savedAdvertisementsDetails"],
        queryFn: fetchSavedAdvertisementsDetails,
        staleTime: 5 * 60 * 1000,
    });
};
