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
package com.pks.names.extension.pojo;





/**
 * Important note:  This class is instantiated as a singleton by the OPA runtime and
 * stays loaded therefore all methods must be thread-safe.   This should not be a
 * problem provided that all variables are created locally within functions.
 * 
 * This class facilitates checking if two names that are asserted to be the same are in
 * fact a reasonable match.  They do not need to match perfectly but a sensible person might 
 * consider them to be the same person.
 * 
 * This class is written using reference standard Java  (SE 1.6) to avoid dependencies
 * and ensure maximum cross platform compatibility.  Deprecated and optional features are 
 * avoided.
 * 
* 
 * the return value is a NameMatch enumeration  
 * MATCH There is a good match allowing for minor spelling errors, initials etc
 * DIFFERS  There is no acceptable match
 * UNCERTAIN it is not certain either way 
 * 
 * The aim is that MATCH and DIFFERS should represent sufficient to make a decision 
 * that is communicated without human checking whereas uncertain cases are worth human checks or
 * discretion. 
 *  
 * When comparing an individual then #
 *      the surname must match fully (surname must be at the end) allowing for mis-spelling
 *      Names are expected in the same order but some (not first) may be missed out
 *      and may be abbreviated or initials and these must align in sequence.
 *      
 * When comparing a business name then, 
 *   if there is ltd or suchlike then an organisational identifier must appear in both
 *   All words must be similar in a similar order
 * 
 * When comparing multi-person then
 *   The people are separated at commas or the word and
 *   Those with initials only or just one name, pick up the surname from the
 *   next person who has a surname.   
 *      The name count should match
 *      Each name in the primary must have a match in the test name
 *      There must be no non matching names in either left at the end (i.e. a 1:1 match)
 * 
 * Making allowance for miss-spelling, common replacements, Use of initial, alternative stems etc
 * @author pshersby
 */



public class NameCompare {
    
    /**
     * An enumeration used to represent the name match result
     */
    public static enum NameMatch {
        MATCH, DIFFERS, UNCERTAIN
    }

    /**
     * A string used to represent the various formats of trading name tokens
     */
    public static final String[] TRADENAME_PHRASES = {
    " TA ", " T/A ", " T/AS ", " TRADING AS ", " TRD AS ", " T.A. ", " T.A "};
    
    
    /**
     * Compare two names
     * @param primary  The name of the entity or thing
     * @param secondary the name of the item claimed to have a matching name
     * @param EntityType the type  (0 - inanimate/organisation, 1 = a real person, 2 a group of people)
     * @return a nameMatch enumeration  MATCH = match satisfactory,  DIFFERS - not sufficiently similar (reject), UNCERTAIN = might be the same
     */
    public NameMatch compareNames(String primary, String secondary, int EntityType){
    return compareNames( primary,  secondary,  EntityType,  false) ;
    }
    
    /**
     * Compare two names
     * @param primary  The name of the entity or thing
     * @param secondary the name of the item claimed to have a matching name
     * @param isPerson true if the names relate to a real person, false if a thing or organisation
     * @return a nameMatch enumeration  MATCH = match satisfactory,  DIFFERS - not sufficiently similar (reject), UNCERTAIN = might be the same
     */        
    public NameMatch compareNames(String primary, String secondary, boolean isPerson){
    return compareNames( primary,  secondary,  isPerson?1:0, false) ;
    }
    
        /**
     * Compare two names
     * @param primary  The name of the entity or thing
     * @param secondary the name of the item claimed to have a matching name
     * @param EntityType the type  (0 - inanimate/organisation, 1 = a real person, 2 a group of people)
     * @param isBank  true if we are comparing a bank account (additional rules apply)
     * @return a nameMatch enumeration  MATCH = match satisfactory,  DIFFERS - not sufficiently similar (reject), UNCERTAIN = might be the same
     */
    
