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
import com.oracle.determinations.engine.Uncertain;


/**
 * An OPA custom function Extension class that plugs together first, middle and last names
 * with appropriate spacing.  This is a helpful function to reduce complexity of OPA rules
 * The names may be unknown or uncertain and if so will simply be treated as empty strings.
 * @author pshersby
 */
public class AssembleName extends CustomFunction{

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
      
        String name0 = (os[0]==null ||  os[0].equals(Uncertain.INSTANCE) || (os[0].toString().isEmpty()) )?"": os[0].toString()+" ";
        String name1 = (os[1]==null ||  os[1].equals(Uncertain.INSTANCE) || (os[1].toString().isEmpty()) )?"": os[1].toString()+" ";
        String name2 = (os[2]==null ||  os[2].equals(Uncertain.INSTANCE) || (os[2].toString().isEmpty()) )?"": os[2].toString();
        String name = name0 + name1 +name2;
        
       
        return name.trim();
           
    }
    
    @Override
    public boolean requireKnownParameters(){
        return false;
    }
    
}
