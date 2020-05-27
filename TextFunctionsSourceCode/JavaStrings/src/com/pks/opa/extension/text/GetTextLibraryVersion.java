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
 * A utility that returns the version number of the text library.  This takes no parameters
 * @author pshersby
 */
public class GetTextLibraryVersion extends CustomFunction {

    /**
 * A utility that returns the version number of the text library.  This takes no parameters
 * @author pshersby
     * @param ei the OPA entity context
     * @param os the OPA array of parameters, in the order string to parse, boolean
     * @return the key or value as a string.  In the event that it is blank it returns and empty string.  if there is no equals sign then it returns uncertain
 */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        return "1.6.0";
        
    }
    
    
    
    
    
    
}
