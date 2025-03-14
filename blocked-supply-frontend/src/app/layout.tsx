import type React from "react"
import "@/styles/globals.css"
import { Inter } from "next/font/google"

import { cn } from "@/lib/utils"
import Navigation from "@/components/navigation"

const inter = Inter({ subsets: ["latin"] })

export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="en" className="dark">
        <body className={cn(inter.className, "min-h-screen bg-background text-foreground antialiased")}>
        <div className="flex min-h-screen flex-col space-y-6 p-4 md:p-8">
            <div className="flex items-center justify-between">
                <h1 className="text-2xl font-bold">Supply Chain Management</h1>
            </div>
            <Navigation />
            <main>{children}</main>
        </div>
        </body>
        </html>
    )
}

