import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";

const inter = Inter({ subsets: ["latin"], variable: "--font-inter" });

export const metadata: Metadata = {
  title: "FinPay | Merchant Dashboard",
  description: "Advanced payment orchestration and analytics dashboard by FinPay.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="dark">
      <body className={`${inter.variable} font-sans min-h-screen bg-surface text-white selection:bg-primary-500/30`}>
        {children}
      </body>
    </html>
  );
}
