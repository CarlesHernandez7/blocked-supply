"use client";

import {useState} from "react";
import {useParams, useRouter} from "next/navigation";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import Loading from "@/components/loading";
import ProtectedRoute from "@/components/protectedroute";
import api from "@/utils/baseApi";


const stateMap: Record<string, number> = {
    "created": 0,
    "in-transit": 1,
    "stored": 2,
    "delivered": 3
};

interface TransferForm {
    shipmentId: number;
    newShipmentOwner: string;
    newState: number;
    location: string;
    transferNotes: string;
}

interface TransferFormErrors {
    shipmentId?: string;
    newShipmentOwner?: string;
    newState?: string;
    location?: string;
    transferNotes?: string;
}

export default function TransferShipmentPage() {
    const {id} = useParams();
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [transferData, setTransferData] = useState<TransferForm>({
        shipmentId: Number(id),
        newShipmentOwner: "",
        newState: stateMap["created"],
        location: "",
        transferNotes: ""
    });
    const [errors, setErrors] = useState<TransferFormErrors>({});

    const handleChange = (field: keyof TransferForm, value: string) => {
        setTransferData(prev => ({
            ...prev,
            [field]: field === "newState" ? stateMap[value] : value
        }));

        setError(null);
    };

    const validateForm = () => {
        const newErrors: TransferFormErrors = {};

        Object.entries(transferData).forEach(([key, value]) => {
            if (key === "transferNotes") {
                return;
            }

            if (typeof value === "string" && !value.trim()) {
                newErrors[key as keyof TransferFormErrors] = "This field is required";
            } else if (key === "newState" && (value === null || isNaN(value))) {
                newErrors.newState = "This field is required";
            }
        });

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
            console.log(transferData)
            const response = await fetch(`${api.baseURL}/api/transfer/create`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(transferData)
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                setError(errorMessage);
                setLoading(false);
                return;
            }

            router.push("/shipments");
        } catch (err) {
            console.error(err);
            setError("Failed to update shipment.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <ProtectedRoute>
            <div className="relative">
                {loading && (
                    <div>
                        <Loading/>
                    </div>
                )}
                <Card className={`${loading ? "opacity-50 pointer-events-none" : ""}`}>
                    <CardHeader>
                        <CardTitle>Transfer Shipment #{id}</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <form className="space-y-4" onSubmit={handleSubmit}>
                            <div className="grid gap-2">
                                <Label>New State</Label>
                                <Select
                                    onValueChange={(value) => handleChange("newState", value)}
                                    value={Object.keys(stateMap).find(key => stateMap[key] === transferData.newState)}
                                    disabled={loading}
                                >
                                    <SelectTrigger>
                                        <SelectValue placeholder="Select new state"/>
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="created">Created</SelectItem>
                                        <SelectItem value="in-transit">In Transit</SelectItem>
                                        <SelectItem value="stored">Stored</SelectItem>
                                        <SelectItem value="delivered">Delivered</SelectItem>
                                    </SelectContent>
                                </Select>
                                {errors.newState && <p className="text-red-500 text-sm">{errors.newState}</p>}
                            </div>
                            <div className="grid gap-2">
                                <Label>New Owner</Label>
                                <Input
                                    value={transferData.newShipmentOwner}
                                    onChange={(e) => handleChange("newShipmentOwner", e.target.value)}
                                    placeholder="Enter the new owner mail"
                                    disabled={loading}
                                />
                                {errors.newShipmentOwner &&
                                    <p className="text-red-500 text-sm">{errors.newShipmentOwner}</p>}
                            </div>
                            <div className="grid gap-2">
                                <Label>Location</Label>
                                <Input
                                    value={transferData.location}
                                    onChange={(e) => handleChange("location", e.target.value)}
                                    placeholder="Enter new location"
                                    disabled={loading}
                                />
                                {errors.location && <p className="text-red-500 text-sm">{errors.location}</p>}
                            </div>
                            <div className="grid gap-2">
                                <Label>Transfer Notes</Label>
                                <Input
                                    value={transferData.transferNotes}
                                    onChange={(e) => handleChange("transferNotes", e.target.value)}
                                    placeholder="Enter additional notes"
                                    disabled={loading}
                                />
                                {errors.transferNotes && <p className="text-red-500 text-sm">{errors.transferNotes}</p>}
                            </div>
                            {error && <p className="text-red-500">{error}</p>}
                            <div className="flex justify-between">
                                <Button
                                    type="button"
                                    variant="outline"
                                    onClick={() => router.back()}
                                    disabled={loading}
                                >
                                    Cancel
                                </Button>
                                <Button type="submit" disabled={loading}>
                                    {loading ? "Saving..." : "Save"}
                                </Button>
                            </div>
                        </form>
                    </CardContent>
                </Card>
            </div>
        </ProtectedRoute>
    );
}
