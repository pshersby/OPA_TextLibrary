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
 * Returns the length of the longest word in text.  Unknown and uncertain both return 0
 * @author pshersby
 */
public class LongestWord extends CustomFunction{

    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        int longest = 0;
        int letterCount = 0;
        int f;
        if (os[0]==null ) return new Double(0);
        if (os[0].equals(Uncertain.INSTANCE )) return new Double(0);
        
        String w =  os[0].toString().trim();
        if (w.isEmpty()) return new Double (longest);
        
        for (f=0; f<w.length(); f++) {
            if (Character.isWhitespace(w.charAt(f))){
                if (letterCount >longest) longest = letterCount;
                letterCount = 0;
            }
            else {
                letterCount++;
            }
            if (letterCount >longest) longest = letterCount; 
        }
        return new Double (longest);
        
        
    }
    
    
        @Override
    public boolean requireKnownParameters(){
        return false;
    }
    
}
