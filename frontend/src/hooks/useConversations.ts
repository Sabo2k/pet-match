import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";

const API_BASE = "http://localhost:8080/api/v1";

export interface ParticipantDto {
    id: string;
    username: string;
}

export interface MessageDto {
    id: string;
    senderId: string;
    senderUsername: string;
    content: string;
    sentAt: string;
}

export interface ConversationDto {
    id: string;
    participants: ParticipantDto[];
    advertisementId: string | null;
    advertisementTitle: string | null;
    messages: MessageDto[];
    createdAt: string;
}

export const useConversation = (id: string) => {
    return useQuery({
        queryKey: ["conversation", id],
        queryFn: async (): Promise<ConversationDto> => {
            const res = await fetch(`${API_BASE}/conversations/${id}`, { credentials: "include" });
            if (!res.ok) throw new Error("Failed to fetch conversation");
            return res.json();
        },
        enabled: !!id,
    });
};

export const useConversations = () => {
    return useQuery({
        queryKey: ["conversations"],
        queryFn: async (): Promise<ConversationDto[]> => {
            const res = await fetch(`${API_BASE}/conversations`, { credentials: "include" });
            if (!res.ok) throw new Error("Failed to fetch conversations");
            return res.json();
        },
    });
};

export const useStartConversation = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: async (payload: {
            recipientId: string;
            advertisementId: string;
            message: string;
        }): Promise<ConversationDto> => {
            const res = await fetch(`${API_BASE}/conversations`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload),
            });
            if (!res.ok) throw new Error("Failed to start conversation");
            return res.json();
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["conversations"] });
        },
    });
};

export const useSendMessage = (conversationId: string) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: async (content: string): Promise<MessageDto> => {
            const res = await fetch(`${API_BASE}/conversations/${conversationId}/messages`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ content }),
            });
            if (!res.ok) throw new Error("Failed to send message");
            return res.json();
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["conversation", conversationId] });
        },
    });
};
