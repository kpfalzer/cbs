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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kpfalzer
 */
public class Evaluate {

    public static int stNumThreads = Runtime.getRuntime().availableProcessors();
    public static int stMaxWaitSecs = 300;
    public static boolean stUseThreadPool = false;

    public static boolean evaluate(Collection<IProcess> procs, int numThreads) throws InterruptedException {
        if ((1 == numThreads) || (2 > procs.size())) {
            for (IProcess proc : procs) {
                proc.process();
            }
            return true;
        } else if (stUseThreadPool) {
            ExecutorService pool = Executors.newFixedThreadPool(numThreads);
            for (IProcess proc : procs) {
                pool.execute(() -> {
                    proc.process();
                });
            }
            pool.shutdown();
            return pool.awaitTermination(stMaxWaitSecs, TimeUnit.SECONDS);
        } else {
            if (null == m_consumers) {
                //first time through, start threads
                m_consumers = new Thread[stNumThreads];
                for (int i = 0; i < stNumThreads; i++) {
                    Consumer consumer = new Consumer(i);
                    m_consumers[i] = new Thread(consumer);
                    m_consumers[i].start();
                }
            }
            for (IProcess proc : procs) {
                m_work.add(proc);
            }
            while (!m_work.isEmpty()) {
                ;
            }
            return true;
        }
    }

    public static boolean evaluate(Collection<IProcess> procs) throws InterruptedException {
        return evaluate(procs, stNumThreads);
    }

    private static Thread m_consumers[] = null;
    private static BlockingQueue<IProcess> m_work = new LinkedBlockingQueue<>();

    //TODO: fix using https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html
    
    private static class Consumer implements Runnable {
        Consumer(int id) {
            m_id = id;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    IProcess proc = m_work.take();
                    if (null != proc) {
                        int rval = proc.process();
                        m_cnt++;
                        System.out.printf("consumer %d: proc=%d rval=%d cnt=%d\n",m_id,proc.hashCode(),rval,m_cnt);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Evaluate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private long    m_cnt = 0;
        private final int     m_id;
    }

}
