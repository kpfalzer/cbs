/*
 * The MIT License
 *
 * Copyright 2015 karlp.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cbs.runtime;

import java.util.BitSet;
import java.util.Objects;

/**
 * Unsigned bit vector.
 *
 * @author karlp
 */
public class BitVector {

    /**
     * Create bit vector [lb:rb] with value 0.
     *
     * @param lb left bound.
     * @param rb right bound.
     */
    public BitVector(int lb, int rb) {
        m_lb = lb;
        m_rb = rb;
        m_isDesc = isDesc(m_lb, m_rb);
        m_val = new BitSet(length(m_lb, m_rb));
    }

    public BitVector(int lb, int rb, long val) {
        m_lb = lb;
        m_rb = rb;
        m_isDesc = isDesc(m_lb, m_rb);
        m_val = BitSet.valueOf(new long[]{val});
        clearUnused();
    }

    /**
     * Clear unused leading bits.
     */
    private void clearUnused() {
        int i;
        final int length = length();
        while ((i = m_val.length()) >= length) {
            m_val.clear(i);
        }
    }

    public BitVector(int lb, int rb, BitSet init) {
        this(lb, rb);
        if (length() < init.length()) {
            String detail = length() + " < " + init.length() + " bits: loses information";
            throw new InvariantException(detail);
        }
        m_val.or(init);
    }

    public BitVector(BitSet init) {
        this(init.length() > 0 ? init.length() - 1 : 0, 0, init);
    }

    public BitVector(BitVector bv) {
        this(bv.getLeft(), bv.getRight(), bv.m_val);
    }

    public BitSet set(BitSet val) {
        m_val.clear();
        m_val.or(val);
        clearUnused();
        return get();
    }

    public BitSet get() {
        return (BitSet) m_val.clone();
    }

    public boolean get(int ix) {
        return m_val.get(realIx(ix));
    }

    public BitVector slice(int lb, int rb) {
        if (isDesc(lb, rb) != m_isDesc) {
            String detail = "[" + lb + ":" + rb + "] has incorrect direction";
            throw new BoundsException(detail);
        }
        final boolean ok = (m_isDesc) ? ((lb <= getLeft()) && (rb >= getRight()))
                : ((lb >= getLeft()) && (rb <= getRight()));
        if (!ok) {
            String detail = getRangeAsString(lb, rb) + " out of range " + getRangeAsString();
            throw new BoundsException(detail);
        }
        return new BitVector(lb, rb, m_val.get(realIx(rb), realIx(lb) + 1));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(length());
        sb.append(length()).append("'b");
        for (int i = length() - 1; i >= 0; i--) {
            sb.append(m_val.get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object to) {
        if ((null != to) && (to instanceof BitVector)) {
            return ((BitVector) to).m_val.equals(m_val);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.m_val);
        return hash;
    }

    public final int length() {
        return length(getLeft(), getRight());
    }

    /**
     * Check is [lb:rb] is descending (left to right).
     *
     * @param lb left bound.
     * @param rb right bound.
     * @return true iff. (rb <= lb).
     */
    public static boolean isDesc(int lb, int rb) {
        return (rb <= lb);
    }

    public static int length(int lb, int rb) {
        return 1 + Math.abs(lb - rb);
    }

    private int realIx(int ix) {
        final int rix = Math.abs(ix - getRight());
        if (rix >= length()) {
            String detail = "[" + ix + "] out of range " + getRangeAsString();
            throw new BoundsException(detail);
        }
        return rix;
    }

    public int getLeft() {
        return m_lb;
    }

    public int getRight() {
        return m_rb;
    }

    public int[] getRange() {
        return new int[]{getLeft(), getRight()};
    }

    public static String getRangeAsString(int lb, int rb) {
        return "[" + lb + ":" + rb + "]";
    }

    public String getRangeAsString() {
        return getRangeAsString(getLeft(), getRight());
    }

    private final int m_lb, m_rb;
    private final boolean m_isDesc;
    /**
     * Bit store. this[m_rb] is m_val.get(0).
     */
    private BitSet m_val;

    private static class BoundsException extends RuntimeException {

        private BoundsException(String detail) {
            throw new IndexOutOfBoundsException(detail);
        }
    }

    private static class InvariantException extends RuntimeException {

        private InvariantException(String detail) {
            throw new RuntimeException(detail);
        }
    }
}
