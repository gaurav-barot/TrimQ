import type { Metadata, Viewport } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';
import { Providers } from '@/components/providers';
import { Toaster } from '@/components/ui/sonner';

const inter = Inter({ 
  subsets: ['latin'],
  variable: '--font-sans',
});

export const metadata: Metadata = {
  title: {
    default: 'TrimQ - Skip the Wait. Own Your Style.',
    template: '%s | TrimQ',
  },
  description: 'Book your salon appointment in seconds. No more waiting in queues. Discover top-rated barber shops near you and get a digital pass.',
  keywords: [
    'salon booking',
    'barber shop',
    'haircut booking',
    'online appointment',
    'queue management',
    'digital pass',
    'India salon',
  ],
  authors: [{ name: 'TrimQ Team' }],
  creator: 'TrimQ',
  openGraph: {
    type: 'website',
    locale: 'en_IN',
    url: 'https://trimq.in',
    siteName: 'TrimQ',
    title: 'TrimQ - Skip the Wait. Own Your Style.',
    description: 'Book your salon appointment in seconds. No more waiting in queues.',
    images: [
      {
        url: '/og-image.png',
        width: 1200,
        height: 630,
        alt: 'TrimQ - Salon Booking Platform',
      },
    ],
  },
  twitter: {
    card: 'summary_large_image',
    title: 'TrimQ - Skip the Wait. Own Your Style.',
    description: 'Book your salon appointment in seconds. No more waiting in queues.',
    images: ['/og-image.png'],
    creator: '@trimq',
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      'max-video-preview': -1,
      'max-image-preview': 'large',
      'max-snippet': -1,
    },
  },
  manifest: '/manifest.json',
  icons: {
    icon: '/favicon.ico',
    shortcut: '/favicon-16x16.png',
    apple: '/apple-touch-icon.png',
  },
};

export const viewport: Viewport = {
  themeColor: [
    { media: '(prefers-color-scheme: light)', color: '#ffffff' },
    { media: '(prefers-color-scheme: dark)', color: '#0f172a' },
  ],
  width: 'device-width',
  initialScale: 1,
  maximumScale: 1,
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={`${inter.variable} font-sans antialiased`}>
        <Providers>
          {children}
          <Toaster richColors position="top-right" />
        </Providers>
      </body>
    </html>
  );
}

