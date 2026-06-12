import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

const fetchSavedAdvertisementIds = async (): Promise<string[]> => {
    const response = await fetch("http://localhost:8080/api/v1/users/me/saved-advertisements", {
        credentials: "include",
    });
    if (!response.ok) throw new Error("Failed to fetch saved advertisements");
    const ids: string[] = await response.json();
    return ids;
};

export const useSavedAdvertisements = (isAuthenticated: boolean) => {
    return useQuery({
        queryKey: ["savedAdvertisements"],
        queryFn: fetchSavedAdvertisementIds,
        enabled: isAuthenticated,
        staleTime: 5 * 60 * 1000,
    });
};

export const useSaveAdvertisement = () => {
    const queryClient = useQueryClient();

    const save = useMutation({
        mutationFn: (advertisementId: string) =>
            fetch(`http://localhost:8080/api/v1/users/me/saved-advertisements/${advertisementId}`, {
                method: "POST",
                credentials: "include",
            }).then((res) => {
                if (!res.ok) throw new Error("Failed to save advertisement");
            }),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ["savedAdvertisements"] }),
    });

    const unsave = useMutation({
        mutationFn: (advertisementId: string) =>
            fetch(`http://localhost:8080/api/v1/users/me/saved-advertisements/${advertisementId}`, {
                method: "DELETE",
                credentials: "include",
            }).then((res) => {
                if (!res.ok) throw new Error("Failed to unsave advertisement");
            }),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ["savedAdvertisements"] }),
    });

    return { save, unsave };
};
