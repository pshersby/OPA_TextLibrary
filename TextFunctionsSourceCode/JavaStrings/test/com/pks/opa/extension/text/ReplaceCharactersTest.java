/*
 * Copyright (C) 2018 pshersby
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
public class ReplaceCharactersTest {
    
    public ReplaceCharactersTest() {
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
     * Test of evaluate method, of class ReplaceCharacters.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate replaceCharacters");
        EntityInstance ei = null;
        Object[] os = {"(1234)-567+8","[,(,),+,-,],","",","};
        ReplaceCharacters instance = new ReplaceCharacters();
        Object expResult = "12345678";
        Object result = instance.evaluate(ei, os);
        assertEquals(expResult, result);

    }
    
}
