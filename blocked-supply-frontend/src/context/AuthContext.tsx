import {createContext, useContext, useEffect, useState} from "react";
import {usePathname, useRouter} from "next/navigation";
import Loading from "@/components/loading";
import api from "@/utils/baseApi";

interface AuthContextType {
    token: string | null;
    login: (token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({children}: { children: React.ReactNode }) => {
    const [token, setToken] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const router = useRouter();
    const pathname = usePathname();

    useEffect(() => {
        const checkAuth = () => {
            const storedToken = localStorage.getItem("authToken");
            const publicPaths = ["/", "/auth"];

            if (!storedToken && !publicPaths.includes(pathname)) {
                router.push("/auth");
            } else {
                setToken(storedToken);
            }
            setLoading(false);
        };

        checkAuth();
    }, [pathname, router]);

    const login = (newToken: string) => {
        localStorage.setItem("authToken", newToken);
        setToken(newToken);
        router.push("/");
    };

    const logout = async () => {
        const token = localStorage.getItem("authToken");
        await fetch(`${api.baseURL}/auth/logout`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
        });
        localStorage.removeItem("authToken");
        setToken(null);
        router.push("/");
    };

    if (loading) {
        return <Loading/>;
    }

    return (
        <AuthContext.Provider value={{token, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};
