// src/contexts/AuthContext.tsx
import { createContext, useState, useEffect, type ReactNode } from "react";

type AuthContextType = {
    isAuthenticated: boolean;
    isAuthLoading: boolean;
    setIsAuthenticated: (isAuthenticated: boolean) => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

type AuthProviderProps = {
    children: ReactNode;
};

export const AuthProvider = ({ children }: AuthProviderProps) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [isAuthLoading, setIsAuthLoading] = useState<boolean>(true);

    useEffect(() => {
        fetch("http://localhost:8080/api/v1/auth/me", {
            method: "GET",
            credentials: "include",
        }).then((res) => {
            setIsAuthenticated(res.ok);
        }).catch(() => {
            setIsAuthenticated(false);
        }).finally(() => {
            setIsAuthLoading(false);
        });
    }, []);

    return (
        <AuthContext.Provider value={{ isAuthenticated, isAuthLoading, setIsAuthenticated }}>
            {children}
        </AuthContext.Provider>
    );
};

export { AuthContext };