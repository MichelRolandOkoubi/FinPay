"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { LayoutDashboard, CreditCard, Activity, Settings, Zap } from "lucide-react";
import { cn } from "@/lib/utils";

const links = [
    { name: "Overview", href: "/dashboard", icon: LayoutDashboard },
    { name: "Payments", href: "/dashboard/payments", icon: CreditCard },
    { name: "Webhooks", href: "/dashboard/webhooks", icon: Activity },
    { name: "Settings", href: "/dashboard/settings", icon: Settings },
];

export function Sidebar() {
    const pathname = usePathname();

    return (
        <div className="w-64 border-r border-surface-border bg-surface-hover/30 flex flex-col h-full inset-y-0 fixed left-0">
            <div className="p-6 flex items-center gap-3">
                <div className="bg-gradient-to-tr from-primary-400 to-primary-600 p-2 rounded-xl">
                    <Zap className="w-6 h-6 text-surface" strokeWidth={2.5} />
                </div>
                <h1 className="text-xl font-bold tracking-tight text-white">FinPay</h1>
            </div>

            <nav className="flex-1 px-4 py-4 space-y-1">
                {links.map((link) => {
                    const Icon = link.icon;
                    const isActive = pathname === link.href;

                    return (
                        <Link
                            key={link.name}
                            href={link.href}
                            className={cn(
                                "group flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium transition-all duration-200",
                                isActive
                                    ? "bg-primary-500/10 text-primary-400"
                                    : "text-zinc-400 hover:bg-surface-border hover:text-white"
                            )}
                        >
                            <Icon
                                className={cn(
                                    "w-5 h-5 flex-shrink-0 transition-colors",
                                    isActive ? "text-primary-400" : "text-zinc-500 group-hover:text-zinc-300"
                                )}
                            />
                            {link.name}
                        </Link>
                    );
                })}
            </nav>

            <div className="p-4 border-t border-surface-border">
                <div className="flex items-center gap-3 px-3 py-2 rounded-xl bg-surface-border/50">
                    <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-zinc-700 to-zinc-600 border border-zinc-500 flex items-center justify-center text-xs font-bold">
                        M
                    </div>
                    <div className="flex flex-col">
                        <span className="text-sm font-medium text-white">My Merchant</span>
                        <span className="text-xs text-primary-400">Starter Plan</span>
                    </div>
                </div>
            </div>
        </div>
    );
}
