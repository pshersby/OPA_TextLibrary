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

import com.oracle.determinations.engine.CustomFunction;
import com.oracle.determinations.engine.EntityInstance;
import com.oracle.determinations.engine.Uncertain;

/**
 * OPA function that tests if a text value contains some text other than whitespace.
 * can be configured to either return false in the event that the tested string is unknown or uncertain, or the appropriate unknown/uncertain
 * @author pshersby
 */
public class ContainsVisibleCharacters extends CustomFunction{

    
    /**
 * OPA function that tests if a text value contains some text other than whitespace.
 * using the second parameter the function can be configured to either return false in the event that the tested string is unknown or uncertain, or the appropriate unknown/uncertain
 * usage:   boolean = ContainsVisibleCharacters(stringToTest:text, TreatUnknownUncertainAsEmpty:boolean)
 * 
 * 
 * @author pshersby
     * @param ei   entity instance for context
     * @param os   first parameter is text, second is true if return false for unknown/uncertain
     * @return true, false or unknown/uncertain
 */
    
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        boolean handleUnknownUncertain = (Boolean)os[1];
        Boolean retval = false;
        //Return unknlown or uncertain if we are not handling them
        if (!handleUnknownUncertain) {
            if (os[0]==null )  return os[0];
            if (os[0].equals(Uncertain.INSTANCE )) return os[0];
        }
        
        if (os[0]==null ) return retval;
        if (os[0].equals(Uncertain.INSTANCE )) return retval;
        
        String w =  os[0].toString().trim();
        retval = !w.isEmpty();
        return retval;
        
    }
    
    
        @Override
    public boolean requireKnownParameters(){
        return false;
    }
    
}
