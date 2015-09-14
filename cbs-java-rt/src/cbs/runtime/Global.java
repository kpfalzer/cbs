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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author karlp
 */
public class Global {

    public static final String stDefaultClockName = "clk";
    public static final int stDefaultPeriod = 1;

    public static void addProcess(IProcess proc) {
        addProcess(stDefaultClockName, proc);
    }

    public static void addState(IUpdate state) {
        addState(stDefaultClockName, state);
    }

    public static void addProcess(String clk, IProcess proc) {
        stClocksByName.get(clk).addProcess(proc);
    }

    public static void addState(String clk, IUpdate state) {
        stClocksByName.get(clk).addState(state);
    }

    public static long doOneCycle() throws InterruptedException {
        final long begin = System.currentTimeMillis();
        final Collection<Clock> clocks = stClocksByName.values();
        assert 1 == clocks.size();
        boolean ok = true;
        for (Clock clock : clocks) {
            if (! evaluate(clock)) {
                String reason = "evaluate("+clock.getName()+") failed @"+clock.getTime();
                throw new RuntimeException(reason);
            }
            System.out.println("update: BEGIN");
            update(clock);
            System.out.println("update: END");
            clock.incr();
        }
        return System.currentTimeMillis() - begin;
    }

    private static void update(Clock clock) {
        for (IUpdate update : clock.getStates()) {
            update.update();    //no multithread
        }
    }

    private static boolean evaluate(Clock clock) throws InterruptedException {
        return Evaluate.evaluate(clock.getProcesses());
    }

    private static final Map<String, Clock> stClocksByName = new HashMap<>();

    static {
        stClocksByName.put(stDefaultClockName, new Clock(stDefaultClockName, stDefaultPeriod));
    }
}
