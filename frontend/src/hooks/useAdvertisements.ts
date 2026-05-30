import { useQuery } from "@tanstack/react-query";

export interface AuthorDto {
    id: string;
    username: string;
}

export interface AdvertisementDto {
    id: string;
    title: string;
    description: string;
    age: number;
    price: number;
    location: string;
    author: AuthorDto;
    createdAt: string;
    updatedAt: string;
}

const fetchAdvertisements = async (): Promise<AdvertisementDto[]> => {
    const response = await fetch("http://localhost:8080/api/v1/advertisements", {
        method: "GET",
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error("Failed to fetch advertisements");
    }

    return response.json();
};

export const useAdvertisements = () => {
    return useQuery({
        queryKey: ["advertisements"],
        queryFn: fetchAdvertisements,
        staleTime: 5 * 60 * 1000, // 5 minutes
    });
};
