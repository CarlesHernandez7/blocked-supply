"use client"

import {useState} from "react"
import {Activity, Box, Home, RefreshCw} from "lucide-react"

import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs"

import NewProduct from "./new-product"
import Traceability from "./traceability"
import Transferences from "./transferences"

export default function Page() {
    const [activeTab, setActiveTab] = useState("home")

    return (
        <div className="flex min-h-screen flex-col space-y-6 p-4 md:p-8">
            <div className="flex items-center justify-between">
                <h1 className="text-2xl font-bold">Supply Chain Management</h1>
            </div>
            <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-4">
                <TabsList className="grid w-full grid-cols-2 md:grid-cols-4">
                    <TabsTrigger value="home" className="flex items-center gap-2">
                        <Home className="h-4 w-4"/>
                        Home
                    </TabsTrigger>
                    <TabsTrigger value="new-product" className="flex items-center gap-2">
                        <Box className="h-4 w-4"/>
                        New Product
                    </TabsTrigger>
                    <TabsTrigger value="traceability" className="flex items-center gap-2">
                        <Activity className="h-4 w-4"/>
                        Traceability
                    </TabsTrigger>
                    <TabsTrigger value="transferences" className="flex items-center gap-2">
                        <RefreshCw className="h-4 w-4"/>
                        Transferences
                    </TabsTrigger>
                </TabsList>
                <TabsContent value="home" className="space-y-4">
                    <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
                        <Card>
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">Total Products</CardTitle>
                                <Box className="h-4 w-4 text-muted-foreground"/>
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">1,234</div>
                                <p className="text-xs text-muted-foreground">+20.1% from last month</p>
                            </CardContent>
                        </Card>
                        <Card>
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">Active Transfers</CardTitle>
                                <RefreshCw className="h-4 w-4 text-muted-foreground"/>
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">45</div>
                                <p className="text-xs text-muted-foreground">Currently in transit</p>
                            </CardContent>
                        </Card>
                        <Card>
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">Completed Today</CardTitle>
                                <Activity className="h-4 w-4 text-muted-foreground"/>
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">127</div>
                                <p className="text-xs text-muted-foreground">Products processed today</p>
                            </CardContent>
                        </Card>
                        <Card>
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">Success Rate</CardTitle>
                                <Activity className="h-4 w-4 text-muted-foreground"/>
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">98.5%</div>
                                <p className="text-xs text-muted-foreground">On-time delivery rate</p>
                            </CardContent>
                        </Card>
                    </div>
                    <Card>
                        <CardHeader>
                            <CardTitle>About Supply Chain Management</CardTitle>
                            <CardDescription>Overview of our supply chain tracking system</CardDescription>
                        </CardHeader>
                        <CardContent className="space-y-4">
                            <p>
                                Welcome to our supply chain management system. This platform helps you track and manage
                                products
                                throughout their lifecycle in the supply chain. Here&apos;s what you can do:
                            </p>
                            <ul className="list-disc pl-4 space-y-2">
                                <li>Create new products and enter them into the supply chain</li>
                                <li>Track products in real-time as they move through different stages</li>
                                <li>Transfer products between different states and locations</li>
                                <li>Monitor key metrics and performance indicators</li>
                            </ul>
                        </CardContent>
                    </Card>
                </TabsContent>
                <TabsContent value="new-product">
                    <NewProduct/>
                </TabsContent>
                <TabsContent value="traceability">
                    <Traceability/>
                </TabsContent>
                <TabsContent value="transferences">
                    <Transferences/>
                </TabsContent>
            </Tabs>
        </div>
    )
}

