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
 * An OPA Custom Function that 
 * returns the first position of a string within a another string starting at a specific
 * character position (0 is first character).  Returns uncertain if the text is not found or a parameter is uncertain, 
 * or unknown if any parameter is unknown
 * usage  number_attribute = PositionOf(searched_for_text, text_to_search, position to start)
 * @author pshersby
 */
public class PositionOf extends CustomFunction{

    /**
    * An OPA Custom Function that 
    * returns the first position of a string within a another string starting at a specific
    * character position (0 is first character).  Returns uncertain if the text is not found or a parameter is uncertain, 
    * or unknown if any parameter is unknown
    * usage  number_attribute = PositionOf(searched_for_text, text_to_search, position to start)
    * @author pshersby
    * @param ei
    * @param os
    * @return the position of a string within another
    */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        String searchedForText= os[0].toString();
        String searchedText= os[1].toString();
        Double startPos = (Double)os[2];
        int position = searchedText.indexOf(searchedForText, startPos.intValue());
        return position >=0? new Double(position): Uncertain.INSTANCE;
        
    }
    
}
