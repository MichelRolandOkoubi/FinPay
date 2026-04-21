import { Bell, Search } from "lucide-react";

export function Header() {
    return (
        <header className="h-20 flex items-center justify-between px-8 bg-surface-hover/50 backdrop-blur-md border-b border-surface-border sticky top-0 z-10">
            <div className="flex items-center gap-4 text-zinc-400 w-96 flex-1">
                <Search className="w-5 h-5" />
                <input
                    type="text"
                    placeholder="Search transactions, customers, or transfers..."
                    className="bg-transparent border-none outline-none w-full text-sm placeholder:text-zinc-500 text-white focus:ring-0"
                />
            </div>

            <div className="flex items-center gap-6">
                <button className="relative text-zinc-400 hover:text-white transition-colors">
                    <Bell className="w-5 h-5" />
                    <span className="absolute top-0 right-0 w-2 h-2 bg-primary-500 rounded-full border-2 border-surface"></span>
                </button>
                <div className="flex items-center gap-3 border-l border-surface-border pl-6">
                    <div className="text-right flex flex-col justify-center">
                        <span className="text-sm font-semibold text-white leading-tight">Admin User</span>
                        <span className="text-xs text-zinc-500">Owner</span>
                    </div>
                    <div className="w-10 h-10 rounded-full bg-gradient-to-br from-primary-400 to-primary-600 shadow-lg flex items-center justify-center text-surface font-bold text-sm">
                        AU
                    </div>
                </div>
            </div>
        </header>
    );
}
