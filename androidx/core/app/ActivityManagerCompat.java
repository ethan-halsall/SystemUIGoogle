// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.app;

import android.os.Build$VERSION;
import android.app.ActivityManager;

public final class ActivityManagerCompat
{
    public static boolean isLowRamDevice(final ActivityManager activityManager) {
        return Build$VERSION.SDK_INT >= 19 && activityManager.isLowRamDevice();
    }
}
