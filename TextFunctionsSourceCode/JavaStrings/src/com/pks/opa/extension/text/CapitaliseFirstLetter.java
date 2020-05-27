/*
 * Copyright (C) 2017 P.K. Shersby
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
import com.oracle.determinations.engine.CustomFunction;
import com.oracle.determinations.engine.EntityInstance;


/**
 * Returns the input but with the first  letter character converted to upper case
 * so "!!!john!!!" is converted to "!!!John!!!" 
 * usage text_attribute = CapitaliseFirstLetter(text_attribute)
 * @author pshersby
 */
public class CapitaliseFirstLetter extends CustomFunction{
    
/**
 * OPA Custom function that Returns the input but with the first  letter character converted to upper case
 * so "!!!john!!!" is converted to "!!!John!!!" 
 * usage text_attribute = CapitaliseFirstLetter(text_attribute)
 * @author pshersby
     * @param ei the entity context
     * @param os the array of input parameters from OPA
     * @return 
 */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        return getCapitaliseEachLetter(os[0].toString());
       
        
    }
    
    /**
     * A static function that returns a string in which the first letter of the parameter is converted to a capital
     * @param input the string to process
     * @return a string that is the same as the iinput string except the first letter is converted to upper case
     */
    public static String getCapitaliseEachLetter(String input){
        
        if (input.isEmpty()) return "";
        
        int f = 0;
        int length = input.length();
        boolean convertNext = true;
        StringBuilder result = new StringBuilder(input);
        
        while (f< length && convertNext) {
            if (Character.isLetter(result.charAt(f))){
                result.setCharAt(f, Character.toUpperCase(result.charAt(f)));
                convertNext = false;
            }
        }

        return result.toString();
    }
}
            
                
    

