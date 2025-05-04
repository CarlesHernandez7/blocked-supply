"use client";

import {useEffect, useState} from "react";
import {Button} from "@/components/ui/button";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import Link from "next/link";
import Loading from "@/components/loading";
import {ClipboardCopy} from "lucide-react";
import ProtectedRoute from "@/components/protectedroute";
import DatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'
import {format} from 'date-fns'
import api from "@/utils/baseApi";

interface ShipmentForm {
    productName: string;
    description: string;
    origin: string;
    destination: string;
    deliveryDate: string;
    units: string;
    weight: string;
}

interface ShipmentRecord {
    shipmentId: number;
    sku: string;
    createdAt: string;
    deliveryDate: string;
    state: string;
}

export default function ShipmentsPage() {
    const [shipments, setShipments] = useState<ShipmentRecord[] | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [showForm, setShowForm] = useState(false);
    const [shipmentForm, setShipmentForm] = useState<ShipmentForm>({
        productName: "",
        description: "",
        origin: "",
        destination: "",
        deliveryDate: "",
        units: "",
        weight: ""
    });
    const [errors, setErrors] = useState<Partial<ShipmentForm>>({});
    const [loading, setLoading] = useState(false);
    const [copiedId, setCopiedId] = useState<string | null>(null);

    useEffect(() => {
        const fetchShipments = async () => {
            setLoading(true);
            setError(null);
            const token = localStorage.getItem("authToken");
            if (token) {
                try {
                    const response = await fetch(`${api.baseURL}/api/records/participant`, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${token}`
                        }
                    });
                    if (!response.ok) {
                        setError("You are not a participant in any shipment.");
                        setShipments(null);
                        return;
                    }
                    const data: ShipmentRecord[] = await response.json();
                    console.log(data)
                    setShipments(data.length ? data : null);
                } catch (err) {
                    console.error(err);
                    setError("An error occurred while fetching shipments.");
                } finally {
                    setLoading(false);
                }
            }
        };
        fetchShipments();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const {id, value} = e.target;
        setShipmentForm(prev => ({...prev, [id]: value}));
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

        const token = localStorage.getItem("authToken");
        try {
            const response = await fetch(`${api.baseURL}/api/shipment/create`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({
                    ...shipmentForm,
                    units: Number(shipmentForm.units),
                    weight: Number(shipmentForm.weight)
                })
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                console.log("API Error:", errorMessage);
                setError(errorMessage);
                setLoading(false);
                return;
            }

            setShowForm(false);
            window.location.reload();
        } catch (err) {
            console.error(err);
            setError("An error occurred while creating the shipment.");
        } finally {
            setLoading(false);
        }
    };

    const handleCopy = async (id: string) => {
        try {
            await navigator.clipboard.writeText(id);
            setCopiedId(id);
            setTimeout(() => setCopiedId(null), 1500);
        } catch (err) {
            console.error("Failed to copy:", err);
        }
    };

    return (
        <ProtectedRoute>
            <div className="relative">
                {loading && <Loading/>} {}

                <div className={`${loading ? "opacity-50 pointer-events-none" : ""}`}>
                    {!showForm ? (
                        <Card>
                            <CardHeader>
                                <CardTitle>Your Shipments</CardTitle>
                                <CardDescription>List of shipments you are participant.</CardDescription>
                            </CardHeader>
                            <CardContent>
                                {error && <p className="text-red-500 pb-5">{error}</p>} {}

                                {shipments ? (
                                    <ul className="space-y-2">
                                        {shipments.map((shipment) => (
                                            <li
                                                key={shipment.shipmentId}
                                                className="border p-2 rounded transition-colors hover:bg-gray-200/50 flex justify-between items-center"
                                            >
                                                <Link href={`/shipments/${shipment.shipmentId}`}
                                                      className="block p-2 flex-grow">
                                                    <p><strong>SKU:</strong> {shipment.sku}</p>
                                                    <p><strong>Status:</strong> {shipment.state}</p>
                                                    <p><strong>Delivery Date:</strong> {shipment.deliveryDate}</p>
                                                    <p><strong>Created
                                                        At:</strong> {new Date(shipment.createdAt).toLocaleString()}</p>
                                                </Link>
                                                <Button
                                                    variant="outline"
                                                    size="sm"
                                                    onClick={() => handleCopy(shipment.sku)}
                                                    className="ml-2"
                                                >
                                                    <ClipboardCopy className="w-4 h-4 mr-1"/>
                                                    {copiedId === shipment.sku ? "Copied!" : "Copy SKU"}
                                                </Button>
                                            </li>
                                        ))}
                                    </ul>
                                ) : (
                                    <p className="text-gray-500">No shipments found.</p>
                                )}

                                <Button
                                    className="mt-4 w-full"
                                    onClick={() => {
                                        setShowForm(true);
                                        setError(null);
                                        setErrors({});
                                    }}
                                >
                                    Create Shipment
                                </Button>
                            </CardContent>
                        </Card>
                    ) : (
                        <Card>
                            <CardHeader>
                                <CardTitle>Create New Shipment</CardTitle>
                                <CardDescription>Fill in the details to create a shipment.</CardDescription>
                            </CardHeader>
                            <CardContent>
                                {error && <p className="text-red-500 pb-5">{error}</p>} {}
                                <form onSubmit={handleSubmit} className="space-y-4">
                                    <div className="grid gap-2">
                                        <Label htmlFor="productName">Shipment Product Name</Label>
                                        <Input id="productName" value={shipmentForm.productName} onChange={handleChange}
                                               placeholder="Enter the product name"/>
                                        {errors.productName &&
                                            <p className="text-red-500 text-sm">{errors.productName}</p>}
                                    </div>
                                    <div className="grid gap-2">
                                        <Label htmlFor="description">Description</Label>
                                        <Input id="description" value={shipmentForm.description} onChange={handleChange}
                                               placeholder="Enter the shipment description"/>
                                        {errors.description &&
                                            <p className="text-red-500 text-sm">{errors.description}</p>}
                                    </div>
                                    <div className="grid gap-2">
                                        <Label htmlFor="origin">Origin</Label>
                                        <Input id="origin" value={shipmentForm.origin} onChange={handleChange}
                                               placeholder="Enter the origin location"/>
                                        {errors.origin && <p className="text-red-500 text-sm">{errors.origin}</p>}
                                    </div>
                                    <div className="grid gap-2">
                                        <Label htmlFor="destination">Destination</Label>
                                        <Input id="destination" value={shipmentForm.destination} onChange={handleChange}
                                               placeholder="Enter the destination location"/>
                                        {errors.destination &&
                                            <p className="text-red-500 text-sm">{errors.destination}</p>}
                                    </div>
                                    <div className="grid gap-2">
                                        <Label htmlFor="deliveryDate">Delivery Date</Label>
                                        <DatePicker
                                            id="deliveryDate"
                                            selected={shipmentForm.deliveryDate ? new Date(shipmentForm.deliveryDate) : null}
                                            onChange={(date: Date | null) => {
                                                if (date) {
                                                    const formatted = format(date, "yyyy-MM-dd");
                                                    setShipmentForm(prev => ({...prev, deliveryDate: formatted}));
                                                }
                                            }}
                                            dateFormat="yyyy-MM-dd"
                                            placeholderText="Select delivery date"
                                            customInput={<Input/>}
                                            className="w-full"
                                            minDate={new Date(Date.now() + 24 * 60 * 60 * 1000)}
                                        />
                                        {errors.deliveryDate && (
                                            <p className="text-red-500 text-sm">{errors.deliveryDate}</p>
                                        )}
                                    </div>
                                    <div className="grid gap-2">
                                        <Label htmlFor="units">Units</Label>
                                        <Input id="units" value={shipmentForm.units} onChange={handleChange}
                                               placeholder="Enter the number of units"/>
                                        {errors.units && <p className="text-red-500 text-sm">{errors.units}</p>}
                                    </div>
                                    <div className="grid gap-2">
                                        <Label htmlFor="weight">Weight</Label>
                                        <Input id="weight" value={shipmentForm.weight} onChange={handleChange}
                                               placeholder="Enter the amount of weight in kg"/>
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
        </ProtectedRoute>
    );
}
