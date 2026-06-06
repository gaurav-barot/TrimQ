// User Layout - Shared layout for user pages

export default function UserLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="min-h-screen bg-background">
      {/* TODO: Add Header component */}
      {/* <Header /> */}
      
      <main>{children}</main>
      
      {/* TODO: Add Footer component */}
      {/* <Footer /> */}
    </div>
  );
}

