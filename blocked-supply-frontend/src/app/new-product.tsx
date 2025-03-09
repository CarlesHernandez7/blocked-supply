import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select"
import {Textarea} from "@/components/ui/textarea"

export default function NewProduct() {
    return (
        <Card>
            <CardHeader>
                <CardTitle>Create New Product</CardTitle>
                <CardDescription>Enter the details of the new product to add it to the supply chain.</CardDescription>
            </CardHeader>
            <CardContent>
                <form className="space-y-4">
                    <div className="grid gap-2">
                        <Label htmlFor="name">Product Name</Label>
                        <Input id="name" placeholder="Enter product name"/>
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="sku">SKU</Label>
                        <Input id="sku" placeholder="Enter product SKU"/>
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="category">Category</Label>
                        <Select>
                            <SelectTrigger>
                                <SelectValue placeholder="Select category"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="electronics">Electronics</SelectItem>
                                <SelectItem value="clothing">Clothing</SelectItem>
                                <SelectItem value="food">Food & Beverage</SelectItem>
                                <SelectItem value="other">Other</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="initial-location">Initial Location</Label>
                        <Select>
                            <SelectTrigger>
                                <SelectValue placeholder="Select location"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="warehouse-a">Warehouse A</SelectItem>
                                <SelectItem value="warehouse-b">Warehouse B</SelectItem>
                                <SelectItem value="distribution-center">Distribution Center</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="description">Description</Label>
                        <Textarea id="description" placeholder="Enter product description"/>
                    </div>
                    <Button className="w-full">Create Product</Button>
                </form>
            </CardContent>
        </Card>
    )
}

