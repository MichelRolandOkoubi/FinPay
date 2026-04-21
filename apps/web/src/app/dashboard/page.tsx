import { Card } from "@/components/ui/Card";
import { Activity, ArrowUpRight, CheckCircle2, CreditCard, DollarSign } from "lucide-react";

export default function DashboardPage() {
    const stats = [
        { name: "Total Revenue", value: "€45,231.89", change: "+20.1%", icon: DollarSign, trend: "up" },
        { name: "Transactions", value: "2,304", change: "+15.0%", icon: CreditCard, trend: "up" },
        { name: "Success Rate", value: "98.5%", change: "+0.2%", icon: CheckCircle2, trend: "up" },
        { name: "Active Webhooks", value: "12/12", change: "100%", icon: Activity, trend: "neutral" },
    ];

    return (
        <div className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-500">
            <div className="flex justify-between items-end">
                <div>
                    <h2 className="text-3xl font-bold tracking-tight text-white mb-1">Dashboard</h2>
                    <p className="text-zinc-400">Welcome back! Here's what's happening with FinPay today.</p>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {stats.map((stat) => {
                    const Icon = stat.icon;
                    return (
                        <Card key={stat.name} className="relative overflow-hidden group">
                            <div className="absolute top-0 right-0 p-6 opacity-10 group-hover:opacity-20 transition-opacity">
                                <Icon className="w-16 h-16 transform translate-x-4 -translate-y-4 text-primary-400" />
                            </div>
                            <div className="flex flex-col h-full relative z-10">
                                <span className="text-zinc-400 font-medium text-sm mb-2">{stat.name}</span>
                                <span className="text-3xl font-bold text-white mb-2">{stat.value}</span>
                                <div className="flex items-center gap-1 mt-auto">
                                    {stat.trend === "up" && <ArrowUpRight className="w-4 h-4 text-primary-400" />}
                                    <span className={stat.trend === "up" ? "text-primary-400 font-medium text-sm" : "text-zinc-500 font-medium text-sm"}>
                                        {stat.change} <span className="text-zinc-500 font-normal">from last month</span>
                                    </span>
                                </div>
                            </div>
                        </Card>
                    );
                })}
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <Card className="col-span-2 min-h-[400px] flex flex-col">
                    <h3 className="text-lg font-semibold text-white mb-4">Recent Transactions</h3>
                    <div className="flex-1 flex items-center justify-center border border-dashed border-surface-border rounded-xl bg-surface/50">
                        <p className="text-zinc-500 text-sm">Transaction table will be loaded here via REST API</p>
                    </div>
                </Card>
                <Card className="col-span-1 min-h-[400px] flex flex-col">
                    <h3 className="text-lg font-semibold text-white mb-4">Payment Methods</h3>
                    <div className="flex-1 flex items-center justify-center border border-dashed border-surface-border rounded-xl bg-surface/50">
                        <p className="text-zinc-500 text-sm">Analytics chart</p>
                    </div>
                </Card>
            </div>
        </div>
    );
}
