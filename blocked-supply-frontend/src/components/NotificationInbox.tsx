"use client"

import api from "@/utils/baseApi";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import {Button} from "@/components/ui/button";
import {Bell} from "lucide-react";

interface NotificationData {
    id: number;
    message: string;
    createdAt: string;
    isRead: boolean;
}

export function NotificationInbox() {
    const [open, setOpen] = useState(false);
    const [notifications, setNotifications] = useState<NotificationData[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();
    const hasUnread = notifications.some((n) => !n.isRead);

    useEffect(() => {
        fetchNotifications();
        const interval = setInterval(fetchNotifications, 60000);
        return () => clearInterval(interval);
    }, []);

    useEffect(() => {
        if (open) {
            fetchNotifications();
        }
    }, [open]);

    const fetchNotifications = async () => {
        setLoading(true);
        setError(null);

        try {
            console.log("Fetching notifications...", localStorage.getItem("authToken"));
            const token = localStorage.getItem("authToken");
            if (!token) {
                router.push("/auth");
                return;
            }

            const response = await fetch(`${api.baseURL}/api/notification`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
            });

            if (!response.ok) {
                setError("Could not load notifications.");
                return;
            }

            const data = await response.json();
            setNotifications(data);
        } catch (err) {
            console.error(err);
            setError("Could not load notifications.");
        } finally {
            setLoading(false);
        }
    };

    const markAsRead = async (id: number) => {
        try {
            const token = localStorage.getItem("authToken");
            const response = await fetch(`${api.baseURL}/api/notification/read/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });

            if (response.ok) {
                setNotifications((prev) =>
                    prev.map((n) => (n.id === id ? {...n, read: true} : n))
                );
                await fetchNotifications();
            } else {
                console.error("Failed to mark notification as read.");
            }
        } catch (err) {
            console.error("Error marking notification as read:", err);
        }
    };

    const markAllAsRead = async () => {
        try {
            const token = localStorage.getItem("authToken");
            const response = await fetch(`${api.baseURL}/api/notification/read`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });

            if (response.ok) {
                setNotifications((prev) =>
                    prev.map((n) => ({...n, isRead: true}))
                );
                await new Promise(resolve => setTimeout(resolve, 1000));
                await fetchNotifications();
            } else {
                console.error("Failed to mark all notifications as read.");
            }
        } catch (err) {
            console.error("Error marking all as read:", err);
        }
    };

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="ghost" size="icon" className="relative rounded-full">
                    <Bell className="h-5 w-5"/>
                    {hasUnread && (
                        <span
                            className="absolute top-0 right-0 block h-2 w-2 rounded-full bg-red-500 ring-2 ring-background"/>
                    )}
                    <span className="sr-only">Notifications</span>
                </Button>
            </DialogTrigger>

            <DialogContent className="sm:max-w-xl max-h-[80vh] overflow-y-auto">
                <DialogHeader>
                    <DialogTitle>Notifications</DialogTitle>
                    <DialogDescription>See your recent activity</DialogDescription>
                    {notifications.length > 0 && (
                        <div className="flex justify-end">
                            <Button
                                variant="ghost"
                                size="sm"
                                className="text-xs"
                                onClick={markAllAsRead}
                            >
                                Mark all as read
                            </Button>
                        </div>
                    )}
                </DialogHeader>

                <div className="space-y-4 py-4">
                    {loading ? (
                        <p>Loading...</p>
                    ) : error ? (
                        <p className="text-red-500">{error}</p>
                    ) : notifications.length > 0 ? (
                        <div className="space-y-2">
                            {notifications.map((notif) => (
                                <div
                                    key={notif.id}
                                    className={`rounded-md border p-3 ${
                                        notif.isRead ? "bg-muted text-muted-foreground" : "bg-background text-foreground"
                                    }`}
                                >
                                    <div className="flex justify-between items-start">
                                        <div>
                                            <div className="text-sm">{notif.message}</div>
                                            <div className="text-xs text-gray-500 mt-1">
                                                {new Date(notif.createdAt).toLocaleString()}
                                            </div>
                                        </div>
                                        {!notif.isRead && (
                                            <Button
                                                size="sm"
                                                variant="outline"
                                                className="text-xs ml-2"
                                                onClick={() => markAsRead(notif.id)}
                                            >
                                                Mark as Read
                                            </Button>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p>No notifications found.</p>
                    )}
                </div>

                <DialogFooter>
                    <Button variant="outline" onClick={() => setOpen(false)}>Close</Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
