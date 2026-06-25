import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";

export interface AuthorDto {
    id: string;
    username: string;
}

export interface ImageDto {
    id: string;
    imageUrl: string;
    isPrimary: boolean;
    createdAt: string;
}

export interface AdvertisementDto {
    id: string;
    title: string;
    description: string;
    age: number;
    price: number;
    location: string;
    author: AuthorDto;
    images: ImageDto[];
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

const fetchAdvertisementById = async (id: string): Promise<AdvertisementDto> => {
    const response = await fetch(`http://localhost:8080/api/v1/advertisements/${id}`, {
        method: "GET",
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error("Failed to fetch advertisement");
    }

    return response.json();
};

export const useAdvertisementById = (id: string) => {
    return useQuery({
        queryKey: ["advertisement", id],
        queryFn: () => fetchAdvertisementById(id),
        staleTime: 5 * 60 * 1000, // 5 minutes
    });
};

export interface UpdateAdvertisementPayload {
    id: string;
    title: string;
    description: string;
    age: number;
    price: number;
    location: string;
    images?: { url: string; isPrimary: boolean }[];
}

export const useUpdateAdvertisement = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: async ({ id, ...body }: UpdateAdvertisementPayload) => {
            const response = await fetch(`http://localhost:8080/api/v1/advertisements/${id}`, {
                method: "PUT",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body),
            });
            if (!response.ok) throw new Error("Failed to update advertisement");
            return response.json() as Promise<AdvertisementDto>;
        },
        onSuccess: (_, { id }) => {
            queryClient.invalidateQueries({ queryKey: ["advertisement", id] });
            queryClient.invalidateQueries({ queryKey: ["advertisements"] });
        },
    });
};

export const useDeleteAdvertisement = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: async (id: string) => {
            const response = await fetch(`http://localhost:8080/api/v1/advertisements/${id}`, {
                method: "DELETE",
                credentials: "include",
            });
            if (!response.ok) throw new Error("Failed to delete advertisement");
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["advertisements"] });
        },
    });
};
