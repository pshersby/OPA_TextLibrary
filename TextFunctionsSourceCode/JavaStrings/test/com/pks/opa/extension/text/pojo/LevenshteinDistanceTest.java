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
package com.pks.opa.extension.text.pojo;

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
public class LevenshteinDistanceTest {
    

    
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
     * Test of apply method, of class LevenshteinDistance.
     */
    @Test
    public void testApply() {
        System.out.println("apply");
        CharSequence left = "Hello";
        CharSequence right = "elo";
        LevenshteinDistance instance = new LevenshteinDistance();
        Integer expResult = 2;
        Integer result = instance.apply(left, right);
        assertEquals(expResult, result);
 
    }
    /**
     * Test of apply method, of class LevenshteinDistance.
     */
    @Test
    public void testApplyWith5() {
        System.out.println("apply");
        CharSequence left = "Hello";
        CharSequence right = "elo";
        LevenshteinDistance instance = new LevenshteinDistance(5);
        Integer expResult = 2;
        Integer result = instance.apply(left, right);
        assertEquals(expResult, result);

    }

    /**
     * Test of apply method, of class LevenshteinDistance.
     */
    @Test
    public void testApplyWith5andBigProblem() {
        System.out.println("apply");
        CharSequence left = "Hello Matey Boy";
        CharSequence right = "elo";
        LevenshteinDistance instance = new LevenshteinDistance(5);
        Integer expResult = -1;
        Integer result = instance.apply(left, right);
        assertEquals(expResult, result);

    }    


    /**
     * Test of getThreshold method, of class LevenshteinDistance.
     */
    @Test
    public void testGetThreshold() {
        System.out.println("getThreshold");
        LevenshteinDistance instance = new LevenshteinDistance();
        Integer expResult = null;
        Integer result = instance.getThreshold();
        assertEquals(expResult, result);
 
    }

    /**
     * Test of getDefaultInstance method, of class LevenshteinDistance.
     */
    @Test
    public void testGetDefaultInstance() {
        System.out.println("getDefaultInstance");
                LevenshteinDistance result = LevenshteinDistance.getDefaultInstance();
        assertNotNull( result);

    }
    
}
