import { useQuery } from "@tanstack/react-query";
import type { AdvertisementDto } from "./useAdvertisements";

const API_BASE = "http://localhost:8080/api/v1";

export interface UserProfileDto {
    id: string;
    username: string;
}

const fetchCurrentUser = async (): Promise<UserProfileDto> => {
    const response = await fetch(`${API_BASE}/users/me`, { credentials: "include" });
    if (!response.ok) throw new Error("Failed to fetch user profile");
    return response.json();
};

const fetchCreatedAdvertisements = async (): Promise<AdvertisementDto[]> => {
    const response = await fetch(`${API_BASE}/users/me/created-advertisements`, { credentials: "include" });
    if (!response.ok) throw new Error("Failed to fetch created advertisements");
    return response.json();
};

const fetchSavedAdvertisementsDetails = async (): Promise<AdvertisementDto[]> => {
    const response = await fetch(`${API_BASE}/users/me/saved-advertisements/details`, { credentials: "include" });
    if (!response.ok) throw new Error("Failed to fetch saved advertisements");
    return response.json();
};

const fetchUserById = async (userId: string): Promise<UserProfileDto> => {
    const response = await fetch(`${API_BASE}/users/${userId}`, { credentials: "include" });
    if (!response.ok) throw new Error("Failed to fetch user");
    return response.json();
};

const fetchUserAdvertisements = async (userId: string): Promise<AdvertisementDto[]> => {
    const response = await fetch(`${API_BASE}/users/${userId}/advertisements`, { credentials: "include" });
    if (!response.ok) throw new Error("Failed to fetch user advertisements");
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

export const useUserById = (userId: string) => {
    return useQuery({
        queryKey: ["user", userId],
        queryFn: () => fetchUserById(userId),
        enabled: !!userId,
        staleTime: 10 * 60 * 1000,
    });
};

export const useUserAdvertisements = (userId: string) => {
    return useQuery({
        queryKey: ["userAdvertisements", userId],
        queryFn: () => fetchUserAdvertisements(userId),
        enabled: !!userId,
        staleTime: 5 * 60 * 1000,
    });
};
