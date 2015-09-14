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

import cbs.runtime.Evaluate;
import cbs.runtime.Global;
import cbs.runtime.Signal;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author kpfalzer
 */
public class Mult16Test {

    private final Queue<Integer> m_expect = new LinkedList<>();

    static class IntSignal extends Signal<Integer> {

        public IntSignal() {
            super(0xdeadbeef);
        }
    }

    /**
     * Test of process method, of class Mult16.
     */
    @Test
    public void testProcess() {
        try {
            Evaluate.stNumThreads = 4;//no MT
            Evaluate.stUseThreadPool = false;
            final boolean TEST = true;
            int N = 2;
            int bubble = 14;
            final int MAX = 0x0f;//f;//0x07fff;
            Signal<Integer> a = new Signal<>(0), b = new Signal<>(0);

            IntSignal z[] = new IntSignal[N];
            Mult16 instance[] = new Mult16[N];
            for (int i = 0; i < z.length; i++) {
                z[i] = new IntSignal();
                z[i].setName("z[" + i + "]");
                instance[i] = new Mult16(a, b, z[i]);
                Global.addProcess(instance[i]);
            }

            int i = 0, expect;
            long took;
            for (int aa = 1; aa <= MAX; aa++) {
                for (int bb = 1; bb <= MAX; bb++) {
                    a.set(aa);
                    b.set(bb);
                    m_expect.add(aa * bb);
                    took = Global.doOneCycle();
                    if (bubble >= 0) {
                        bubble--;
                        continue;
                    }
                    expect = m_expect.remove();

                    for (IntSignal z1 : z) {
                        if (!TEST || (expect != z1.get())) {
                            System.out.println(i + ": " + z1.toString() + " (expect " + expect + ")");
                            //Assert.assertEquals((long) expect, (long) z1.get());
                        }
                    }

                    i++;
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Mult16Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
