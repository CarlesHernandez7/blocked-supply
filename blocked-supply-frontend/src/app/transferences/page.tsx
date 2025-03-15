"use client";

import {useEffect, useState} from "react";
import Loading from "@/components/loading";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import Link from "next/link";

const API_URL = process.env.NEXT_PUBLIC_API_URL;
const USER_ID = "1";

interface ShipmentRecord {
    shipmentId: number;
    createdAt: string;
    status: string;
}

export default function TransferencesPage() {
    const [shipments, setShipments] = useState<ShipmentRecord[] | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchShipments = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await fetch(`${API_URL}/records/owner/${USER_ID}`);
                if (!response.ok) {
                    setError("You are not a participant in any shipment.");
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
        };
        fetchShipments();
    }, []);

    return (
        <div className="relative">
            {loading && <Loading />} {}

            <div className={`${loading ? "opacity-50 pointer-events-none" : ""}`}>
                <Card>
                    <CardHeader>
                        <CardTitle>Your Shipments</CardTitle>
                        <CardDescription>List of shipments you are part of.</CardDescription>
                    </CardHeader>
                    <CardContent>
                        {error ? (
                            <p className="text-red-500">{error}</p>
                        ) : (
                            <ul className="space-y-2">
                                {shipments?.map(shipment => (
                                    <li key={shipment.shipmentId} className="border p-2 rounded transition-colors hover:bg-gray-200/50">
                                        <Link href={`/transferences/${shipment.shipmentId}`} className="block p-2">
                                            <p><strong>ID:</strong> {shipment.shipmentId}</p>
                                            <p><strong>Status:</strong> {shipment.status}</p>
                                            <p><strong>Created At:</strong> {new Date(shipment.createdAt).toLocaleString()}</p>
                                        </Link>
                                    </li>
                                ))}
                            </ul>
                        )}
                    </CardContent>
                </Card>
            </div>
        </div>
    );
}

