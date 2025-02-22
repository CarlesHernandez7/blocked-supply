import type React from "react"
import "@/styles/globals.css"
import { Inter } from "next/font/google"

import { cn } from "@/lib/utils"

const inter = Inter({ subsets: ["latin"] })

export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="en" className="dark">
        <body className={cn(inter.className, "min-h-screen bg-background text-foreground antialiased")}>{children}</body>
        </html>
    )
}

