import {Activity, Box, RefreshCw} from "lucide-react"

import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"

export default function HomePage() {
    return (
        <div className="space-y-4">
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Total Products</CardTitle>
                        <Box className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">1,234</div>
                        <p className="text-xs text-muted-foreground">+20.1% from last month</p>
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Active Transfers</CardTitle>
                        <RefreshCw className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">45</div>
                        <p className="text-xs text-muted-foreground">Currently in transit</p>
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Completed Today</CardTitle>
                        <Activity className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">127</div>
                        <p className="text-xs text-muted-foreground">Products processed today</p>
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Success Rate</CardTitle>
                        <Activity className="h-4 w-4 text-muted-foreground" />
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
                        Welcome to our supply chain management system. This platform helps you track and manage products throughout
                        their lifecycle in the supply chain. Here&apos;s what you can do:
                    </p>
                    <ul className="list-disc pl-4 space-y-2">
                        <li>Create new products and enter them into the supply chain</li>
                        <li>Track products in real-time as they move through different stages</li>
                        <li>Transfer products between different states and locations</li>
                        <li>Monitor key metrics and performance indicators</li>
                    </ul>
                </CardContent>
            </Card>
        </div>
    )
}

