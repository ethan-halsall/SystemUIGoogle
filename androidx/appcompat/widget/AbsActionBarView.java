// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewCompat;
import android.view.MotionEvent;
import android.content.res.TypedArray;
import androidx.appcompat.R$styleable;
import android.content.res.Configuration;
import android.view.View$MeasureSpec;
import android.view.View;
import android.view.ContextThemeWrapper;
import androidx.appcompat.R$attr;
import android.util.TypedValue;
import android.util.AttributeSet;
import androidx.core.view.ViewPropertyAnimatorCompat;
import android.content.Context;
import android.view.ViewGroup;

abstract class AbsActionBarView extends ViewGroup
{
    protected ActionMenuPresenter mActionMenuPresenter;
    protected int mContentHeight;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    protected ActionMenuView mMenuView;
    protected final Context mPopupContext;
    protected final VisibilityAnimListener mVisAnimListener;
    protected ViewPropertyAnimatorCompat mVisibilityAnim;
    
    AbsActionBarView(final Context context) {
        this(context, null);
    }
    
    AbsActionBarView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    AbsActionBarView(final Context mPopupContext, final AttributeSet set, final int n) {
        super(mPopupContext, set, n);
        this.mVisAnimListener = new VisibilityAnimListener();
        final TypedValue typedValue = new TypedValue();
        if (mPopupContext.getTheme().resolveAttribute(R$attr.actionBarPopupTheme, typedValue, true) && typedValue.resourceId != 0) {
            this.mPopupContext = (Context)new ContextThemeWrapper(mPopupContext, typedValue.resourceId);
        }
        else {
            this.mPopupContext = mPopupContext;
        }
    }
    
    static /* synthetic */ void access$001(final AbsActionBarView absActionBarView, final int visibility) {
        absActionBarView.setVisibility(visibility);
    }
    
    static /* synthetic */ void access$101(final AbsActionBarView absActionBarView, final int visibility) {
        absActionBarView.setVisibility(visibility);
    }
    
    protected static int next(int n, final int n2, final boolean b) {
        if (b) {
            n -= n2;
        }
        else {
            n += n2;
        }
        return n;
    }
    
    protected int measureChildView(final View view, final int n, final int n2, final int n3) {
        view.measure(View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE), n2);
        return Math.max(0, n - view.getMeasuredWidth() - n3);
    }
    
    protected void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes((AttributeSet)null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
        this.setContentHeight(obtainStyledAttributes.getLayoutDimension(R$styleable.ActionBar_height, 0));
        obtainStyledAttributes.recycle();
        final ActionMenuPresenter mActionMenuPresenter = this.mActionMenuPresenter;
        if (mActionMenuPresenter != null) {
            mActionMenuPresenter.onConfigurationChanged(configuration);
        }
    }
    
    public boolean onHoverEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            final boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            final boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }
    
    protected int positionChild(final View view, int n, int n2, final int n3, final boolean b) {
        final int measuredWidth = view.getMeasuredWidth();
        final int measuredHeight = view.getMeasuredHeight();
        n2 += (n3 - measuredHeight) / 2;
        if (b) {
            view.layout(n - measuredWidth, n2, n, measuredHeight + n2);
        }
        else {
            view.layout(n, n2, n + measuredWidth, measuredHeight + n2);
        }
        n = measuredWidth;
        if (b) {
            n = -measuredWidth;
        }
        return n;
    }
    
    public abstract void setContentHeight(final int p0);
    
    public void setVisibility(final int visibility) {
        if (visibility != this.getVisibility()) {
            final ViewPropertyAnimatorCompat mVisibilityAnim = this.mVisibilityAnim;
            if (mVisibilityAnim != null) {
                mVisibilityAnim.cancel();
            }
            super.setVisibility(visibility);
        }
    }
    
    public ViewPropertyAnimatorCompat setupAnimatorToVisibility(final int n, final long n2) {
        final ViewPropertyAnimatorCompat mVisibilityAnim = this.mVisibilityAnim;
        if (mVisibilityAnim != null) {
            mVisibilityAnim.cancel();
        }
        if (n == 0) {
            if (this.getVisibility() != 0) {
                this.setAlpha(0.0f);
            }
            final ViewPropertyAnimatorCompat animate = ViewCompat.animate((View)this);
            animate.alpha(1.0f);
            animate.setDuration(n2);
            final VisibilityAnimListener mVisAnimListener = this.mVisAnimListener;
            mVisAnimListener.withFinalVisibility(animate, n);
            animate.setListener(mVisAnimListener);
            return animate;
        }
        final ViewPropertyAnimatorCompat animate2 = ViewCompat.animate((View)this);
        animate2.alpha(0.0f);
        animate2.setDuration(n2);
        final VisibilityAnimListener mVisAnimListener2 = this.mVisAnimListener;
        mVisAnimListener2.withFinalVisibility(animate2, n);
        animate2.setListener(mVisAnimListener2);
        return animate2;
    }
    
    protected class VisibilityAnimListener implements ViewPropertyAnimatorListener
    {
        private boolean mCanceled;
        int mFinalVisibility;
        
        protected VisibilityAnimListener() {
            this.mCanceled = false;
        }
        
        @Override
        public void onAnimationCancel(final View view) {
            this.mCanceled = true;
        }
        
        @Override
        public void onAnimationEnd(final View view) {
            if (this.mCanceled) {
                return;
            }
            final AbsActionBarView this$0 = AbsActionBarView.this;
            this$0.mVisibilityAnim = null;
            AbsActionBarView.access$101(this$0, this.mFinalVisibility);
        }
        
        @Override
        public void onAnimationStart(final View view) {
            AbsActionBarView.access$001(AbsActionBarView.this, 0);
            this.mCanceled = false;
        }
        
        public VisibilityAnimListener withFinalVisibility(final ViewPropertyAnimatorCompat mVisibilityAnim, final int mFinalVisibility) {
            AbsActionBarView.this.mVisibilityAnim = mVisibilityAnim;
            this.mFinalVisibility = mFinalVisibility;
            return this;
        }
    }
}
