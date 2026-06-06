'use client';

// Shop Owner Sidebar Component
// TODO: Implement full sidebar with:
// - Shop logo/name
// - Navigation items with icons
// - Active state highlighting
// - Collapse toggle
// - Mobile responsive

import { usePathname } from 'next/navigation';
import Link from 'next/link';

const navItems = [
  { href: '/shop/dashboard', label: 'Dashboard', icon: '📊' },
  { href: '/shop/today', label: "Today's Queue", icon: '📋' },
  { href: '/shop/bookings', label: 'All Bookings', icon: '📅' },
  { href: '/shop/services', label: 'Services', icon: '✂️' },
  { href: '/shop/staff', label: 'Staff', icon: '👥' },
  { href: '/shop/validate', label: 'Validate Pass', icon: '🎫' },
  { href: '/shop/earnings', label: 'Earnings', icon: '💰' },
  { href: '/shop/settings', label: 'Settings', icon: '⚙️' },
];

export function ShopOwnerSidebar() {
  const pathname = usePathname();

  return (
    <aside className="w-64 min-h-screen border-r bg-card hidden lg:block">
      <div className="p-6">
        {/* Logo */}
        <div className="mb-8">
          <span className="text-2xl font-bold gradient-text">TrimQ</span>
          <p className="text-xs text-muted-foreground mt-1">Shop Owner Portal</p>
        </div>

        {/* Navigation */}
        <nav className="space-y-2">
          {navItems.map((item) => (
            <Link
              key={item.href}
              href={item.href}
              className={`flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                pathname === item.href
                  ? 'bg-primary text-primary-foreground'
                  : 'text-muted-foreground hover:bg-muted hover:text-foreground'
              }`}
            >
              <span>{item.icon}</span>
              <span className="text-sm font-medium">{item.label}</span>
            </Link>
          ))}
        </nav>
      </div>
    </aside>
  );
}

export default ShopOwnerSidebar;

