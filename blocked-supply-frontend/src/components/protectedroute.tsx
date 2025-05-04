"use client";

import {useEffect} from "react";
import {useRouter} from "next/navigation";
import {useAuth} from "@/context/AuthContext";

export default function ProtectedRoute({children}: {children: React.ReactNode}) {
    const router = useRouter();
    const {token} = useAuth();

    useEffect(() => {
        if (!token) {
            router.push("/auth");
        }
    }, [token, router]);

    if (!token) return null;

    return <>{children}</>;
}
