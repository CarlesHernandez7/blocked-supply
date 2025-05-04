"use client";

import type React from "react";
import "@/styles/globals.css";
import {Inter} from "next/font/google";
import {usePathname} from "next/navigation";
import Head from "next/head";

import {cn} from "@/lib/utils";
import Navigation from "@/components/navigation";
import {AuthProvider} from "@/context/AuthContext";
import {UserInfoModal} from "@/components/UserInfoModal";
import {NotificationInbox} from "@/components/NotificationInbox";

const inter = Inter({subsets: ["latin"]});

export default function RootLayout({children}: { children: React.ReactNode }) {
    const pathname = usePathname();
    const isAuthPage = pathname.startsWith("/auth");

    return (
        <html lang="en" className="dark">
        <Head>
            <title>Supply Chain Management</title>
            <meta name="Blocked Supply" content="Supply chain management with blockchain"/>
            <link rel="icon" href="/favicon.ico"/>
        </Head>
        <body className={cn(inter.className, "min-h-screen bg-background text-foreground antialiased")}>
        <AuthProvider>
            <div className="flex min-h-screen flex-col space-y-6 p-4 md:p-8">
                {!isAuthPage && (
                    <>
                        <div className="flex items-center justify-between">
                            <h1 className="text-2xl font-bold">Supply Chain Management</h1>
                            <div>
                                <NotificationInbox/>
                                <UserInfoModal/>
                            </div>
                        </div>
                        <Navigation/>
                    </>
                )}
                <main className="flex-1">{children}</main>
            </div>
        </AuthProvider>
        </body>
        </html>
    );
}
