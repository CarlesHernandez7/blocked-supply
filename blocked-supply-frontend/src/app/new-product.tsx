import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";

interface ProductForm {
    name: string;
    category: string;
    origin: string;
    destination: string;
    units: string;
    weight: string;
    description: string;
}

export default function NewProduct() {
    const [form, setForm] = useState<ProductForm>({
        name: "",
        category: "",
        origin: "",
        destination: "",
        units: "",
        weight: "",
        description: ""
    });
    const [errors, setErrors] = useState<Partial<ProductForm>>({});

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { id, value } = e.target;
        setForm(prev => ({ ...prev, [id]: value }));
    };

    const handleSelectChange = (value: string) => {
        setForm(prev => ({ ...prev, category: value }));
    };

    const validateForm = () => {
        const newErrors: Partial<ProductForm> = {};
        Object.keys(form).forEach(key => {
            if (!form[key as keyof ProductForm]) {
                newErrors[key as keyof ProductForm] = "This field is required";
            }
        });

        if (form.units && !/^[0-9]+$/.test(form.units)) {
            newErrors.units = "Units must be a valid number";
        }

        if (form.weight && !/^\d*\.?\d+$/.test(form.weight)) {
            newErrors.weight = "Weight must be a valid number";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (validateForm()) {
            alert("Product created successfully!");
            console.log("Form Data:", form);
        }
    };

    return (
        <Card>
            <CardHeader>
                <CardTitle>Create New Product</CardTitle>
                <CardDescription>Enter the details of the new product in order to start its supply chain.</CardDescription>
            </CardHeader>
            <CardContent>
                <form className="space-y-4" onSubmit={handleSubmit}>
                    <div className="grid gap-2">
                        <Label htmlFor="name">Product Name</Label>
                        <Input id="name" placeholder="Enter product name" value={form.name} onChange={handleChange} />
                        {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="category">Category</Label>
                        <Select onValueChange={handleSelectChange}>
                            <SelectTrigger>
                                <SelectValue placeholder="Select category" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="electronics">Electronics</SelectItem>
                                <SelectItem value="health">Health</SelectItem>
                                <SelectItem value="clothing">Clothing</SelectItem>
                                <SelectItem value="food">Food & Beverage</SelectItem>
                                <SelectItem value="other">Other</SelectItem>
                            </SelectContent>
                        </Select>
                        {errors.category && <p className="text-red-500 text-sm">{errors.category}</p>}
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="origin">Origin</Label>
                        <Input id="origin" placeholder="Enter the origin location of the product" value={form.origin} onChange={handleChange} />
                        {errors.origin && <p className="text-red-500 text-sm">{errors.origin}</p>}
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="destination">Destination</Label>
                        <Input id="destination" placeholder="Enter the destination location of the product" value={form.destination} onChange={handleChange} />
                        {errors.destination && <p className="text-red-500 text-sm">{errors.destination}</p>}
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="units">Units</Label>
                        <Input id="units" placeholder="Enter the total units of the product" value={form.units} onChange={handleChange} />
                        {errors.units && <p className="text-red-500 text-sm">{errors.units}</p>}
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="weight">Weight</Label>
                        <Input id="weight" placeholder="Enter the total weight of the product in kilograms" value={form.weight} onChange={handleChange} />
                        {errors.weight && <p className="text-red-500 text-sm">{errors.weight}</p>}
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="description">Description</Label>
                        <Textarea id="description" placeholder="Enter product description" value={form.description} onChange={handleChange} />
                        {errors.description && <p className="text-red-500 text-sm">{errors.description}</p>}
                    </div>
                    <Button className="w-full" type="submit">Create Product</Button>
                </form>
            </CardContent>
        </Card>
    );
}
