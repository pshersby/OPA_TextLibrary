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
 * Returns a locale specific upper case version of the string passed in in
 * the first parameter
 * usage text_attribute = CapitaliseAll(text_attribute)
 * @author pshersby
 */
public class CapitaliseAll extends CustomFunction{
    

/**
 * An OPA Custom function that returns a locale specific upper case version of the string passed in in
 * the parameter
 * <p>usage text_attribute = CapitaliseAll(text_attribute)</p>
     * @param ei an entity instance
     * @param os the String to be converted
     * @return 
     */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        return os[0].toString().toUpperCase();
    }
}
            
                
    

