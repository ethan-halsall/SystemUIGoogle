// 
// Decompiled by Procyon v0.5.36
// 

package androidx.fragment.app;

import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import android.view.ViewParent;
import android.graphics.RectF;
import android.graphics.Rect;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import android.view.ViewGroup;
import android.view.View;
import java.util.List;
import android.annotation.SuppressLint;

@SuppressLint({ "UnknownNullness" })
public abstract class FragmentTransitionImpl
{
    protected static void bfsAddViewChildren(final List<View> list, View view) {
        final int size = list.size();
        if (containedBeforeIndex(list, view, size)) {
            return;
        }
        list.add(view);
        for (int i = size; i < list.size(); ++i) {
            view = list.get(i);
            if (view instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup)view;
                for (int childCount = viewGroup.getChildCount(), j = 0; j < childCount; ++j) {
                    final View child = viewGroup.getChildAt(j);
                    if (!containedBeforeIndex(list, child, size)) {
                        list.add(child);
                    }
                }
            }
        }
    }
    
    private static boolean containedBeforeIndex(final List<View> list, final View view, final int n) {
        for (int i = 0; i < n; ++i) {
            if (list.get(i) == view) {
                return true;
            }
        }
        return false;
    }
    
    static String findKeyForValue(final Map<String, String> map, final String s) {
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            if (s.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    protected static boolean isNullOrEmpty(final List list) {
        return list == null || list.isEmpty();
    }
    
    public abstract void addTarget(final Object p0, final View p1);
    
    public abstract void addTargets(final Object p0, final ArrayList<View> p1);
    
    public abstract void beginDelayedTransition(final ViewGroup p0, final Object p1);
    
    public abstract boolean canHandle(final Object p0);
    
    void captureTransitioningViews(final ArrayList<View> list, final View e) {
        if (e.getVisibility() == 0) {
            if (e instanceof ViewGroup) {
                final ViewGroup e2 = (ViewGroup)e;
                if (ViewGroupCompat.isTransitionGroup(e2)) {
                    list.add((View)e2);
                }
                else {
                    for (int childCount = e2.getChildCount(), i = 0; i < childCount; ++i) {
                        this.captureTransitioningViews(list, e2.getChildAt(i));
                    }
                }
            }
            else {
                list.add(e);
            }
        }
    }
    
    public abstract Object cloneTransition(final Object p0);
    
    void findNamedViews(final Map<String, View> map, final View view) {
        if (view.getVisibility() == 0) {
            final String transitionName = ViewCompat.getTransitionName(view);
            if (transitionName != null) {
                map.put(transitionName, view);
            }
            if (view instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup)view;
                for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
                    this.findNamedViews(map, viewGroup.getChildAt(i));
                }
            }
        }
    }
    
    protected void getBoundsOnScreen(final View view, final Rect rect) {
        if (!ViewCompat.isAttachedToWindow(view)) {
            return;
        }
        final RectF rectF = new RectF();
        rectF.set(0.0f, 0.0f, (float)view.getWidth(), (float)view.getHeight());
        view.getMatrix().mapRect(rectF);
        rectF.offset((float)view.getLeft(), (float)view.getTop());
        View view2;
        for (ViewParent viewParent = view.getParent(); viewParent instanceof View; viewParent = view2.getParent()) {
            view2 = (View)viewParent;
            rectF.offset((float)(-view2.getScrollX()), (float)(-view2.getScrollY()));
            view2.getMatrix().mapRect(rectF);
            rectF.offset((float)view2.getLeft(), (float)view2.getTop());
        }
        final int[] array = new int[2];
        view.getRootView().getLocationOnScreen(array);
        rectF.offset((float)array[0], (float)array[1]);
        rect.set(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }
    
    public abstract Object mergeTransitionsInSequence(final Object p0, final Object p1, final Object p2);
    
    public abstract Object mergeTransitionsTogether(final Object p0, final Object p1, final Object p2);
    
    ArrayList<String> prepareSetNameOverridesReordered(final ArrayList<View> list) {
        final ArrayList<String> list2 = new ArrayList<String>();
        for (int size = list.size(), i = 0; i < size; ++i) {
            final View view = list.get(i);
            list2.add(ViewCompat.getTransitionName(view));
            ViewCompat.setTransitionName(view, null);
        }
        return list2;
    }
    
    public abstract void removeTarget(final Object p0, final View p1);
    
    public abstract void replaceTargets(final Object p0, final ArrayList<View> p1, final ArrayList<View> p2);
    
    public abstract void scheduleHideFragmentView(final Object p0, final View p1, final ArrayList<View> p2);
    
    void scheduleNameReset(final ViewGroup viewGroup, final ArrayList<View> list, final Map<String, String> map) {
        OneShotPreDrawListener.add((View)viewGroup, new Runnable(this) {
            @Override
            public void run() {
                for (int size = list.size(), i = 0; i < size; ++i) {
                    final View view = list.get(i);
                    ViewCompat.setTransitionName(view, (String)map.get(ViewCompat.getTransitionName(view)));
                }
            }
        });
    }
    
    public abstract void scheduleRemoveTargets(final Object p0, final Object p1, final ArrayList<View> p2, final Object p3, final ArrayList<View> p4, final Object p5, final ArrayList<View> p6);
    
    public abstract void setEpicenter(final Object p0, final Rect p1);
    
    public abstract void setEpicenter(final Object p0, final View p1);
    
    public void setListenerForTransitionEnd(final Fragment fragment, final Object o, final CancellationSignal cancellationSignal, final Runnable runnable) {
        runnable.run();
    }
    
    void setNameOverridesOrdered(final View view, final ArrayList<View> list, final Map<String, String> map) {
        OneShotPreDrawListener.add(view, new Runnable(this) {
            @Override
            public void run() {
                for (int size = list.size(), i = 0; i < size; ++i) {
                    final View view = list.get(i);
                    final String transitionName = ViewCompat.getTransitionName(view);
                    if (transitionName != null) {
                        ViewCompat.setTransitionName(view, FragmentTransitionImpl.findKeyForValue(map, transitionName));
                    }
                }
            }
        });
    }
    
    void setNameOverridesReordered(final View view, final ArrayList<View> list, final ArrayList<View> list2, final ArrayList<String> list3, final Map<String, String> map) {
        final int size = list2.size();
        final ArrayList<String> list4 = new ArrayList<String>();
        for (int i = 0; i < size; ++i) {
            final View view2 = list.get(i);
            final String transitionName = ViewCompat.getTransitionName(view2);
            list4.add(transitionName);
            if (transitionName != null) {
                ViewCompat.setTransitionName(view2, null);
                final String s = map.get(transitionName);
                for (int j = 0; j < size; ++j) {
                    if (s.equals(list3.get(j))) {
                        ViewCompat.setTransitionName(list2.get(j), transitionName);
                        break;
                    }
                }
            }
        }
        OneShotPreDrawListener.add(view, new Runnable(this) {
            @Override
            public void run() {
                for (int i = 0; i < size; ++i) {
                    ViewCompat.setTransitionName((View)list2.get(i), (String)list3.get(i));
                    ViewCompat.setTransitionName((View)list.get(i), (String)list4.get(i));
                }
            }
        });
    }
    
    public abstract void setSharedElementTargets(final Object p0, final View p1, final ArrayList<View> p2);
    
    public abstract void swapSharedElementTargets(final Object p0, final ArrayList<View> p1, final ArrayList<View> p2);
    
    public abstract Object wrapTransitionInSet(final Object p0);
}
