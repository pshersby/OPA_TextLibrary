/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class PositionOfTest {
    
    public PositionOfTest() {
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
     * Test of evaluate method, of class PositionOf.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate, find t in Peter");
        EntityInstance ei = null;
        Object[] os = {"t", "Peter", 0.0};
        PositionOf instance = new PositionOf();
        Object expResult = 2.0;
        Object result = instance.evaluate(ei, os);
        assertEquals(expResult, result);

    }
    
}
