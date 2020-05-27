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
import com.oracle.determinations.engine.Uncertain;
/**
 *
 * @author pshersby
 */
public class ContainsVisibleCharactersTest {
    
    public ContainsVisibleCharactersTest() {
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
     * Test of evaluate method, of class ContainsVisibleCharacters.
     */
    @Test
    public void testEvaluateUnknown() {
        System.out.println("evaluate");
        EntityInstance ei = null;
        Object[] os = {null, false};
        ContainsVisibleCharacters instance = new ContainsVisibleCharacters();
        Object expResult = null;
        Object result = instance.evaluate(ei, os);
        assertEquals(expResult, result);
    }
        /**
     * Test of evaluate method, of class ContainsVisibleCharacters.
     */
    @Test
    public void testEvaluateUncertain() {
        System.out.println("evaluate");
        EntityInstance ei = null;
        Object[] os = {Uncertain.INSTANCE, false};
        ContainsVisibleCharacters instance = new ContainsVisibleCharacters();
        Object expResult = Uncertain.INSTANCE;
        Object result = instance.evaluate(ei, os);
        assertEquals(expResult, result);
    }
        /**
     * Test of evaluate method, of class ContainsVisibleCharacters.
     */
    @Test
    public void testEvaluateEmptytoFalse() {
        System.out.println("evaluate");
        EntityInstance ei = null;
        Object[] os = {"", false};
        ContainsVisibleCharacters instance = new ContainsVisibleCharacters();
        Object expResult = false;
        Object result = instance.evaluate(ei, os);
        assertEquals(expResult, result);
    }
    
        @Test
    public void testEvaluateUnknowntoFalse() {
        System.out.println("evaluate");
        EntityInstance ei = null;
        Object[] os = {null, true};
        ContainsVisibleCharacters instance = new ContainsVisibleCharacters();
        Object expResult = false;
        Object result = instance.evaluate(ei, os);
        assertEquals(expResult, result);
    }
            @Test
    public void testEvaluateSontingInString() {
        System.out.println("evaluate");
        EntityInstance ei = null;
        Object[] os = {" h ", true};
        ContainsVisibleCharacters instance = new ContainsVisibleCharacters();
        Object expResult = true;
        Object result = instance.evaluate(ei, os);
        assertEquals(expResult, result);
    }

                @Test
    public void testEvaluatewhitespaceInString() {
        System.out.println("evaluate");
        EntityInstance ei = null;
        Object[] os = {"  ", true};
        ContainsVisibleCharacters instance = new ContainsVisibleCharacters();
        Object expResult = false;
        Object result = instance.evaluate(ei, os);
        assertEquals(expResult, result);
    }
    /**
     * Test of requireKnownParameters method, of class ContainsVisibleCharacters.
     */
    @Test
    public void testRequireKnownParameters() {
        System.out.println("requireKnownParameters");
        ContainsVisibleCharacters instance = new ContainsVisibleCharacters();
        boolean expResult = false;
        boolean result = instance.requireKnownParameters();
        assertEquals(expResult, result);
 
    }
    
}
