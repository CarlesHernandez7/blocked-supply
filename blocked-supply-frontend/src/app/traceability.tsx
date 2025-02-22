import { Search } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

export default function Traceability() {
    return (
        <div className="space-y-4">
            <Card>
                <CardHeader>
                    <CardTitle>Product Traceability</CardTitle>
                    <CardDescription>Track the movement and status of products in the supply chain.</CardDescription>
                </CardHeader>
                <CardContent>
                    <div className="flex space-x-2">
                        <Input placeholder="Search by Product ID or SKU" />
                        <Button>
                            <Search className="h-4 w-4 mr-2" />
                            Search
                        </Button>
                    </div>
                </CardContent>
            </Card>
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
                            <TableRow>
                                <TableCell>2024-02-22 10:30 AM</TableCell>
                                <TableCell>Warehouse A</TableCell>
                                <TableCell>Received</TableCell>
                                <TableCell>John Doe</TableCell>
                                <TableCell>Initial receipt of product</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>2024-02-22 02:15 PM</TableCell>
                                <TableCell>Quality Control</TableCell>
                                <TableCell>Inspected</TableCell>
                                <TableCell>Jane Smith</TableCell>
                                <TableCell>Passed quality inspection</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>2024-02-22 04:45 PM</TableCell>
                                <TableCell>Distribution Center</TableCell>
                                <TableCell>In Transit</TableCell>
                                <TableCell>Mike Johnson</TableCell>
                                <TableCell>Preparing for shipment</TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </CardContent>
            </Card>
        </div>
    )
}

