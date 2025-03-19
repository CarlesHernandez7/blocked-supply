"use client"

import Link from "next/link"
import {usePathname} from "next/navigation"
import {Activity, Box, Home, RefreshCw} from "lucide-react"

import {cn} from "@/lib/utils"

const navItems = [
    {
        name: "Home",
        href: "/",
        icon: Home,
    },
    {
        name: "Shipments",
        href: "/shipments",
        icon: Box,
    },
    {
        name: "Traceability",
        href: "/traceability",
        icon: Activity,
    },
    {
        name: "Transferences",
        href: "/transferences",
        icon: RefreshCw,
    },
]

export default function Navigation() {
    const pathname = usePathname()

    return (
        <nav className="flex overflow-auto">
            <div
                className="inline-flex h-10 items-center justify-center rounded-md bg-muted p-1 text-muted-foreground w-full">
                {navItems.map((item) => {
                    const isActive = pathname === item.href
                    const Icon = item.icon

                    return (
                        <Link
                            key={item.href}
                            href={item.href}
                            className={cn(
                                "inline-flex items-center justify-center whitespace-nowrap rounded-sm px-3 py-1.5 text-sm font-medium ring-offset-background transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 flex-1",
                                isActive ? "bg-background text-foreground shadow-sm" : "hover:bg-background/50 hover:text-foreground",
                            )}
                        >
                            <Icon className="h-4 w-4 mr-2"/>
                            {item.name}
                        </Link>
                    )
                })}
            </div>
        </nav>
    )
}

