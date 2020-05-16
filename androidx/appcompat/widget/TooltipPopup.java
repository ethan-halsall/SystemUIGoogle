// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.view.WindowManager;
import android.view.ViewGroup$LayoutParams;
import android.app.Activity;
import android.content.ContextWrapper;
import android.util.DisplayMetrics;
import android.content.res.Resources;
import android.view.View$MeasureSpec;
import android.util.Log;
import androidx.appcompat.R$dimen;
import androidx.appcompat.R$style;
import androidx.appcompat.R$id;
import android.view.ViewGroup;
import androidx.appcompat.R$layout;
import android.view.LayoutInflater;
import android.graphics.Rect;
import android.widget.TextView;
import android.view.WindowManager$LayoutParams;
import android.content.Context;
import android.view.View;

class TooltipPopup
{
    private final View mContentView;
    private final Context mContext;
    private final WindowManager$LayoutParams mLayoutParams;
    private final TextView mMessageView;
    private final int[] mTmpAnchorPos;
    private final int[] mTmpAppPos;
    private final Rect mTmpDisplayFrame;
    
    TooltipPopup(final Context mContext) {
        this.mLayoutParams = new WindowManager$LayoutParams();
        this.mTmpDisplayFrame = new Rect();
        this.mTmpAnchorPos = new int[2];
        this.mTmpAppPos = new int[2];
        this.mContext = mContext;
        final View inflate = LayoutInflater.from(mContext).inflate(R$layout.abc_tooltip, (ViewGroup)null);
        this.mContentView = inflate;
        this.mMessageView = (TextView)inflate.findViewById(R$id.message);
        this.mLayoutParams.setTitle((CharSequence)TooltipPopup.class.getSimpleName());
        this.mLayoutParams.packageName = this.mContext.getPackageName();
        final WindowManager$LayoutParams mLayoutParams = this.mLayoutParams;
        mLayoutParams.type = 1002;
        mLayoutParams.width = -2;
        mLayoutParams.height = -2;
        mLayoutParams.format = -3;
        mLayoutParams.windowAnimations = R$style.Animation_AppCompat_Tooltip;
        mLayoutParams.flags = 24;
    }
    
    private void computePosition(final View view, int n, int height, final boolean b, final WindowManager$LayoutParams windowManager$LayoutParams) {
        windowManager$LayoutParams.token = view.getApplicationWindowToken();
        final int dimensionPixelOffset = this.mContext.getResources().getDimensionPixelOffset(R$dimen.tooltip_precise_anchor_threshold);
        if (view.getWidth() < dimensionPixelOffset) {
            n = view.getWidth() / 2;
        }
        int n4;
        if (view.getHeight() >= dimensionPixelOffset) {
            final int dimensionPixelOffset2 = this.mContext.getResources().getDimensionPixelOffset(R$dimen.tooltip_precise_anchor_extra_offset);
            final int n2 = height + dimensionPixelOffset2;
            final int n3 = height - dimensionPixelOffset2;
            height = n2;
            n4 = n3;
        }
        else {
            height = view.getHeight();
            n4 = 0;
        }
        windowManager$LayoutParams.gravity = 49;
        final Resources resources = this.mContext.getResources();
        int n5;
        if (b) {
            n5 = R$dimen.tooltip_y_offset_touch;
        }
        else {
            n5 = R$dimen.tooltip_y_offset_non_touch;
        }
        final int dimensionPixelOffset3 = resources.getDimensionPixelOffset(n5);
        final View appRootView = getAppRootView(view);
        if (appRootView == null) {
            Log.e("TooltipPopup", "Cannot find app view");
            return;
        }
        appRootView.getWindowVisibleDisplayFrame(this.mTmpDisplayFrame);
        final Rect mTmpDisplayFrame = this.mTmpDisplayFrame;
        if (mTmpDisplayFrame.left < 0 && mTmpDisplayFrame.top < 0) {
            final Resources resources2 = this.mContext.getResources();
            final int identifier = resources2.getIdentifier("status_bar_height", "dimen", "android");
            int dimensionPixelSize;
            if (identifier != 0) {
                dimensionPixelSize = resources2.getDimensionPixelSize(identifier);
            }
            else {
                dimensionPixelSize = 0;
            }
            final DisplayMetrics displayMetrics = resources2.getDisplayMetrics();
            this.mTmpDisplayFrame.set(0, dimensionPixelSize, displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        appRootView.getLocationOnScreen(this.mTmpAppPos);
        view.getLocationOnScreen(this.mTmpAnchorPos);
        final int[] mTmpAnchorPos = this.mTmpAnchorPos;
        final int n6 = mTmpAnchorPos[0];
        final int[] mTmpAppPos = this.mTmpAppPos;
        mTmpAnchorPos[0] = n6 - mTmpAppPos[0];
        mTmpAnchorPos[1] -= mTmpAppPos[1];
        windowManager$LayoutParams.x = mTmpAnchorPos[0] + n - appRootView.getWidth() / 2;
        n = View$MeasureSpec.makeMeasureSpec(0, 0);
        this.mContentView.measure(n, n);
        n = this.mContentView.getMeasuredHeight();
        final int[] mTmpAnchorPos2 = this.mTmpAnchorPos;
        final int n7 = mTmpAnchorPos2[1] + n4 - dimensionPixelOffset3 - n;
        height = mTmpAnchorPos2[1] + height + dimensionPixelOffset3;
        if (b) {
            if (n7 >= 0) {
                windowManager$LayoutParams.y = n7;
            }
            else {
                windowManager$LayoutParams.y = height;
            }
        }
        else if (n + height <= this.mTmpDisplayFrame.height()) {
            windowManager$LayoutParams.y = height;
        }
        else {
            windowManager$LayoutParams.y = n7;
        }
    }
    
    private static View getAppRootView(final View view) {
        final View rootView = view.getRootView();
        final ViewGroup$LayoutParams layoutParams = rootView.getLayoutParams();
        if (layoutParams instanceof WindowManager$LayoutParams && ((WindowManager$LayoutParams)layoutParams).type == 2) {
            return rootView;
        }
        for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper)context).getBaseContext()) {
            if (context instanceof Activity) {
                return ((Activity)context).getWindow().getDecorView();
            }
        }
        return rootView;
    }
    
    void hide() {
        if (!this.isShowing()) {
            return;
        }
        ((WindowManager)this.mContext.getSystemService("window")).removeView(this.mContentView);
    }
    
    boolean isShowing() {
        return this.mContentView.getParent() != null;
    }
    
    void show(final View view, final int n, final int n2, final boolean b, final CharSequence text) {
        if (this.isShowing()) {
            this.hide();
        }
        this.mMessageView.setText(text);
        this.computePosition(view, n, n2, b, this.mLayoutParams);
        ((WindowManager)this.mContext.getSystemService("window")).addView(this.mContentView, (ViewGroup$LayoutParams)this.mLayoutParams);
    }
}
