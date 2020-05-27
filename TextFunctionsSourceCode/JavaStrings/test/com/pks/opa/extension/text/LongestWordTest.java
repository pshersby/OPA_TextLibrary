/*
 * Copyright (C) 2017 pshersby
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.pks.opa.extension.text;

import com.oracle.determinations.engine.EntityInstance;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pshersby
 */
public class LongestWordTest {
    
    public LongestWordTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of evaluate method, of class LongestWord.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        EntityInstance ei = null;
        Object[] os = {null};
        LongestWord instance = new LongestWord();
        Object expResult = null;
        Object result = instance.evaluate(ei, os);
        assertEquals(new Double (0), result);
        // TODO review the generated test code and remove the default call to fail.
     
    }
    /**
     * Test of evaluate method, of class LongestWord.
     */
    @Test
    public void testEvaluate1() {
        System.out.println("evaluate");
        EntityInstance ei = null;
        Object[] os = {"Hello this is a string with lots of lllllllong word"};
        LongestWord instance = new LongestWord();
        Object expResult = null;
        Object result = instance.evaluate(ei, os);
        assertEquals(new Double (10), result);
        // TODO review the generated test code and remove the default call to fail.
     
    }

    
}
