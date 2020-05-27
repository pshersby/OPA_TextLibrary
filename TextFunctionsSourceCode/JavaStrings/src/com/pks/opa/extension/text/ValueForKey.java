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

import com.oracle.determinations.engine.CustomFunction;
import com.oracle.determinations.engine.EntityInstance;

/**
 *
 * @author pshersby
 */
public class ValueForKey extends CustomFunction{

    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        String propertyList = os[0].toString().trim();
        String keyName = os[1].toString().trim();
        String delimiter = os[2].toString();
        String [] propertyArray;
        String keyFound ;
        if (delimiter.isEmpty()) delimiter = ":";
       
        
        propertyArray = propertyList.split("["+delimiter+"]");
        
        for (String e : propertyArray){
            keyFound = ExtractPropertyKeyOrValue.getPropertyKeyOrValue(e, true).trim();
            if(keyFound.equalsIgnoreCase(keyName)) { return ExtractPropertyKeyOrValue.getPropertyKeyOrValue(e, false).trim(); }
        }
        return null;
        
    }
    
    
    
}
