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
 * An OPA Custom function that splits a string about the = sign and returns either
  * the first part (the property key name) or the last part the value.
 * @author pshersby
 */
public class ExtractPropertyKeyOrValue extends CustomFunction {

    /**
 * An OPA Custom function that splits a string about the = sign and returns either
  * the first part (the property key name) or the last part the value.
  * usage  text = ExtractPropertyKeyOrValue (text, boolean)
  * The first parameter is the property string to parse, the second is the boolean that if true results in the 
  * key being returned, if false results in the value being returned.
 * @author pshersby
     * @param ei the OPA entity context
     * @param os the OPA array of parameters, in the order string to parse, boolean
     * @return the key or value as a string.  In the event that it is blank it returns and empty string.  if there is no equals sign then it returns uncertain
 */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        String s = os[0].toString();
        boolean isKey = (Boolean)os[1];
        String retval = getPropertyKeyOrValue (s, isKey);
        return (retval==null)?Uncertain.INSTANCE : retval;
        
    }
    
    
    /**
     * A Static helper function that splits a string about the = sign and returns either
     * the first part (the property key name) or the last part the value.
     * @param propertyString  the property to be parsed
     * @param getKey   boolean: true if you wan the key, false if you want the value
     * @return the key or value as a string.  In the event that it is blank it returns and empty string.  if there is no equals sign then it returns null
     */
    public static String getPropertyKeyOrValue (String propertyString, boolean getKey){
        String s = propertyString.trim();
        if (s.isEmpty()) return null;
        if (!s.contains("=")) return null;
        
        int pos = s.indexOf("=");
        if (pos <0) return null;
        if (getKey) {
            if (pos==0) return "";  // there is no key name
            return propertyString.substring(0, pos).trim();
        }
        else {
            if (pos==s.length()-1) return "";  // there is no value
            return propertyString.substring(pos+1).trim();
        }

    }
    
    
    
    
}
