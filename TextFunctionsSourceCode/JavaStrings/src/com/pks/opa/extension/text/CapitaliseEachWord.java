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
 * An OPA CustomFunction that Returns the input but with the first  letter of each word converted to upper case
 * so "!!!john 123 smith!!!" is converted to "!!!John 123 Smith!!!" 
 * usage text_attribute = CapitaliseFirstLetter(text_attribute)
 * @author pshersby
 */
public class CapitaliseEachWord extends CustomFunction{
    
/**
 * AN OPA Custom function that returns the input but with the first letter of each word converted to upper case
 * so "!!!john 123 smith!!!" is converted to "!!!John 123 Smith!!!" 
 * usage text_attribute = CapitaliseFirstLetter(text_attribute)
 * @author pshersby
     * @param ei the entity for context
     * @param os the parameter being passed to the custom function
     * @return the input string converted to a form with each word starting with a capital
 */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        return getCapitaliseEachWord(os[0].toString());
   
    }
    
    
    /**
     * A static function exposed to enable simple capitalisation of words
     * by other OPA extensions.
     * 
     * @param input
     * @return A capitalised version of the input string where the first letter of each word is converted to a capital letter.
     */
    public static String getCapitaliseEachWord(String input){
        
        if (input.isEmpty()) return "";
        
        int f ;
        int length = input.length();
        boolean convertNext = true;
        StringBuilder result = new StringBuilder(input);
        
        for (f = 0; f< length; f++) {
            if (Character.isLetter(result.charAt(f))&& convertNext){
                result.setCharAt(f, Character.toUpperCase(result.charAt(f)));
            }
            convertNext = !Character.isLetter(result.charAt(f));
        }

        return result.toString();
    }
    
    
}
            
                
    

