import { useState } from "react";
import { Search } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

interface TransferOutput {
    id: number;
    shipmentId: number;
    timestamp: number;
    newState: string;
    location: string;
    newOwner: string;
    transferNotes: string;
}

export default function Traceability() {
    const [shipmentId, setShipmentId] = useState("");
    const [transfers, setTransfers] = useState<TransferOutput[] | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleSearch = async () => {
        setTransfers(null);
        setError(null);

        const idNumber = Number(shipmentId);
        if (isNaN(idNumber) || idNumber <= 0) {
            setError("Invalid Shipment ID: Must be greater than 0.");
            return;
        }

        try {
            const response = await fetch(`${API_URL}/transfer/${idNumber}`);
            console.log(response);
            if (!response.ok) {
                const errorMessage = await response.text();
                setError(errorMessage);
                return;
            }

            const data: TransferOutput[] = await response.json();
            setTransfers(data);
        } catch (err) {
            console.log(err);
            setError("An error occurred while retrieving the shipment.");
        }
    };

    return (
        <div className="space-y-4">
            <Card>
                <CardHeader>
                    <CardTitle>Product Traceability</CardTitle>
                    <CardDescription>Track the movement and status of products in the supply chain.</CardDescription>
                </CardHeader>
                <CardContent>
                    <div className="flex space-x-2">
                        <Input
                            placeholder="Search by Product ID"
                            value={shipmentId}
                            onChange={(e) => setShipmentId(e.target.value)}
                        />
                        <Button onClick={handleSearch}>
                            <Search className="h-4 w-4 mr-2" />
                            Search
                        </Button>
                    </div>
                    {error && <p className="text-red-500 mt-2">{error}</p>}
                </CardContent>
            </Card>

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
    );
}
