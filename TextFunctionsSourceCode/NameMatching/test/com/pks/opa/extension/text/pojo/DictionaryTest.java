/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pks.opa.extension.text.pojo;

import com.pks.names.extension.pojo.Dictionary;
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
public class DictionaryTest {
    
    public DictionaryTest() {
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

    @Test
    public void testlookup() {
        String original = "ROB";
        String expResult = "ROBERT";
        String result = Dictionary.getSubstitute(original, Dictionary.Location.MID_WORD);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testlookup2() {
        String original = "BOBBY";
        String expResult = "BOBBY";
        String result;
        result = Dictionary.getSubstitute(original, Dictionary.Location.MID_WORD);
        assertEquals(expResult, result);
    }


    
}
