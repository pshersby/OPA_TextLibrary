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
 * An OPA custom function Extension class that assembles 4 numeric version identifiers into a single string
 * this is so that there is a simple method to verify that the java extensions are loaded
 * @author pshersby
 */
public class GetVersionString extends CustomFunction{

    /**
    * An OPA custom function Extension  that plugs together first, middle and last names
    * with appropriate spacing.  This is a helpful function to reduce complexity of OPA rules
    * The names may be unknown or uncertain and if so will simply be treated as empty strings.
 * <p>Usage:  text_attribute = AssembleName(text, text, text)</p>
     * @param ei the entity
     * @param os the parameter array (three names).  
     * @return 
     */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
      
        String version = ((Double)os[0]).intValue() +"." +
                ((Double)os[1]).intValue() +"." +
                ((Double)os[2]).intValue() +"." +
                ((Double)os[3]).intValue() ;
               
        return version.trim();
           
    }

    
}
