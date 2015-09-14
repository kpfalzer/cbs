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
package cbs.runtime;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kpfalzer
 */
public class EvaluateTest {

    private static class Process implements IProcess {

        private static int stCnt = 0;

        private Process() {
        }

        private final int m_ix = stCnt++;

        @Override
        public int process() {
            try {
                System.out.printf("Item %04d: %d\n", m_ix, System.currentTimeMillis());
                Thread.sleep(1 * 1000);//5 secs
            } catch (InterruptedException ex) {
                Logger.getLogger(EvaluateTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }
    }

    private final LinkedList<IProcess> m_procs = new LinkedList<>();

    @Before
    public void setUp() {
        final int N = 20;
        for (int i = 0; i < N; i++) {
            m_procs.add(new Process());
        }
    }

    /**
     * Test of evaluate method, of class Evaluate.
     */
    @Test
    public void testEvaluate() throws Exception {
        System.out.println("evaluate");
        Collection<IProcess> procs = m_procs;
        boolean expResult = true;
        final long begin = System.currentTimeMillis();
        boolean result = Evaluate.evaluate(procs);
        assertEquals(expResult, result);
        final long elapsed =System.currentTimeMillis() - begin;
        System.out.printf("Elapsed = %d (msec)\n", elapsed);
    }

}
