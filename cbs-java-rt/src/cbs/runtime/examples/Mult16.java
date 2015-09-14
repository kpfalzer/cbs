/*
 * The MIT License
 *
 * Copyright 2015 kpfalzer.
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
package cbs.runtime.examples;

import cbs.runtime.*;

/**
 * 16bit multiplier implementation using 16-stages.
 *
 * @author kpfalzer
 */
public class Mult16 implements IProcess {

    private static final int stNumStages = 16;

    public Mult16(
            Signal<Integer> a,
            Signal<Integer> b,
            Signal<Integer> z
    ) {
        m_a = new Input<>(a);
        m_b = new Input<>(b);
        m_z = new State<>(true, new Output<>(z));
    }

    @Override
    public int process() {
        int rval = -1;
        for (int i = m_stages.length - 1; i > 0; i--) {
            if (null != m_stages[i - 1]) {
                m_stages[i] = new Stage(m_stages[i - 1]);
                m_stages[i].compute();
            }
        }
        m_stages[0] = new Stage(m_a.get(), m_b.get());
        if (null != m_stages[stNumStages - 1]) {
            rval = m_stages[stNumStages - 1].m_sum;
            m_z.set(rval);
        }
        return rval;
    }

    private static class Stage {

        private Stage(int a, int b) {
            m_a = a;
            m_b = b;
            debug_origABZ = new int[]{a,b,a*b};
            m_sum = 0;
            compute();
        }

        private Stage(Stage prev) {
            m_a = prev.m_a << 1;
            m_b = prev.m_b >> 1;
            m_sum = prev.m_sum;
            debug_origABZ = prev.debug_origABZ;
        }

        private void compute() {
            m_sum += (0 != (0x01 & m_b)) ? m_a : 0;
        }
        int m_a, m_b, m_sum, debug_origABZ[];
    }

    private final Stage m_stages[] = new Stage[stNumStages];

    private final Input<Integer> m_a, m_b;
    private final State<Integer> m_z;
}
