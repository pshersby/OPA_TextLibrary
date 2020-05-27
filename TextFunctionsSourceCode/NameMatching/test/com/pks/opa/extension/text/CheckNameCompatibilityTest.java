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

import com.pks.names.extension.CheckBankNameCompatibility;
import com.pks.names.extension.CheckNameCompatibility;
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
public class CheckNameCompatibilityTest {
    CheckBankNameCompatibility instance = new CheckBankNameCompatibility();
    public CheckNameCompatibilityTest() {
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
     * Test of evaluate method, of class CheckNameCompatibility.
     */
    @Test
    public void testEvaluate3names() {
        System.out.println("evaluate");
        CheckNameCompatibility instance = new CheckNameCompatibility();
        Object expResult = true;
        Object result = instance.evaluate("John Andrew Smith","MR J SMITH", true);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
        @Test
        public void testEvaluate3bnames() {
        System.out.println("evaluate3b");
        Object expResult = true;
        Object result = instance.evaluate("John Andrew Smith","MR J SMITH BUSINESS ACCOUNT",  "PERSON", "GBP");
        assertEquals(expResult, result);
        }
        
                @Test
        public void testEvaluateReverseNames() {
        System.out.println("evaluate3b");
        Object expResult = true;
        Object result = instance.evaluate("John Andrew Smith","Smith, John", "PERSON", "GBP");
        assertEquals(expResult, result);
        }
        
        @Test
        public void testEvaluateShortenNames() {
        System.out.println("evaluate3b");
        Object expResult = true;
        Object result = instance.evaluate("Tim Andrew Smith","Smith, Timothy", "PERSON", "GBP");
        assertEquals(expResult, result);
        }
        
        @Test
        public void testEvaluateBracketsNames() {
        System.out.println("evaluate3b");
        Object expResult = true;
        Object result = instance.evaluate("Atom filters (UK) Ltd","Atom Filters LTD", "COMPANY", "GBP");
        assertEquals(expResult, result);
        }
    
                @Test
        public void testEvaluateWierdInitilsNames() {
        System.out.println("Initials1");
        Object expResult = true;
        Object result = instance.evaluate("JA Smith","John Smith", "PERSON", "GBP");
        assertEquals(expResult, result);
        }
                        @Test
        public void testEvaluateWierdInitils2Names() {
        System.out.println("Initials1");
        Object expResult = true;
        Object result = instance.evaluate("JA Smith","John Andrew Smith", "PERSON", "GBP");
        assertEquals(expResult, result);
        }
                        @Test
        public void testEvaluateWierdInitils3Names() {
        System.out.println("Initials1");
        Object expResult = false;
        Object result = instance.evaluate("JA Smith","John Bob Smith", "PERSON", "GBP");
        assertEquals(expResult, result);
        }
        
}
