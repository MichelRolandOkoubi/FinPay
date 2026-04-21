import { Sidebar } from "@/components/layout/Sidebar";
import { Header } from "@/components/layout/Header";

export default function DashboardLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <div className="flex h-screen overflow-hidden bg-surface">
            <Sidebar />
            <div className="flex-1 flex flex-col ml-64 overflow-hidden relative">
                <div className="absolute top-0 left-0 w-[500px] h-[500px] bg-primary-900/20 rounded-full blur-[120px] -translate-x-1/2 -translate-y-1/2 pointer-events-none" />
                <Header />
                <main className="flex-1 overflow-y-auto p-8 relative z-0">
                    {children}
                </main>
            </div>
        </div>
    );
}
