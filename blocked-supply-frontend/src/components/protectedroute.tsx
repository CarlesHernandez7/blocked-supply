"use client"

import { ReactNode, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";

interface ProtectedRouteProps {
    children: ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
    const { token } = useAuth();
    const router = useRouter();

    useEffect(() => {
        if (!token) {
            router.push("/auth");
        }
    }, [token, router]);

    return token ? <>{children}</> : null;
};

export default ProtectedRoute;
