// Footer Component
// TODO: Implement full footer with:
// - Logo and tagline
// - Quick links
// - Contact info
// - Social links
// - Newsletter signup
// - Copyright

export function Footer() {
  return (
    <footer className="border-t bg-background">
      <div className="section-container py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Brand */}
          <div className="space-y-4">
            <span className="text-2xl font-bold gradient-text">TrimQ</span>
            <p className="text-sm text-muted-foreground">
              Skip the Wait. Own Your Style.
            </p>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="font-semibold mb-4">Quick Links</h4>
            <ul className="space-y-2 text-sm text-muted-foreground">
              <li><a href="/search" className="hover:text-foreground">Find Salons</a></li>
              <li><a href="/shop/register" className="hover:text-foreground">Register Your Shop</a></li>
              <li><a href="/about" className="hover:text-foreground">About Us</a></li>
            </ul>
          </div>

          {/* Support */}
          <div>
            <h4 className="font-semibold mb-4">Support</h4>
            <ul className="space-y-2 text-sm text-muted-foreground">
              <li><a href="/help" className="hover:text-foreground">Help Center</a></li>
              <li><a href="/contact" className="hover:text-foreground">Contact Us</a></li>
              <li><a href="/faq" className="hover:text-foreground">FAQs</a></li>
            </ul>
          </div>

          {/* Legal */}
          <div>
            <h4 className="font-semibold mb-4">Legal</h4>
            <ul className="space-y-2 text-sm text-muted-foreground">
              <li><a href="/privacy" className="hover:text-foreground">Privacy Policy</a></li>
              <li><a href="/terms" className="hover:text-foreground">Terms of Service</a></li>
              <li><a href="/refund" className="hover:text-foreground">Refund Policy</a></li>
            </ul>
          </div>
        </div>

        <div className="mt-12 pt-8 border-t text-center text-sm text-muted-foreground">
          <p>&copy; {new Date().getFullYear()} TrimQ. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;

