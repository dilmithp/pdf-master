import { clerkMiddleware, createRouteMatcher } from "@clerk/nextjs/server";
import { gdprMiddleware } from "@/lib/gdpr/middleware";

const isProtectedRoute = createRouteMatcher(["/dashboard(.*)", "/settings(.*)"]);

export default clerkMiddleware((auth, req) => {
  if (isProtectedRoute(req)) auth.protect();
  // Append GDPR data-region hint header after Clerk auth processing
  return gdprMiddleware(req);
});

export const config = {
  matcher: [
    "/((?!_next|[^?]*\\.(?:html?|css|js(?!on)|jpe?g|webp|png|gif|svg|ttf|woff2?|ico|csv|docx?|xlsx?|zip|webmanifest)).*)",
    "/(api|trpc)(.*)",
  ],
};
