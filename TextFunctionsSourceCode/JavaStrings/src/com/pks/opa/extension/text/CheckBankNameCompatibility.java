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

import com.oracle.determinations.engine.EntityInstance;
import com.oracle.determinations.engine.Uncertain;
import com.pks.opa.extension.text.pojo.NameCompare;
import com.pks.opa.extension.text.pojo.NameCompare.*;
/**
 * An OPA Custom function intended to check if two provided names appear to be generally
 * acceptable as matching.  This is intended to check names in the context that a person
 * has provided names on two parts of the form that are supposed to represent the same thing however
 * because they may be used in differing contexts, they may vary slightly. Prime examples might be
 * that the name on a passport is formal and correct, the person however uses a preferred name or the
 * name as a matter of habit on other documents, or misses the middle name or uses an initial.  Extending this to businesses then
 * the name may be merged with a trading name e.g. J Smith trading as Smiths Crisps.
 * Return true, false, Uncertain
 * @author pshersby
 */
public class CheckBankNameCompatibility extends CheckNameCompatibility {
    /**
     * An OPA Custom function intended to check if two provided names appear to be generally
     * acceptable as matching.  This is intended to check names in the context that a person
     * has provided names on two parts of the form that are supposed to represent the same thing however
     * because they may be used in differing contexts, they may vary slightly. Prime examples might be
     * that the name on a passport is formal and correct, the person however uses a preferred name or the
     * name as a matter of habit on other documents, or misses the middle name or uses an initial.  Extending this to businesses then
     * the name may be merged with a trading name e.g. J Smith trading as Smiths Crisps.
     * Return true, false, Uncertain
     *
     * @param ei
     * @param os
     * @return  */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
        
       String name = os[0].toString().toUpperCase().trim();
       String bank = os[1].toString().toUpperCase().trim();
       String entity = os[2].toString().toUpperCase().trim();
       String exclude = os[3].toString().toUpperCase().trim();
        
       int entityType = 0;
       if (entity.equals("PERSON")){entityType =1;}
       if (entity.startsWith("PARTNER")){entityType =2;}
        
       NameMatch result = new NameCompare().compareNames(name, bank, entityType, true);
       
       String bank1 = bank;
        //Now Since this is a bank, if the param 3 is not an empty string and the result is currently DIFFERS then
        //we need to retry without the exclusioon string on the end
        if (result==NameMatch.DIFFERS && !exclude.isEmpty() && bank.length()>exclude.length()) {
            if (bank.endsWith(exclude)){
                bank1 = bank.substring(0,bank.length()-exclude.length());
                result = new NameCompare().compareNames(name, bank1, entityType, true);
            }

        }
        
        
        // These attempt the removal of partner, partnership for partnerships!
        if (result == NameMatch.DIFFERS && entityType == 2)
        {
            result = partnerCheck(name, bank, true);

        }
        if (result == NameMatch.DIFFERS && entityType == 2)
        {
            result = partnerCheck(name, bank1, true);

        }
         
        if (result==NameMatch.MATCH)return true;
        if (result==NameMatch.DIFFERS)return false;
        return Uncertain.INSTANCE;
        
    }
    
}
