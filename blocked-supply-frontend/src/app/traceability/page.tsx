"use client";

import {useState} from "react";
import Loading from "@/components/loading";
import Image from "next/image";
import {Search} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {Input} from "@/components/ui/input";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import ProtectedRoute from "@/components/protectedroute";
import api from "@/utils/baseApi";


interface TransferOutput {
    id: number;
    shipmentId: number;
    timestamp: number;
    newState: string;
    location: string;
    newOwner: string;
    transferNotes: string;
}

export default function TraceabilityPage() {
    const [shipmentSku, setShipmentSku] = useState("");
    const [transfers, setTransfers] = useState<TransferOutput[] | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [noTransfers, setNoTransfers] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(false);

    const handleSearch = async () => {
        setTransfers(null);
        setError(null);
        setNoTransfers(false);
        setLoading(true);

        const sku = String(shipmentSku);
        if (sku === "" || sku === " " || sku.length < 3) {
            setError("Invalid SKU");
            setLoading(false);
            return;
        }

        const token = localStorage.getItem("authToken");

        try {
            const response = await fetch(`${api.baseURL}/api/transfer/${sku}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });
            if (!response.ok) {
                const errorMessage = await response.text();
                setError(errorMessage);
                setLoading(false);
                return;
            }

            let data: TransferOutput[] = await response.json();
            if (data.length === 0) {
                setNoTransfers(true);
            } else {
                data = data.map(transfer => ({
                    ...transfer,
                    transferNotes: transfer.transferNotes || "none"
                }));
                setTransfers(data);
            }
        } catch (err) {
            console.error(err);
            setError("An error occurred while retrieving the shipment.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <ProtectedRoute>
            <div className="relative">
                {loading && <Loading/>} {}

                <div className={`space-y-4 ${loading ? "opacity-50 pointer-events-none" : ""}`}>
                    <Card>
                        <CardHeader>
                            <CardTitle>Product Traceability</CardTitle>
                            <CardDescription>
                                Track the movement and status of products in the supply chain.
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <div className="flex space-x-2">
                                <Input
                                    placeholder="Search by Product SKU"
                                    value={shipmentSku}
                                    onChange={(e) => setShipmentSku(e.target.value)}
                                />
                                <Button onClick={handleSearch}>
                                    <Search className="h-4 w-4 mr-2"/>
                                    Search
                                </Button>
                            </div>
                        </CardContent>
                    </Card>

                    {error && <p className="text-red-500 mt-2">{error}</p>}

                    {!transfers && !noTransfers && !loading && (
                        <div className="flex justify-center pt-20">
                            <Image
                                src="/trace-product.png"
                                alt="Trace your product"
                                width={200}
                                height={50}
                                className="rounded-lg shadow-md"
                            />
                        </div>
                    )}

                    {noTransfers && (
                        <Card>
                            <CardHeader>
                                <CardTitle>Tracking Results</CardTitle>
                            </CardHeader>
                            <CardContent>
                                <p className="text-gray-500">No transfers found for this shipment yet.</p>
                            </CardContent>
                        </Card>
                    )}

                    {transfers && (
                        <Card>
                            <CardHeader>
                                <CardTitle>Tracking Results</CardTitle>
                            </CardHeader>
                            <CardContent>
                                <Table>
                                    <TableHeader>
                                        <TableRow>
                                            <TableHead>Timestamp</TableHead>
                                            <TableHead>Location</TableHead>
                                            <TableHead>Status</TableHead>
                                            <TableHead>Handler</TableHead>
                                            <TableHead>Notes</TableHead>
                                        </TableRow>
                                    </TableHeader>
                                    <TableBody>
                                        {transfers.map((transfer) => (
                                            <TableRow key={transfer.id}>
                                                <TableCell>{new Date(transfer.timestamp * 1000).toLocaleString()}</TableCell>
                                                <TableCell>{transfer.location}</TableCell>
                                                <TableCell>{transfer.newState}</TableCell>
                                                <TableCell>{transfer.newOwner}</TableCell>
                                                <TableCell>{transfer.transferNotes}</TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </CardContent>
                        </Card>
                    )}
                </div>
            </div>
        </ProtectedRoute>
    );
}
