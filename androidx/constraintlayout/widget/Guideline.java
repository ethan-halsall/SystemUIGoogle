// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.widget;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

public class Guideline extends View
{
    public Guideline(final Context context) {
        super(context);
        super.setVisibility(8);
    }
    
    public Guideline(final Context context, final AttributeSet set) {
        super(context, set);
        super.setVisibility(8);
    }
    
    public void draw(final Canvas canvas) {
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(0, 0);
    }
    
    public void setVisibility(final int n) {
    }
}
