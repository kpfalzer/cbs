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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author karlp
 */
public class Global {

    public static final String stDefaultClockName = "clk";

    public static void addProcess(IProcess proc) {
        addProcess(stDefaultClockName, proc);
    }

    public static void addState(IUpdate state) {
        addState(stDefaultClockName, state);
    }

    public static void addProcess(String clk, IProcess proc) {
        Clock clock = stClocksByName.get(clk);
        if (null == clock) {
            clock = new Clock(clk);
            stClocksByName.put(clk, clock);
        }
        clock.addProcess(proc);
    }

    public static void addState(String clk, IUpdate state) {
        Clock clock = stClocksByName.get(clk);
        if (null == clock) {
            clock = new Clock(clk);
            stClocksByName.put(clk, clock);
        }
        clock.addState(state);
    }

    private static final Map<String, Clock> stClocksByName = new HashMap<>();
}
