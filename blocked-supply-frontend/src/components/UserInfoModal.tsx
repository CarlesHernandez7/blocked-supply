"use client";

import {useEffect, useState} from "react";
import {LogOut, User} from "lucide-react";
import {Button} from "@/components/ui/button";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import {useRouter} from "next/navigation";
import {useAuth} from "@/context/AuthContext";
import api from "@/utils/baseApi";

interface UserData {
    name: string;
    email: string;
    blockchainAddress: string;
}

export function UserInfoModal() {
    const [open, setOpen] = useState(false);
    const [userData, setUserData] = useState<UserData | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();
    const {logout} = useAuth();

    useEffect(() => {
        if (open) {
            fetchUserData();
        }
    }, [open]);

    const fetchUserData = async () => {
        setLoading(true);
        setError(null);
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                router.push("/auth");
            }
            const response = await fetch(`${api.baseURL}/api/user`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
            });

            if (!response.ok) {
                router.push("/auth");
            } else {
                const data = await response.json();
                setUserData(data);
            }
        } catch (err) {
            router.push("/auth");
            console.error(err);
            setError("Could not load user data.");
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        console.log("User logged out");
        logout();
        setOpen(false);
    };

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="ghost" size="icon" className="rounded-full">
                    <User className="h-5 w-5"/>
                    <span className="sr-only">User profile</span>
                </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-xl">
                <DialogHeader>
                    <DialogTitle>User Information</DialogTitle>
                    <DialogDescription>View and manage your account information</DialogDescription>
                </DialogHeader>
                <div className="space-y-4 py-4">
                    {loading ? (
                        <p>Loading...</p>
                    ) : error ? (
                        <p className="text-red-500">{error}</p>
                    ) : userData ? (
                        <div className="rounded-md border p-4">
                            <div className="space-y-3">
                                <div>
                                    <div className="text-sm font-medium text-muted-foreground">Name</div>
                                    <div>{userData.name}</div>
                                </div>
                                <div>
                                    <div className="text-sm font-medium text-muted-foreground">Email</div>
                                    <div>{userData.email}</div>
                                </div>
                                <div>
                                    <div className="text-sm font-medium text-muted-foreground">Blockchain Address</div>
                                    {userData.blockchainAddress ? (
                                        <div>{userData.blockchainAddress}</div>
                                    ) : (
                                        <div>No blockchain address set.</div>
                                    )}
                                </div>
                            </div>
                        </div>

                    ) : (
                        <p>No user data available.</p>
                    )}
                </div>
                <DialogFooter className="sm:justify-between">
                    <Button variant="outline" type="button" onClick={() => setOpen(false)}>
                        Close
                    </Button>
                    <Button variant="destructive" type="button" onClick={handleLogout}
                            className="flex items-center gap-2">
                        <LogOut className="h-4 w-4"/>
                        Logout
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
