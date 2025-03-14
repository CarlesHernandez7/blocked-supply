import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select"
import {Textarea} from "@/components/ui/textarea"

export default function TransferencesPage() {
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
                            <Label htmlFor="product-id">Product ID</Label>
                            <Input id="product-id" placeholder="Enter Product ID"/>
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="current-status">Current Status</Label>
                            <Input id="current-status" disabled value="Created"/>
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="new-status">New Status</Label>
                            <Select>
                                <SelectTrigger>
                                    <SelectValue placeholder="Select new status"/>
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="created">Created</SelectItem>
                                    <SelectItem value="in-transit">In Transit</SelectItem>
                                    <SelectItem value="delivered">Delivered</SelectItem>
                                    <SelectItem value="stored">Stored</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="new-location">New Location</Label>
                            <Input id="new-location" placeholder="Enter the new location of the product" />
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="new-owner">New Owner</Label>
                            <Input id="new-owner" placeholder="Enter the new owner of the product" />
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="notes">Transfer Notes</Label>
                            <Textarea id="notes" placeholder="Enter any additional notes about the transfer"/>
                        </div>
                        <Button className="w-full">Update Product Status</Button>
                    </form>
                </CardContent>
            </Card>
        </div>
    )
}

