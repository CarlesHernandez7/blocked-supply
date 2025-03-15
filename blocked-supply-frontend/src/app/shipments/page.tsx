"use client";

import {useEffect, useState} from "react";
import {Button} from "@/components/ui/button";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {Textarea} from "@/components/ui/textarea";
import Link from "next/link";
import Loading from "@/components/loading";

const API_URL = process.env.NEXT_PUBLIC_API_URL;
const USER_ID = "1";

interface ShipmentForm {
    name: string;
    description: string;
    origin: string;
    destination: string;
    units: string;
    weight: string;
}

interface ShipmentRecord {
    shipmentId: number;
    createdAt: string;
    status: string;
}

export default function ShipmentsPage() {
    const [shipments, setShipments] = useState<ShipmentRecord[] | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [showForm, setShowForm] = useState(false);
    const [shipmentForm, setShipmentForm] = useState<ShipmentForm>({
        name: "",
        description: "",
        origin: "",
        destination: "",
        units: "",
        weight: ""
    });
    const [errors, setErrors] = useState<Partial<ShipmentForm>>({});
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchShipments = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await fetch(`${API_URL}/records/participant/${USER_ID}`);
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

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { id, value } = e.target;
        setShipmentForm(prev => ({ ...prev, [id]: value }));
    };

    const validateForm = () => {
        const newErrors: Partial<ShipmentForm> = {};

        Object.entries(shipmentForm).forEach(([key, value]) => {
            if (!value.trim()) {
                newErrors[key as keyof ShipmentForm] = "This field is required";
            }
        });

        if (!/^[1-9]\d*$/.test(shipmentForm.units)) {
            newErrors.units = "Units must be a positive integer";
        }

        if (!/^\d+(\.\d+)?$/.test(shipmentForm.weight) || parseFloat(shipmentForm.weight) <= 0) {
            newErrors.weight = "Weight must be a positive number";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;

        setLoading(true);
        setError(null);

        try {
            const response = await fetch(`${API_URL}/shipment/create`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    ...shipmentForm,
                    units: Number(shipmentForm.units),
                    weight: Number(shipmentForm.weight)
                })
            });

            if (!response.ok) throw new Error("Failed to create shipment");

            setShowForm(false);
            window.location.reload();
        } catch (err) {
            console.error(err);
            setError("An error occurred while creating the shipment.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="relative">
            {loading && <Loading />} {}

            <div className={`${loading ? "opacity-50 pointer-events-none" : ""}`}>
                {!showForm ? (
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
                                            <Link href={`/shipments/${shipment.shipmentId}`} className="block p-2">
                                                <p><strong>ID:</strong> {shipment.shipmentId}</p>
                                                <p><strong>Status:</strong> {shipment.status}</p>
                                                <p><strong>Created At:</strong> {new Date(shipment.createdAt).toLocaleString()}</p>
                                            </Link>
                                        </li>
                                    ))}
                                </ul>
                            )}
                            <Button className="mt-4 w-full" onClick={() => setShowForm(true)}>Create Shipment</Button>
                        </CardContent>
                    </Card>
                ) : (
                    <Card>
                        <CardHeader>
                            <CardTitle>Create New Shipment</CardTitle>
                            <CardDescription>Fill in the details to create a shipment.</CardDescription>
                        </CardHeader>
                        <CardContent>
                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div className="grid gap-2">
                                    <Label htmlFor="name">Shipment Name</Label>
                                    <Input id="name" value={shipmentForm.name} onChange={handleChange} />
                                    {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}
                                </div>
                                <div className="grid gap-2">
                                    <Label htmlFor="description">Description</Label>
                                    <Textarea id="description" value={shipmentForm.description} onChange={handleChange} />
                                    {errors.description && <p className="text-red-500 text-sm">{errors.description}</p>}
                                </div>
                                <div className="grid gap-2">
                                    <Label htmlFor="origin">Origin</Label>
                                    <Input id="origin" value={shipmentForm.origin} onChange={handleChange} />
                                    {errors.origin && <p className="text-red-500 text-sm">{errors.origin}</p>}
                                </div>
                                <div className="grid gap-2">
                                    <Label htmlFor="destination">Destination</Label>
                                    <Input id="destination" value={shipmentForm.destination} onChange={handleChange} />
                                    {errors.destination && <p className="text-red-500 text-sm">{errors.destination}</p>}
                                </div>
                                <div className="grid gap-2">
                                    <Label htmlFor="units">Units</Label>
                                    <Input id="units" value={shipmentForm.units} onChange={handleChange} />
                                    {errors.units && <p className="text-red-500 text-sm">{errors.units}</p>}
                                </div>
                                <div className="grid gap-2">
                                    <Label htmlFor="weight">Weight</Label>
                                    <Input id="weight" value={shipmentForm.weight} onChange={handleChange} />
                                    {errors.weight && <p className="text-red-500 text-sm">{errors.weight}</p>}
                                </div>
                                <div className="flex justify-between">
                                    <Button type="button" onClick={() => setShowForm(false)}>Cancel</Button>
                                    <Button type="submit">Create Shipment</Button>
                                </div>
                            </form>
                        </CardContent>
                    </Card>
                )}
            </div>
        </div>
    );
}
