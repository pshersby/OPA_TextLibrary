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
package com.pks.names.extension.pojo;

/**
 * Bank Name List provides a specialism of NameList so that it can perform actions 
 * that only relate to bank names.  In particular bank names for businesses may 
 * include phrases such as "business account" and these need removing during
 * name comparisons
 * @author pshersby
 */
public class BankNameList extends NameList implements InameList{
    
    public BankNameList(String phrase, String spaceList, boolean isPerson, boolean stripLastBracket) {
        super(phrase, spaceList, isPerson,stripLastBracket );
                
        
        // for the bank we re-substitute if a person to strip business account etc
        // need to mark first and last words since these have meaning in peoples names
        // they may have changes since substitution may have deleted some words
        if (isPerson ){
            this.substitute(null,words);
            words.get(0).setIsFirstWord(true);
            words.get(words.size()-1).setIsLastWord(true);
        }
    }
   
}
