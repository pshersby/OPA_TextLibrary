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
import com.pks.names.extension.pojo.NameCompare.NameMatch;


/**
 * <p>A class function intended to check if two provided names appear to be generally
 * acceptable as matching, this is an extension of CheckNameCompatibility specifically designed for bank account name matching. </p>
 * This class is intended to check names in the context that a person
 * has provided their name and the name of a bank account that is supposed to belong to them. 
 * because they may be used in differing contexts, they may vary slightly. E.g. a person uses a preferred name or the
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
public class CheckBankNameCompatibility extends CheckNameCompatibility {
    /**
     * Compare a persons/or organisations name with the name of a bank account that is asserted to be their bank account.
     * This utilises a Name checking algorithm that checks for perfect matches, typos and similar.  It then considers if the entity is a person or thing and
     * looks at use of initials and shortened names for people, for organisations it looks at common industry abbreviations and applies stemming rules
     *
     * @param name      the name of the party to be compared. 
     * @param bankAccountName  the name of the bank account belonging to the party
     * @param entityDescription   the Description of the type of entity - "INDIVIDUAL" or "PARTNER" have particular rules, other types may be entered but are all treated as "inanimate things"
     * @param optionalSuffixToRemove   sometimes names may have suffixes, for example the bank may suffix an individual account with the word "BUSINESS" or "BUSINESS ACCOUNT",   this option allows it to be ignored
     * @return  true if the names reasonably match, false if not,  null if there is an parameter error 
     */
    public Object evaluate(String name, String bankAccountName, String entityDescription, String optionalSuffixToRemove) {
        
       //All Parameters are needed although the entityDescription and the optionSffixToRemove may be empty 
       if (name==null || name.isEmpty()) return null;
       if (bankAccountName==null || bankAccountName.isEmpty()) return null;
       if (entityDescription==null || entityDescription.isEmpty()) return null;
       if (optionalSuffixToRemove==null) return null;
       
       
       
       
       //Convert Entity Strings into the format used by the POJOs
       int entityType = 0;
       if (entityDescription.equals("PERSON") ||entityDescription.equals("INDIVIDUAL")){entityType =1;}
       if (entityDescription.startsWith("PARTNER")){entityType =2;}
        
       
       NameMatch result = new NameCompare().compareNames(name, bankAccountName, entityType, true);
       

       // Suffix stripping
       //Now Since this is a bankAccountName, if the param 3 is not an empty string and the result is currently DIFFERS then
       //we need to retry without the exclusioon string on the end
       String bank1 = bankAccountName;
             
        if (result==NameMatch.DIFFERS && !optionalSuffixToRemove.isEmpty() && bankAccountName.length()>optionalSuffixToRemove.length()) {
            if (bankAccountName.endsWith(optionalSuffixToRemove)){
                bank1 = bankAccountName.substring(0,bankAccountName.length()-optionalSuffixToRemove.length());
                result = new NameCompare().compareNames(name, bank1, entityType, true);
            }

        }
        
        
        // These attempt the removal of partner, partnership for partnerships!
        if (result == NameMatch.DIFFERS && entityType == 2)
        {
            result = partnerCheck(name, bankAccountName, true);
        }
        if (result == NameMatch.DIFFERS && entityType == 2)
        {
            result = partnerCheck(name, bank1, true);
        }
         
        if (result==NameMatch.MATCH)return true;
        if (result==NameMatch.DIFFERS)return false;
        return null;
        
    }
    
}
