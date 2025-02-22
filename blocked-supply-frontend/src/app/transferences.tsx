import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Textarea } from "@/components/ui/textarea"

export default function Transferences() {
    return (
        <div className="space-y-4">
            <Card>
                <CardHeader>
                    <CardTitle>Transfer Product</CardTitle>
                    <CardDescription>Update the status or location of a product in the supply chain.</CardDescription>
                </CardHeader>
                <CardContent>
                    <form className="space-y-4">
                        <div className="grid gap-2">
                            <Label htmlFor="product-id">Product ID/SKU</Label>
                            <Input id="product-id" placeholder="Enter Product ID or SKU" />
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="current-status">Current Status</Label>
                            <Input id="current-status" disabled value="In Warehouse A" />
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="new-status">New Status</Label>
                            <Select>
                                <SelectTrigger>
                                    <SelectValue placeholder="Select new status" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="in-transit">In Transit</SelectItem>
                                    <SelectItem value="delivered">Delivered</SelectItem>
                                    <SelectItem value="inspection">Under Inspection</SelectItem>
                                    <SelectItem value="stored">Stored</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="new-location">New Location</Label>
                            <Select>
                                <SelectTrigger>
                                    <SelectValue placeholder="Select new location" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="warehouse-a">Warehouse A</SelectItem>
                                    <SelectItem value="warehouse-b">Warehouse B</SelectItem>
                                    <SelectItem value="distribution">Distribution Center</SelectItem>
                                    <SelectItem value="retail">Retail Location</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="notes">Transfer Notes</Label>
                            <Textarea id="notes" placeholder="Enter any additional notes about the transfer" />
                        </div>
                        <Button className="w-full">Update Product Status</Button>
                    </form>
                </CardContent>
            </Card>
        </div>
    )
}

