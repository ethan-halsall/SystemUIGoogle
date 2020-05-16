// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeProvider;
import android.os.Build$VERSION;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.accessibility.AccessibilityEvent;
import java.lang.ref.WeakReference;
import android.util.SparseArray;
import android.text.style.ClickableSpan;
import java.util.Collections;
import androidx.core.R$id;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.List;
import android.view.View;
import android.view.View$AccessibilityDelegate;

public class AccessibilityDelegateCompat
{
    private static final View$AccessibilityDelegate DEFAULT_DELEGATE;
    private final View$AccessibilityDelegate mBridge;
    private final View$AccessibilityDelegate mOriginalDelegate;
    
    static {
        DEFAULT_DELEGATE = new View$AccessibilityDelegate();
    }
    
    public AccessibilityDelegateCompat() {
        this(AccessibilityDelegateCompat.DEFAULT_DELEGATE);
    }
    
    public AccessibilityDelegateCompat(final View$AccessibilityDelegate mOriginalDelegate) {
        this.mOriginalDelegate = mOriginalDelegate;
        this.mBridge = new AccessibilityDelegateAdapter(this);
    }
    
    static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(final View view) {
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> emptyList;
        if ((emptyList = (List<AccessibilityNodeInfoCompat.AccessibilityActionCompat>)view.getTag(R$id.tag_accessibility_actions)) == null) {
            emptyList = Collections.emptyList();
        }
        return emptyList;
    }
    
    private boolean isSpanStillValid(final ClickableSpan clickableSpan, final View view) {
        if (clickableSpan != null) {
            final ClickableSpan[] clickableSpans = AccessibilityNodeInfoCompat.getClickableSpans(view.createAccessibilityNodeInfo().getText());
            for (int n = 0; clickableSpans != null && n < clickableSpans.length; ++n) {
                if (clickableSpan.equals(clickableSpans[n])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean performClickableSpanAction(final int n, final View view) {
        final SparseArray sparseArray = (SparseArray)view.getTag(R$id.tag_accessibility_clickable_spans);
        if (sparseArray != null) {
            final WeakReference weakReference = (WeakReference)sparseArray.get(n);
            if (weakReference != null) {
                final ClickableSpan clickableSpan = weakReference.get();
                if (this.isSpanStillValid(clickableSpan, view)) {
                    clickableSpan.onClick(view);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        return this.mOriginalDelegate.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }
    
    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            final AccessibilityNodeProvider accessibilityNodeProvider = this.mOriginalDelegate.getAccessibilityNodeProvider(view);
            if (accessibilityNodeProvider != null) {
                return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
            }
        }
        return null;
    }
    
    View$AccessibilityDelegate getBridge() {
        return this.mBridge;
    }
    
    public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        this.mOriginalDelegate.onInitializeAccessibilityEvent(view, accessibilityEvent);
    }
    
    public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        this.mOriginalDelegate.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat.unwrap());
    }
    
    public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        this.mOriginalDelegate.onPopulateAccessibilityEvent(view, accessibilityEvent);
    }
    
    public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
        return this.mOriginalDelegate.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }
    
    public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
        final List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = getActionList(view);
        final int n2 = 0;
        int n3 = 0;
        int perform;
        while (true) {
            perform = n2;
            if (n3 >= actionList.size()) {
                break;
            }
            final AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat = actionList.get(n3);
            if (accessibilityActionCompat.getId() == n) {
                perform = (accessibilityActionCompat.perform(view, bundle) ? 1 : 0);
                break;
            }
            ++n3;
        }
        int performAccessibilityAction = perform;
        if (perform == 0) {
            performAccessibilityAction = perform;
            if (Build$VERSION.SDK_INT >= 16) {
                performAccessibilityAction = (this.mOriginalDelegate.performAccessibilityAction(view, n, bundle) ? 1 : 0);
            }
        }
        int performClickableSpanAction;
        if ((performClickableSpanAction = performAccessibilityAction) == 0) {
            performClickableSpanAction = performAccessibilityAction;
            if (n == R$id.accessibility_action_clickable_span) {
                performClickableSpanAction = (this.performClickableSpanAction(bundle.getInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", -1), view) ? 1 : 0);
            }
        }
        return performClickableSpanAction != 0;
    }
    
    public void sendAccessibilityEvent(final View view, final int n) {
        this.mOriginalDelegate.sendAccessibilityEvent(view, n);
    }
    
    public void sendAccessibilityEventUnchecked(final View view, final AccessibilityEvent accessibilityEvent) {
        this.mOriginalDelegate.sendAccessibilityEventUnchecked(view, accessibilityEvent);
    }
    
    static final class AccessibilityDelegateAdapter extends View$AccessibilityDelegate
    {
        final AccessibilityDelegateCompat mCompat;
        
        AccessibilityDelegateAdapter(final AccessibilityDelegateCompat mCompat) {
            this.mCompat = mCompat;
        }
        
        public boolean dispatchPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            return this.mCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }
        
        public AccessibilityNodeProvider getAccessibilityNodeProvider(final View view) {
            final AccessibilityNodeProviderCompat accessibilityNodeProvider = this.mCompat.getAccessibilityNodeProvider(view);
            AccessibilityNodeProvider accessibilityNodeProvider2;
            if (accessibilityNodeProvider != null) {
                accessibilityNodeProvider2 = (AccessibilityNodeProvider)accessibilityNodeProvider.getProvider();
            }
            else {
                accessibilityNodeProvider2 = null;
            }
            return accessibilityNodeProvider2;
        }
        
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            this.mCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
        }
        
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfo accessibilityNodeInfo) {
            final AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo);
            wrap.setScreenReaderFocusable(ViewCompat.isScreenReaderFocusable(view));
            wrap.setHeading(ViewCompat.isAccessibilityHeading(view));
            wrap.setPaneTitle(ViewCompat.getAccessibilityPaneTitle(view));
            this.mCompat.onInitializeAccessibilityNodeInfo(view, wrap);
            wrap.addSpansToExtras(accessibilityNodeInfo.getText(), view);
            final List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = AccessibilityDelegateCompat.getActionList(view);
            for (int i = 0; i < actionList.size(); ++i) {
                wrap.addAction((AccessibilityNodeInfoCompat.AccessibilityActionCompat)actionList.get(i));
            }
        }
        
        public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            this.mCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }
        
        public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
            return this.mCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
        
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            return this.mCompat.performAccessibilityAction(view, n, bundle);
        }
        
        public void sendAccessibilityEvent(final View view, final int n) {
            this.mCompat.sendAccessibilityEvent(view, n);
        }
        
        public void sendAccessibilityEventUnchecked(final View view, final AccessibilityEvent accessibilityEvent) {
            this.mCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
        }
    }
}