    public NameMatch compareNames(String primary, String secondary, int EntityType, boolean isBank) {
        //Sanity Checks and ensuring we have safe parameters
        boolean isPerson = (EntityType==1);
        
        if (primary.isEmpty() || secondary.isEmpty()) return NameMatch.UNCERTAIN;
        if (primary.equalsIgnoreCase(secondary)) return NameMatch.MATCH;
        

        //Extract the basic names from the inputs  these have to be parsed 
        //because they may be a combination of a name and a trading name
        //We don't worry about spaces because we will get rid of these when we turn
        //them into lists of individual words for comaprison
        String primaryName = extractTradingName (primary,TRADENAME_PHRASES)[0].toUpperCase();
        String primaryTradingName = extractTradingName (primary,TRADENAME_PHRASES)[1].toUpperCase();
        String testName = extractTradingName (secondary,TRADENAME_PHRASES)[0].toUpperCase();
        String testTradingName = extractTradingName (secondary,TRADENAME_PHRASES)[1].toUpperCase();
   
                
        //Check for a quick win situations
        //Note that if the trading names match each other (could be blank) then
        //the name prefixing this must also match so John Smith Trading as 
        //Chips does not incorrectly match Peter trading as Chips
        if (primaryName.equalsIgnoreCase(testName)) { return NameMatch.MATCH;  } 
        if (primaryName.length()>8 && testName.length()<3&& testName.length()>0) {return NameMatch.DIFFERS;}
        if (primaryTradingName.equalsIgnoreCase(testName)) { return NameMatch.MATCH;  }
        if (primaryName.equalsIgnoreCase(testTradingName)) { return NameMatch.MATCH;  }
        
      
        //There can be a problem comparing a single name f a person with a group 
        //of people so we handle this using the presence of "and"
        //
        if (isPerson){
            if ((primaryName.contains(" AND ") || primaryName.contains("&"))!= (testName.contains(" AND ") || testName.contains("&"))) {return NameMatch.DIFFERS;}
        }
        
        // We convert the 4 possible names into name word lists
        //during this there is automatic removel of non letter/digit characters an
        // also word subsitution
        
            InameList primaryNameList = null;
            InameList testNameList = null;
            InameList primaryTradingAsList =null;
            InameList testTradingAsList = null;
        
        //set up a loop that conditionally re-trys all the tests without a bracketted word
        // note the primary name or trading contains the bracket not the bank name and it only
        //applies to bank checks
        int bracketTestLimit = 0;
        if (!isPerson && primaryName.contains("("))bracketTestLimit = 1;
        int bracketStripper ;
        for (bracketStripper = 0; bracketStripper <=bracketTestLimit; bracketStripper ++){
            
        
            int f;
            String spaceList = " -&";
            // This loop controlls multiple iterations with slight changes 
            // 1 = alternative hyphen handling
            // 2 = revese surnname order if a person
            for (f = 0; f<4; f++) {  
                spaceList = " -&";
                if (f ==1 || f ==3 ) spaceList = " -&";


                //we should really have a nameListFactory serve up the right type
                if (isBank) {
                     primaryNameList = new BankNameList(primaryName, spaceList, isPerson, bracketStripper==1) ;
                     testNameList = new BankNameList(testName, spaceList, isPerson, false);
                     primaryTradingAsList =  new BankNameList(primaryTradingName,spaceList, false, bracketStripper==1);
                     testTradingAsList =  new BankNameList(testTradingName,spaceList, false, false);
                }
                else
                {
                     primaryNameList = new NameList(primaryName, spaceList, isPerson ,false) ;
                     testNameList = new NameList(testName, spaceList, isPerson ,false);
                     primaryTradingAsList =  new NameList(primaryTradingName, spaceList, false ,false);
                     testTradingAsList =  new NameList(testTradingName, spaceList, false ,false);
                }

                if (isPerson &&  testName.startsWith(primaryNameList.getWord(primaryNameList.length()-1).getWord()) &&(f== 2 || f ==3)) {
                    testNameList.firstToLast();
                    // note we assiume the surnmame is on the end of the bank name and move it to the end

                }
 

                // Finally we need to do the comparisons.  This is done in the
                // Name List itself since it should be able to compare itself to another list.


                if (primaryNameList.acceptableMatch(testNameList)) return NameMatch.MATCH; 
                if (primaryNameList.acceptableMatch(testTradingAsList)) return NameMatch.MATCH;
                if (primaryTradingAsList.acceptableMatch(testNameList)) return NameMatch.MATCH;
                if (primaryTradingAsList.acceptableMatch(testTradingAsList)) return NameMatch.MATCH;
            } // end of for loop
        } //end of bracketstripper
        
       //The defult is that the names differ 
       return NameMatch.DIFFERS;
       
    }
    
  
    

    /**
     * This locates synonyms for trading as within the word lists and
     * splits the list on either side, getting rid of the trading as bit.
     * This might in fact make one of the lists empty!
     * The list passed in as a parameter is changed after the call.
     * 
     * 
     * @param originalName
     * @param phrases  the various strings representing "trading as"
     * @return a pair of strings.  The one at positi
     */
    public String[] extractTradingName(String originalName, String [] phrases) {
        String[] result = {originalName,""};
        int p = -1;
        int f;
        if (originalName.length()<6) { return result; }  // too short to have trading as
        
        
        for (f=0; f<phrases.length && p==-1; f++) {
            p = originalName.indexOf(phrases[f], 3);
            if (p >=0){
                result[0] = originalName.substring(0, p);
                result[1] = originalName.substring(p+phrases[f].length());
            }
        }
        
        return result;
        
        
    }
    
    
     private boolean startsWith (StringBuffer MainString, String testString) {
        return MainString.indexOf(testString)==0;
    }   

   
    
}
