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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author karlp
 */
public class State<T> extends Value<T> implements IUpdate {

    public State(T val) {
        set(val);
        addToClock();
    }
    
    public State(boolean unused, Output<T> ... outs) {
        for (Output<T> out : outs) {
            addFanout(out);
        }
        addToClock();
    }
    
    private void addToClock() {
        Global.addState(this);
    }

    public final void addFanout(Output<T> out) {
        if (null == m_outs) {
            m_outs = new LinkedList<>();
        }
        m_outs.add(out);
    }
    
    @Override
    public final T set(T val) {
        m_next = val;
        System.out.printf("State: next=%d\n", m_next);
        return val;
    }

    @Override
    public void update() {
        super.set(m_next);
        if (null != m_outs) {
            for (Output<T> out : m_outs) {
                out.set(get());
                System.out.printf("out.get()=%d\n", out.get());
            }
        }
    }

    private T m_next;
    private List<Output<T>> m_outs = null;

}
