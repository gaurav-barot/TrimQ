// Shop Owner Layout - Shared layout for shop owner pages

export default function ShopOwnerLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="min-h-screen bg-background">
      {/* TODO: Add Shop Owner Sidebar component */}
      <div className="flex">
        {/* Sidebar */}
        {/* <ShopOwnerSidebar /> */}
        
        {/* Main content */}
        <main className="flex-1">
          {/* TODO: Add Shop Owner Header component */}
          {/* <ShopOwnerHeader /> */}
          
          {children}
        </main>
      </div>
    </div>
  );
}

