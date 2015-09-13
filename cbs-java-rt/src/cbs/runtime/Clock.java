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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author karlp
 */
public class Clock {

    public Clock(String name, int period) {
        m_name = name;
        m_period = period;
    }

    public void addProcess(IProcess proc) {
        m_processes.add(proc);
    }

    public void addState(IUpdate state) {
        m_states.add(state);
    }

    public String getName() {
        return m_name;
    }
    
    public long getTime() {
        return m_time;
    }
    
    public int getPeriod() {
        return m_period;
    }
    
    Collection<IProcess> getProcesses() {
        return Collections.unmodifiableCollection(m_processes);
    }
    
    Collection<IUpdate> getStates() {
        return Collections.unmodifiableCollection(m_states);
    }

    long incr() {
        return (m_time += m_period);
    }
    
    private final String m_name;
    private final int   m_period;
    private long  m_time = 0;
    private final List<IProcess> m_processes = new LinkedList<>();
    private final List<IUpdate> m_states = new LinkedList();

}
