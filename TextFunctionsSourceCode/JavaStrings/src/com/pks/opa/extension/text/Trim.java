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
 * An OPA custom function Extension class that trims the leading and trailing whitespace
 * From a text attribute
 * @author pshersby
 */
public class Trim extends CustomFunction{

    /**
     * An OPA custom function Extension class that trims the leading and trailing whitespace
 * From a text attribute.
 * <p>Usage:  text_attribute = Trim(text_attribute/expression)
     * @param ei the entity
     * @param os the parameter array.  
     * @return 
     */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        if (os[0].toString().isEmpty()) return os[0];
        return os[0].toString().trim();
           
    }
    
}
