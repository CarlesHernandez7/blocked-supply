"use client";

import {useEffect, useState} from "react";
import {useParams} from "next/navigation";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import Link from "next/link";
import Loading from "@/components/loading";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

interface Shipment {
    id: number;
    name: string;
    description: string;
    origin: string;
    destination: string;
    units: number;
    weight: number;
    currentState: string;
    currentOwner: string;
}

export default function ShipmentDetail() {
    const { id } = useParams();
    const [shipment, setShipment] = useState<Shipment | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchShipment = async () => {
            setLoading(true);
            try {
                const response = await fetch(`${API_URL}/shipment/${id}`);
                if (!response.ok) {
                    const errorMessage = await response.text();
                    throw new Error(errorMessage);
                }
                const data: Shipment = await response.json();
                setShipment(data);
            } catch (err) {
                console.error(err);
                setError("An error occurred while fetching shipment.");
            } finally {
                setLoading(false);
            }
        };
        fetchShipment();
    }, [id]);

    if (loading) {
        return <Loading />;
    }

    if (error) {
        return (
            <Card>
                <CardHeader>
                    <CardTitle>Error</CardTitle>
                </CardHeader>
                <CardContent>
                    <p className="text-red-500">{error}</p>
                    <Link href="/shipments">
                        <Button className="mt-4">Back to Shipments</Button>
                    </Link>
                </CardContent>
            </Card>
        );
    }

    if (!shipment) {
        return null;
    }

    return (
        <Card>
            <CardHeader>
                <CardTitle>Shipment Details</CardTitle>
            </CardHeader>
            <CardContent>
                <p><strong>ID:</strong> {shipment.id}</p>
                <p><strong>Name:</strong> {shipment.name}</p>
                <p><strong>Description:</strong> {shipment.description}</p>
                <p><strong>Origin:</strong> {shipment.origin}</p>
                <p><strong>Destination:</strong> {shipment.destination}</p>
                <p><strong>Units:</strong> {shipment.units}</p>
                <p><strong>Weight:</strong> {shipment.weight} kg</p>
                <p><strong>Current State:</strong> {shipment.currentState}</p>
                <p><strong>Current Owner:</strong> {shipment.currentOwner}</p>
                <Link href="/shipments">
                    <Button className="mt-4">Back to Shipments</Button>
                </Link>
            </CardContent>
        </Card>
    );
}
