// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import androidx.appcompat.R$styleable;
import android.util.AttributeSet;
import android.os.Build$VERSION;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import androidx.core.view.ViewCompat;
import android.graphics.drawable.Drawable;
import android.view.View;

class AppCompatBackgroundHelper
{
    private int mBackgroundResId;
    private TintInfo mBackgroundTint;
    private final AppCompatDrawableManager mDrawableManager;
    private TintInfo mInternalBackgroundTint;
    private TintInfo mTmpInfo;
    private final View mView;
    
    AppCompatBackgroundHelper(final View mView) {
        this.mBackgroundResId = -1;
        this.mView = mView;
        this.mDrawableManager = AppCompatDrawableManager.get();
    }
    
    private boolean applyFrameworkTintUsingColorFilter(final Drawable drawable) {
        if (this.mTmpInfo == null) {
            this.mTmpInfo = new TintInfo();
        }
        final TintInfo mTmpInfo = this.mTmpInfo;
        mTmpInfo.clear();
        final ColorStateList backgroundTintList = ViewCompat.getBackgroundTintList(this.mView);
        if (backgroundTintList != null) {
            mTmpInfo.mHasTintList = true;
            mTmpInfo.mTintList = backgroundTintList;
        }
        final PorterDuff$Mode backgroundTintMode = ViewCompat.getBackgroundTintMode(this.mView);
        if (backgroundTintMode != null) {
            mTmpInfo.mHasTintMode = true;
            mTmpInfo.mTintMode = backgroundTintMode;
        }
        if (!mTmpInfo.mHasTintList && !mTmpInfo.mHasTintMode) {
            return false;
        }
        AppCompatDrawableManager.tintDrawable(drawable, mTmpInfo, this.mView.getDrawableState());
        return true;
    }
    
    private boolean shouldApplyFrameworkTintUsingColorFilter() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = true;
        if (sdk_INT > 21) {
            if (this.mInternalBackgroundTint == null) {
                b = false;
            }
            return b;
        }
        return sdk_INT == 21;
    }
    
    void applySupportBackgroundTint() {
        final Drawable background = this.mView.getBackground();
        if (background != null) {
            if (this.shouldApplyFrameworkTintUsingColorFilter() && this.applyFrameworkTintUsingColorFilter(background)) {
                return;
            }
            final TintInfo mBackgroundTint = this.mBackgroundTint;
            if (mBackgroundTint != null) {
                AppCompatDrawableManager.tintDrawable(background, mBackgroundTint, this.mView.getDrawableState());
            }
            else {
                final TintInfo mInternalBackgroundTint = this.mInternalBackgroundTint;
                if (mInternalBackgroundTint != null) {
                    AppCompatDrawableManager.tintDrawable(background, mInternalBackgroundTint, this.mView.getDrawableState());
                }
            }
        }
    }
    
    ColorStateList getSupportBackgroundTintList() {
        final TintInfo mBackgroundTint = this.mBackgroundTint;
        ColorStateList mTintList;
        if (mBackgroundTint != null) {
            mTintList = mBackgroundTint.mTintList;
        }
        else {
            mTintList = null;
        }
        return mTintList;
    }
    
    PorterDuff$Mode getSupportBackgroundTintMode() {
        final TintInfo mBackgroundTint = this.mBackgroundTint;
        PorterDuff$Mode mTintMode;
        if (mBackgroundTint != null) {
            mTintMode = mBackgroundTint.mTintMode;
        }
        else {
            mTintMode = null;
        }
        return mTintMode;
    }
    
    void loadFromAttributes(final AttributeSet set, final int n) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), set, R$styleable.ViewBackgroundHelper, n, 0);
        final View mView = this.mView;
        ViewCompat.saveAttributeDataForStyleable(mView, mView.getContext(), R$styleable.ViewBackgroundHelper, set, obtainStyledAttributes.getWrappedTypeArray(), n, 0);
        try {
            if (obtainStyledAttributes.hasValue(R$styleable.ViewBackgroundHelper_android_background)) {
                this.mBackgroundResId = obtainStyledAttributes.getResourceId(R$styleable.ViewBackgroundHelper_android_background, -1);
                final ColorStateList tintList = this.mDrawableManager.getTintList(this.mView.getContext(), this.mBackgroundResId);
                if (tintList != null) {
                    this.setInternalBackgroundTint(tintList);
                }
            }
            if (obtainStyledAttributes.hasValue(R$styleable.ViewBackgroundHelper_backgroundTint)) {
                ViewCompat.setBackgroundTintList(this.mView, obtainStyledAttributes.getColorStateList(R$styleable.ViewBackgroundHelper_backgroundTint));
            }
            if (obtainStyledAttributes.hasValue(R$styleable.ViewBackgroundHelper_backgroundTintMode)) {
                ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R$styleable.ViewBackgroundHelper_backgroundTintMode, -1), null));
            }
        }
        finally {
            obtainStyledAttributes.recycle();
        }
    }
    
    void onSetBackgroundDrawable(final Drawable drawable) {
        this.mBackgroundResId = -1;
        this.setInternalBackgroundTint(null);
        this.applySupportBackgroundTint();
    }
    
    void onSetBackgroundResource(final int mBackgroundResId) {
        this.mBackgroundResId = mBackgroundResId;
        final AppCompatDrawableManager mDrawableManager = this.mDrawableManager;
        ColorStateList tintList;
        if (mDrawableManager != null) {
            tintList = mDrawableManager.getTintList(this.mView.getContext(), mBackgroundResId);
        }
        else {
            tintList = null;
        }
        this.setInternalBackgroundTint(tintList);
        this.applySupportBackgroundTint();
    }
    
    void setInternalBackgroundTint(final ColorStateList mTintList) {
        if (mTintList != null) {
            if (this.mInternalBackgroundTint == null) {
                this.mInternalBackgroundTint = new TintInfo();
            }
            final TintInfo mInternalBackgroundTint = this.mInternalBackgroundTint;
            mInternalBackgroundTint.mTintList = mTintList;
            mInternalBackgroundTint.mHasTintList = true;
        }
        else {
            this.mInternalBackgroundTint = null;
        }
        this.applySupportBackgroundTint();
    }
    
    void setSupportBackgroundTintList(final ColorStateList mTintList) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        final TintInfo mBackgroundTint = this.mBackgroundTint;
        mBackgroundTint.mTintList = mTintList;
        mBackgroundTint.mHasTintList = true;
        this.applySupportBackgroundTint();
    }
    
    void setSupportBackgroundTintMode(final PorterDuff$Mode mTintMode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        final TintInfo mBackgroundTint = this.mBackgroundTint;
        mBackgroundTint.mTintMode = mTintMode;
        mBackgroundTint.mHasTintMode = true;
        this.applySupportBackgroundTint();
    }
}
