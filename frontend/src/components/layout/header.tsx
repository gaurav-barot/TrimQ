'use client';

// Header Component
// TODO: Implement full header with:
// - Logo
// - Navigation links
// - Search bar
// - User menu (avatar, dropdown)
// - Theme toggle
// - Mobile menu

export function Header() {
  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="section-container flex h-16 items-center justify-between">
        {/* Logo */}
        <div className="flex items-center gap-2">
          <span className="text-2xl font-bold gradient-text">TrimQ</span>
        </div>

        {/* Navigation - Desktop */}
        <nav className="hidden md:flex items-center gap-6">
          <a href="/search" className="text-muted-foreground hover:text-foreground transition-colors">
            Find Salons
          </a>
          <a href="/dashboard" className="text-muted-foreground hover:text-foreground transition-colors">
            My Bookings
          </a>
        </nav>

        {/* Actions */}
        <div className="flex items-center gap-4">
          {/* TODO: Add theme toggle */}
          {/* TODO: Add user menu */}
          <a href="/login" className="text-sm font-medium">
            Login
          </a>
        </div>
      </div>
    </header>
  );
}

export default Header;

