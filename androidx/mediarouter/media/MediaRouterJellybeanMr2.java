// 
// Decompiled by Procyon v0.5.36
// 

package androidx.mediarouter.media;

import android.media.MediaRouter$UserRouteInfo;
import android.media.MediaRouter$RouteInfo;
import android.media.MediaRouter$Callback;
import android.media.MediaRouter;

final class MediaRouterJellybeanMr2
{
    public static void addCallback(final Object o, final int n, final Object o2, final int n2) {
        ((MediaRouter)o).addCallback(n, (MediaRouter$Callback)o2, n2);
    }
    
    public static Object getDefaultRoute(final Object o) {
        return ((MediaRouter)o).getDefaultRoute();
    }
    
    public static final class RouteInfo
    {
        public static CharSequence getDescription(final Object o) {
            return ((MediaRouter$RouteInfo)o).getDescription();
        }
        
        public static boolean isConnecting(final Object o) {
            return ((MediaRouter$RouteInfo)o).isConnecting();
        }
    }
    
    public static final class UserRouteInfo
    {
        public static void setDescription(final Object o, final CharSequence description) {
            ((MediaRouter$UserRouteInfo)o).setDescription(description);
        }
    }
}
