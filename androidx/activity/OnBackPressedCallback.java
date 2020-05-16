// 
// Decompiled by Procyon v0.5.36
// 

package androidx.activity;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class OnBackPressedCallback
{
    private CopyOnWriteArrayList<Cancellable> mCancellables;
    private boolean mEnabled;
    
    public OnBackPressedCallback(final boolean mEnabled) {
        this.mCancellables = new CopyOnWriteArrayList<Cancellable>();
        this.mEnabled = mEnabled;
    }
    
    void addCancellable(final Cancellable e) {
        this.mCancellables.add(e);
    }
    
    public abstract void handleOnBackPressed();
    
    public final boolean isEnabled() {
        return this.mEnabled;
    }
    
    public final void remove() {
        final Iterator<Cancellable> iterator = this.mCancellables.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel();
        }
    }
    
    void removeCancellable(final Cancellable o) {
        this.mCancellables.remove(o);
    }
    
    public final void setEnabled(final boolean mEnabled) {
        this.mEnabled = mEnabled;
    }
}
