import { useQuery } from "@tanstack/react-query";

export interface CategoryDto {
    id: string;
    name: string;
    advertisementCount: number;
}

const fetchCategories = async (): Promise<CategoryDto[]> => {
    const apiUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";
    const response = await fetch(`${apiUrl}/api/v1/categories`, {
        method: "GET",
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error("Failed to fetch categories");
    }

    return response.json();
};

export const useCategories = () => {
    return useQuery({
        queryKey: ["categories"],
        queryFn: fetchCategories,
        staleTime: 10 * 60 * 1000, // 10 minutes
    });
};
