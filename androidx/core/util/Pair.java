// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.util;

public class Pair<F, S>
{
    public final F first;
    public final S second;
    
    public Pair(final F first, final S second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof Pair;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final Pair pair = (Pair)o;
        boolean b3 = b2;
        if (ObjectsCompat.equals(pair.first, this.first)) {
            b3 = b2;
            if (ObjectsCompat.equals(pair.second, this.second)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        final F first = this.first;
        int hashCode = 0;
        int hashCode2;
        if (first == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = first.hashCode();
        }
        final S second = this.second;
        if (second != null) {
            hashCode = second.hashCode();
        }
        return hashCode2 ^ hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Pair{");
        sb.append(String.valueOf(this.first));
        sb.append(" ");
        sb.append(String.valueOf(this.second));
        sb.append("}");
        return sb.toString();
    }
}
