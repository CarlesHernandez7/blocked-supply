"use client"

import {Activity, Box, RefreshCw} from "lucide-react"

import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {useEffect, useState} from "react";
import Loading from "@/components/loading";
import api from "@/utils/baseApi";

export default function HomePage() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const defaultString = "-";
    const [total, setTotal] = useState(defaultString);
    const [active, setActive] = useState(defaultString);
    const [completed, setCompleted] = useState(defaultString);
    const [successRate, setSuccessRate] = useState(defaultString);

    useEffect(() => {
        const fetchShipments = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await fetch(`${api.baseURL}/api/records/stats`);
                if (!response.ok) {
                    setError("An error occurred while fetching stats data.");
                    return;
                }

                const data = await response.json();
                console.log(data);
                setTotal(data.totalShipments ?? defaultString);
                setActive(data.activeShipments ?? defaultString);
                setCompleted(data.deliveredToday ?? defaultString);
                setSuccessRate(data.successRate ?? defaultString);
            } catch (err) {
                console.error(err);
                setError("An error occurred while fetching stats data.");
            } finally {
                setLoading(false);
            }
        };
        fetchShipments();
    }, []);

    return (
        <div className="relative">
            {loading && <Loading/>} {}
            <div className={`${loading ? "opacity-50 pointer-events-none" : "space-y-4"}`}>
                {error && <p className="text-red-500">{error}</p>}
                <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
                    <Card>
                        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                            <CardTitle className="text-sm font-medium">Total Products</CardTitle>
                            <Box className="h-4 w-4 text-muted-foreground"/>
                        </CardHeader>
                        <CardContent>
                            <div className="text-2xl font-bold">{total}</div>
                            <p className="text-xs text-muted-foreground">Amount of products handled by Blocked
                                Supply</p>
                        </CardContent>
                    </Card>
                    <Card>
                        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                            <CardTitle className="text-sm font-medium">Active Transfers</CardTitle>
                            <RefreshCw className="h-4 w-4 text-muted-foreground"/>
                        </CardHeader>
                        <CardContent>
                            <div className="text-2xl font-bold">{active}</div>
                            <p className="text-xs text-muted-foreground">Products not delivered yet</p>
                        </CardContent>
                    </Card>
                    <Card>
                        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                            <CardTitle className="text-sm font-medium">Completed Today</CardTitle>
                            <Activity className="h-4 w-4 text-muted-foreground"/>
                        </CardHeader>
                        <CardContent>
                            <div className="text-2xl font-bold">{completed}</div>
                            <p className="text-xs text-muted-foreground">Products delivered today</p>
                        </CardContent>
                    </Card>
                    <Card>
                        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                            <CardTitle className="text-sm font-medium">Success Rate</CardTitle>
                            <Activity className="h-4 w-4 text-muted-foreground"/>
                        </CardHeader>
                        <CardContent>
                            <div className="text-2xl font-bold">{successRate}</div>
                            <p className="text-xs text-muted-foreground">On-time delivery rate</p>
                        </CardContent>
                    </Card>
                </div>
                <Card>
                    <CardHeader>
                        <CardTitle>About Supply Chain Management</CardTitle>
                        <CardDescription>Overview of our supply chain tracking system</CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <p>
                            Welcome to our blockchain-based supply chain management system. This platform helps you
                            track and manage products
                            throughout
                            their lifecycle in the supply chain. Here&apos;s what you can do:
                        </p>
                        <ul className="list-disc pl-4 space-y-2">
                            <li>Create new products and enter them into the supply chain</li>
                            <li>Track products in real-time as they move through different stages</li>
                            <li>Transfer products between different states, owners and locations</li>
                        </ul>
                    </CardContent>
                </Card>
            </div>
        </div>
    )
}

