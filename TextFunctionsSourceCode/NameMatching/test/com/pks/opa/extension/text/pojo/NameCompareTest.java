/*
 * To run tests you must include Library  JUnit 4.12 or later
 */
package com.pks.opa.extension.text.pojo;

import com.pks.names.extension.pojo.NameCompare;
import com.pks.names.extension.pojo.NameCompare.NameMatch;
import static com.pks.names.extension.pojo.NameCompare.NameMatch.*;
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
public class NameCompareTest {
    
    public NameCompareTest() {
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
     * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate1() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("John Smith","John Smith TA Smith Joinery", true);
        assertEquals(expResult, result);

    }
        /**
     * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate2() {
        System.out.println("evaluate");
        NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("smith joinery","John Smith TA Smith Joinery", false);
        assertEquals(expResult, result);

    }
           /**
     * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate3() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("smiths joinery","John Smith TA Smith Joinery", false);
        assertEquals(expResult, result);

    }
               /**
     * Test of evaluate method, of class NameCompare.
     * In this case testing a typo
     */
    @Test
    public void testEvaluate4() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("smiths joinery","John Smith TA Smith Joinry", false);
        assertEquals(expResult, result);

    }
                   /**
     * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate5() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = DIFFERS;
        NameMatch result = instance.compareNames("smiths joinery","John Smith TA Andrews Joinery", false);
        assertEquals(expResult, result);

    }
    
     /**    * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate6() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("anna Smith LTD","Anna smith LIMITED", false);
        assertEquals(expResult, result);

    }
     /**    * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate7() {
        System.out.println("evaluate");
 
        NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("John Henry Smith","John smith", true);
        assertEquals(expResult, result);

    }
         /**    * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate8() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = DIFFERS;
        NameMatch result = instance.compareNames("Josy Smith","Josephine smith", true);
        assertEquals(expResult, result);

    }
    
             /**    * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate9() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("Wise Move Logistics","Wise Move Logistics Ltd", false);
        assertEquals(expResult, result);

    }
    
    
                /**    * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate10() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("John Big Smith","Mr JB Smith", true);
        assertEquals(expResult, result);

    }
    
    
    
                    /**    * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate11() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("One Big Company Ltd","OneBig Company Limited", false);
        assertEquals(expResult, result);

    }
    
                       /**    * Test of evaluate method, of class NameCompare.
     */
    @Test
    public void testEvaluate12() {
        System.out.println("evaluate");

        NameCompare instance = new NameCompare();
        NameMatch expResult = DIFFERS;
        NameMatch result = instance.compareNames("John Smith","John And Jill Smith", true);
        assertEquals(expResult, result);

    }
        public void testEvaluateInitials() {
        System.out.println("evaluate");
         NameCompare instance = new NameCompare();
        NameMatch expResult = MATCH;
        NameMatch result = instance.compareNames("John ANDREW Smith","MR J Smith", true);
        assertEquals(expResult, result);  
    }
    
    
    /**
     * Test of extractTradingName method, of class NameCompare.
     * 
     */
    @Test 
    public void testExtractTradingName1() {
        System.out.println("extractTradingName");
        String originalName = "John Smith TRADING AS Limited Joinery";
        String[] phrases = NameCompare.TRADENAME_PHRASES;
        NameCompare instance = new NameCompare();
        String[] expResult = {"John Smith","Limited Joinery"};
        String[] result = instance.extractTradingName(originalName, phrases);
        assertArrayEquals(expResult, result);
   
    }
    
        /**
     * Test of extractTradingName method, of class NameCompare.
     */
    @Test 
    public void testExtractTradingName2() {
        System.out.println("extractTradingName");
        String originalName = "John Smith";
        String[] phrases = NameCompare.TRADENAME_PHRASES;
        NameCompare instance = new NameCompare();
        String[] expResult = {"John Smith",""};
        String[] result = instance.extractTradingName(originalName, phrases);
        assertArrayEquals(expResult, result);
   
    }
        /**
     * Test of extractTradingName method, of class NameCompare.
     */
    @Test 
    public void testExtractTradingName3() {
        System.out.println("extractTradingName");
        String originalName = "John Smith TA Limited Joinery";
        String[] phrases = NameCompare.TRADENAME_PHRASES;
        NameCompare instance = new NameCompare();
        String[] expResult = {"John Smith","Limited Joinery"};
        String[] result = instance.extractTradingName(originalName, phrases);
        assertArrayEquals(expResult, result);
   
    }

 
    
}
