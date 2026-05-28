// src/contexts/AuthContext.tsx
import { createContext, useState, type ReactNode } from "react";

type AuthContextType = {
    isAuthenticated: boolean;
    setIsAuthenticated: (isAuthenticated: boolean) => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

type AuthProviderProps = {
    children: ReactNode;
};

export const AuthProvider = ({ children }: AuthProviderProps) => {
    // Track authentication state for UI purposes.
    // The actual JWT token is stored securely in an httpOnly cookie managed by the browser,
    // and is automatically sent with requests. JavaScript cannot access httpOnly cookies.
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

    return (
        <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated }}>
            {children}
        </AuthContext.Provider>
    );
};

export { AuthContext };