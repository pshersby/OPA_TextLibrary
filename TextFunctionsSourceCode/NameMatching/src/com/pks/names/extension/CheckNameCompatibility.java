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
package com.pks.names.extension;


import com.pks.names.extension.pojo.NameCompare;
import com.pks.names.extension.pojo.NameCompare.*;
/**
 * <p>A class function intended to check if two provided names appear to be generally
 * acceptable as matching,  </p>
 * This class is intended to check names in the context that a person or entity
 * has provided their name and the name something that is supposed to belong to them. 
 * Because they may be used in differing contexts, they may vary slightly. Prime examples might be
 * that the name on a passport is formal and correct, the person however uses a preferred name or the
 * name as a matter of habit on other documents, or misses the middle name or uses an initial.  Extending this to businesses then
 * the name may be merged with a trading name e.g. J Smith trading as Smiths Crisps.

 * <p> Originally designed as an OPA extension this class may be invoked as follows:
 * 
 * Firstly note that this uses a default constructor that takes no parameters and that
 * it is designed as thread-safe. YOu may therefore invoke by:</p>
 * 
 * <ul>
 * <li> Create a new instance each time it is needed and call the "evaluate" method to compare the names</li>
 * <li> Have a container create a singleton new instance and cache it,  call the 
 * "evaluate" method on this stored instance to compare the names</li>
 * </ul>
 * 
 * The OPA runtime uses this second method and it is most efficient if deployed in a application server or web service environment
 * 
 * @Return true, false, null.   Null is returned in the event that the input data cannot be processed e.g. only one name given
 * this behaviour stems from the OPA origins in which the framework automatically provided validation of inputs and handled missing data.  
 * It is not permitted for an OPA custom function to throw an exception (null is therefore the exception state)
 * @author pshersby
 */
public class CheckNameCompatibility {
    /**
     * Compare a persons/or organisations name with the name of a an object bearing their name,  that is asserted to be their property.
     * This utilises a Name checking algorithm that checks for perfect matches, typos and similar.  It then considers if the entity is a person or thing and
     * looks at use of initials and shortened names for people, for organisations it looks at common industry abbreviations and applies stemming rules
     *
     * @param name the name of the person or entity
     * @param nameToComapare the name that is stated as being the same.  
     * Note that the name of the entity goes in "name" the name of anything that is "owned" e.g. 
     * the passport name, the bank account name... goes in this parameter
     * @param isPerson,  true if the name is for a real person, false if for a "thing" or organisation
     * @return  true if the names reasonably match, false if not,  null if there is an parameter error
     */

    public Object evaluate(String name, String nameToComapare, boolean isPerson ) {
        
       //Must have names
       if (name==null || name.isEmpty()) return null;
       if (nameToComapare==null || nameToComapare.isEmpty()) return null;

        
        NameMatch result = new NameCompare().compareNames(name, nameToComapare, isPerson);
         if (result == NameCompare.NameMatch.DIFFERS && isPerson)
            {
                result = partnerCheck(name,  nameToComapare, isPerson);

            }

        
        if (result==NameMatch.MATCH)return true;
        if (result==NameMatch.DIFFERS)return false;
        return null;
        
    }
     //Extra Checks for partners
        public NameMatch partnerCheck(String name, String name2, boolean isPerson)
        {
           
            if (name.endsWith("PARTNERSHIP"))
            {
                NameMatch result = new NameCompare().compareNames(name.substring(0, name.length() - 11), name2, isPerson);
                if (result == NameMatch.MATCH) return result;
            }
            if (name.endsWith("PARTNERS"))
            {
                NameMatch result = new NameCompare().compareNames(name.substring(0, name.length() - 8), name2, isPerson);
                if (result == NameMatch.MATCH) return result;
            }
            if (name2.endsWith("PARTNERSHIP"))
            {
                NameMatch result = new NameCompare().compareNames(name, name2.substring(0, name2.length() - 11), isPerson);
                if (result == NameMatch.MATCH) return result;
            }
            if (name2.endsWith("PARTNERS"))
            {
                NameMatch result = new NameCompare().compareNames(name, name2.substring(0, name2.length() - 8), isPerson);
                if (result == NameMatch.MATCH) return result;
            }

            return NameMatch.DIFFERS;

        }
    
}
