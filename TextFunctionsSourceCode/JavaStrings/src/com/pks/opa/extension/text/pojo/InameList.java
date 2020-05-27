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
package com.pks.opa.extension.text.pojo;

/**
 * InameList defines the interface for a list that is intended to specifically deal with names
 * names can be either peoples names or the names of "things"
 * @author pshersby
 */
public interface InameList {

    boolean acceptableMatch(InameList nl);

    /**
     * an equality test between name lists.  This performs only basic
     * one to one tests of the lists and requires full matching of the words
     * @param nl the name list against which to compare
     * @return true if they are the same, false if they differ
     */
    public boolean equals(InameList nl);

    /**
     * A utility function to return all the names concatenated without spaces
     * @return
     */
    public String getNameWithoutSpaces();

    /**
     * Obtains a word from the name in a particular location. The first word is in location 0
     * @param index
     * @return a WordInName instance or null if thee is no name
     */
    public WordInName getWord(int index);
    
    /**
     * FirstToLast moves the first word in the name into the final position and
     * moves all the others up one position.  It is intended to be used when names are
     * represented surname first.  For example   "Smith, John Paul" will convert to "John Paul Smith"
     */
    public void firstToLast ();
    
    /**
     * Obtain the last word in the name
     * @return the last word as a string
     */
    public String getLastWord();

    /**
     * Obtain the first word in a name
     * @return the first word as a string or null
     */
    public String getFirstWord();

    /**
     * Return the length of the list, that is the number of words in the name
     * @return an integer representing the count of words in the name
     */
    public int length();

    public void fixSpaces(InameList testNameList);

    public String getName();
    
    
    /**
     * applies dictionary and bracket stripping etc
     * 
     */
    public void preprocess();
    
}
