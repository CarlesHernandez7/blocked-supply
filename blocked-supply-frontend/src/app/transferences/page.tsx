"use client";

import {useEffect, useState} from "react";
import Loading from "@/components/loading";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import Link from "next/link";
import ProtectedRoute from "@/components/protectedroute";
import api from "@/utils/baseApi";

interface ShipmentRecord {
    shipmentId: number;
    sku: string;
    createdAt: string;
    deliveryDate: string;
    state: string;
}

export default function TransferencesPage() {
    const [shipments, setShipments] = useState<ShipmentRecord[] | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchShipments = async () => {
            setLoading(true);
            setError(null);

            const token = localStorage.getItem("authToken");

            if (token) {
                try {
                    const response = await fetch(`${api.baseURL}/api/records/owner`, {
                        method: "GET",
                        headers: {
                            "Authorization": `Bearer ${token}`,
                        },
                    });

                    if (!response.ok) {
                        setError("You do not own any shipment.");
                        setShipments(null);
                        return;
                    }

                    const data: ShipmentRecord[] = await response.json();
                    setShipments(data.length ? data : null);
                } catch (err) {
                    console.error(err);
                    setError("An error occurred while fetching shipments.");
                } finally {
                    setLoading(false);
                }
            } else {
                console.warn("No auth token found in localStorage.");
            }

        };
        fetchShipments();
    }, []);

    return (
        <ProtectedRoute>
            <div className="relative">
                {loading && <Loading/>} {}

                <div className={`${loading ? "opacity-50 pointer-events-none" : ""}`}>
                    <Card>
                        <CardHeader>
                            <CardTitle>Your Shipments</CardTitle>
                            <CardDescription>List of shipments you own at the moment.</CardDescription>
                        </CardHeader>
                        <CardContent>
                            {error ? (
                                <p className="text-red-500">{error}</p>
                            ) : (
                                <ul className="space-y-2">
                                    {shipments?.map(shipment => (
                                        <li key={shipment.shipmentId}
                                            className="border p-2 rounded transition-colors hover:bg-gray-200/50">
                                            <Link href={`/transferences/${shipment.shipmentId}`} className="block p-2">
                                                <p><strong>SKU:</strong> {shipment.sku}</p>
                                                <p><strong>Status:</strong> {shipment.state}</p>
                                                <p><strong>Delivery Date:</strong> {shipment.deliveryDate}</p>
                                                <p><strong>Created
                                                    At:</strong> {new Date(shipment.createdAt).toLocaleString()}</p>
                                            </Link>
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </CardContent>
                    </Card>
                </div>
            </div>
        </ProtectedRoute>
    );
}

