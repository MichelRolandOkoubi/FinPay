import { type ReactNode } from "react";
import { cn } from "@/lib/utils";

interface CardProps {
    children: ReactNode;
    className?: string;
}

export function Card({ children, className }: CardProps) {
    return (
        <div className={cn("glassmorphism rounded-2xl p-6 transition-all duration-300 hover:shadow-primary-500/10 hover:border-primary-500/30", className)}>
            {children}
        </div>
    );
}
