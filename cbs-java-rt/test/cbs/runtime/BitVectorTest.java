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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author karlp
 */
public class BitVectorTest {
    
    /**
     * Test of slice method, of class BitVector.
     */
    @Test
    public void testSlice() {
        System.out.println("slice");
        BitVector instance = new BitVector(7, 0, 0x053);
        BitVector expResult = new BitVector(3, 0, 0x03);
        BitVector result = instance.slice(3, 0);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class BitVector.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        BitVector instance = new BitVector(16,1,0xbeef);
        String expResult = (new BitVector(7,0,0xbe)).toString();
        String result = instance.slice(16,9).toString();
        assertEquals(expResult, result);
    }

    
}
