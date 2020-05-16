// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

class RtlSpacingHelper
{
    private int mEnd;
    private int mExplicitLeft;
    private int mExplicitRight;
    private boolean mIsRelative;
    private boolean mIsRtl;
    private int mLeft;
    private int mRight;
    private int mStart;
    
    RtlSpacingHelper() {
        this.mLeft = 0;
        this.mRight = 0;
        this.mStart = Integer.MIN_VALUE;
        this.mEnd = Integer.MIN_VALUE;
        this.mExplicitLeft = 0;
        this.mExplicitRight = 0;
        this.mIsRtl = false;
        this.mIsRelative = false;
    }
    
    public int getEnd() {
        int n;
        if (this.mIsRtl) {
            n = this.mLeft;
        }
        else {
            n = this.mRight;
        }
        return n;
    }
    
    public int getStart() {
        int n;
        if (this.mIsRtl) {
            n = this.mRight;
        }
        else {
            n = this.mLeft;
        }
        return n;
    }
    
    public void setAbsolute(final int n, final int n2) {
        this.mIsRelative = false;
        if (n != Integer.MIN_VALUE) {
            this.mExplicitLeft = n;
            this.mLeft = n;
        }
        if (n2 != Integer.MIN_VALUE) {
            this.mExplicitRight = n2;
            this.mRight = n2;
        }
    }
    
    public void setDirection(final boolean mIsRtl) {
        if (mIsRtl == this.mIsRtl) {
            return;
        }
        this.mIsRtl = mIsRtl;
        if (this.mIsRelative) {
            if (mIsRtl) {
                int mLeft = this.mEnd;
                if (mLeft == Integer.MIN_VALUE) {
                    mLeft = this.mExplicitLeft;
                }
                this.mLeft = mLeft;
                int mRight = this.mStart;
                if (mRight == Integer.MIN_VALUE) {
                    mRight = this.mExplicitRight;
                }
                this.mRight = mRight;
            }
            else {
                int mLeft2 = this.mStart;
                if (mLeft2 == Integer.MIN_VALUE) {
                    mLeft2 = this.mExplicitLeft;
                }
                this.mLeft = mLeft2;
                int mRight2 = this.mEnd;
                if (mRight2 == Integer.MIN_VALUE) {
                    mRight2 = this.mExplicitRight;
                }
                this.mRight = mRight2;
            }
        }
        else {
            this.mLeft = this.mExplicitLeft;
            this.mRight = this.mExplicitRight;
        }
    }
    
    public void setRelative(final int mLeft, final int mRight) {
        this.mStart = mLeft;
        this.mEnd = mRight;
        this.mIsRelative = true;
        if (this.mIsRtl) {
            if (mRight != Integer.MIN_VALUE) {
                this.mLeft = mRight;
            }
            if (mLeft != Integer.MIN_VALUE) {
                this.mRight = mLeft;
            }
        }
        else {
            if (mLeft != Integer.MIN_VALUE) {
                this.mLeft = mLeft;
            }
            if (mRight != Integer.MIN_VALUE) {
                this.mRight = mRight;
            }
        }
    }
}
