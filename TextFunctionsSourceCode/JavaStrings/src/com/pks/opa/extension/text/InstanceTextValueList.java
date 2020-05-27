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
import com.oracle.determinations.engine.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * An OPA custom function Extension class that iterates an enetiy via a relationship
 * selecting and adding strings found to a list.
 */
public class InstanceTextValueList extends CustomFunction{

    /**
    * An OPA custom function Extension  that iterates a relationship members building a list 
    * from known non-empty strings with delimiter.  

 * <p>Usage:  text_attribute = AssembleName(text, text, text)</p>
     * @param ei the entity
     * @param os 0 = The name of the  relationship
     * @param os[1] = the name of the attribute.  
     * @param os[2] = the delimiter
     * @return 
     */
    @Override
    public Object evaluate(EntityInstance ei, Object[] os) {
      
        String relationshipName = os[0].toString().trim();
        String attributeName = os[1].toString().trim();
        String delimiter = os[2].toString().trim();
        Double qty = (Double)os[3];
        
        Entity ent = null;
        Entity currEntity = null;
        Relationship rel = null;
        List<EntityInstance> targets;
        List<Relationship> relationships;
        List<Attribute> attributes;
        
        String result = "";
        
        // First we need to get the entity for the entity instance that is our scope
        // We can also attempt to resolve the exact relationship by public name
        ent = ei.getEntity();
        relationships = ent.getRelationships();
        try{
            rel = ent.getRelationship(relationshipName);
        } catch (Exception e){}
        
        // Assuming we didn't find the relationship by public name we itterate to find the right one
        try{
            if (rel ==null){
                for (Relationship possibleRelationship : relationships){
                    if (possibleRelationship.getText().equals(relationshipName)) rel = possibleRelationship;
                }
            }
        } catch (Exception e){}
        // Failed to resolvbe relationship - get out of here
        if (rel==null) return "";
        
        
        //Now we need to find a list of entity Instances that are all the know instances 
        //on the far end of the relationship for our specific entity innstance start point
        targets = rel.getKnownTargets(ei);
         
        // Now we iterate to get he attribute per entity ionstance to process it
        for (EntityInstance targetEntity : targets) {
            currEntity = targetEntity.getEntity();
            
            //First attempt to get attribute hoping we got the public attribute name
            Attribute currAttr = null;
            try{
                currAttr = currEntity.getAttribute(attributeName);
            } catch (Exception e){}
            
            
            // if we didn't fiind by public name lets see if we can get it using sentence form
            if (currAttr == null){
                attributes = currEntity.getAttributes();
                Iterator<Attribute> iAttributes = attributes.iterator();  // using iterator because easy to exit early
                
                while(currAttr == null && iAttributes.hasNext()){
                    Attribute a = iAttributes.next();
                    if (a.getText(SentenceForm.BASIC).equals(attributeName)) currAttr = a;
                }
            }
            
            // If I can't find the attribute then exit
            if (currAttr == null) return "";
            
            
            // Make the list
            Object attributeValue = currAttr.getValue(targetEntity);
            if (attributeValue != null && attributeValue != Uncertain.INSTANCE   ){
                if (result.isEmpty()) {
                    result= attributeValue.toString();
                }
                else {
                    result += (delimiter + attributeValue.toString());
                }    
            }
                    


        }
        
    return result;       
    }

    
}
