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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author kpfalzer
 */
public class Evaluate {

    public static int stNumThreads = Runtime.getRuntime().availableProcessors();
    public static int stMaxWaitSecs = 300;

    public static boolean evaluate(Collection<IProcess> procs, int numThreads) throws InterruptedException {
        if ((1 == numThreads) || (2 > procs.size())) {
            for (IProcess proc : procs) {
                proc.process();
            }
            return true;
        } else {
            ExecutorService pool = Executors.newFixedThreadPool(numThreads);
            for (IProcess proc : procs) {
                pool.execute(() -> {
                    proc.process();
                });
            }
            pool.shutdown();
            return pool.awaitTermination(stMaxWaitSecs, TimeUnit.SECONDS);
        }
    }

    public static boolean evaluate(Collection<IProcess> procs) throws InterruptedException {
        return evaluate(procs, stNumThreads);
    }
}
