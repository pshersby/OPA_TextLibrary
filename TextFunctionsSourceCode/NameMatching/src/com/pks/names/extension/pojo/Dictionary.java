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
 * This is a dictionary provider for the name comparison functions
 * it is the default dictionary and can be replaced if needed at run time.
 * However since OPA must be thread-safe, the dictionary can only be assigned at the moment the containing object creates the original instance.  
 * It may not be changed later therfore it may be statically coupled.
 * For efficienty this implementation uses static methods however the calling standards allow for either static or instanced,
 *
 * The dictionary contains stop-words that are to be ignored and common usage abbreviations
 * @author pshersby
 */
public class Dictionary {
    
    public static enum Location  {
        FIRST_WORD, LAST_WORD, MID_WORD, PERSON_END_WORD
    }
   
    /**
     * Returns a substitute word in place of the original if one is known.
     * It can substitute first or last words or middle words using separate lists.
     * If looking to substitute a first word or last word, and it does not find a sustitue it
     * will look in the more general word list for an alteration that can take place regardless of position.
     * 
     * @param originalWord the original word to lookup
     * @param locationOfword - specifies enumeration that tells this lookup what 
     * the position of the word within the phase: FIRST_WORD, LAST_WORD, MID_WORD
     * @return the new word
     */
    public static final String getSubstitute(String originalWord, Location locationOfword){
        String[][] WordList;
        String found = originalWord;
        switch (locationOfword) {
            case FIRST_WORD:
                WordList = FRONT_LIST;
                found = lookup( originalWord,  WordList);
                break;
            case LAST_WORD:
                WordList = END_LIST;
                found = lookup( originalWord,  WordList);
                break;
            case PERSON_END_WORD:
                WordList = BANK_END_LIST;
                found = lookup( originalWord,  WordList);
                break;
        }
        
        //Having optionally looked for a front or back replacment we now
        //look for an "any word" replacment only if we have not already
        //changed the word
        
        if (found.equalsIgnoreCase(originalWord)) {
            found = lookup( originalWord,  WORD_LIST);
        }
        return found;
        
     
        
    }
    
    
    //This list of words handles any word in the phrase including those at start and end and in the middle
    private static final String[][] WORD_LIST = {
        {"AND",""},
        {"THE",""},
        {"OF",""},
        {"MR",""},
        {"MRS",""},
        {"MX",""},
        {"MS",""},
        {"SIR",""},
        {"DR",""},
        {"DAME",""},
        {"MISS",""},
        {"MADAM",""},
        {"MESSRS",""},
        {"RT",""}, {"HON",""},
        {"ACTIVY","ACTIVITY"},
        {"ACTIVTY","ACTIVITY"},
        {"ANDY","ANDREW"},
        {"ACCT", "ACCOUNT"},
        {"ASSN","ASSOCIATION"},
        {"ASSOC","ASSOCIATE"},
        {"CATH","CATHERINE"},  {"CATHY","CATHERINE"},
        {"CHRIS","CHRISTOPHER"},{"CRIS","CHRISTOPHER"},
        {"CHRTD","CHARTERED"},  {"CHTD","CHARTERED"},
        {"CO","COMPANY"}, {"COMP","COMPANY"},{"COMPNY","COMPANY"},
        {"COMMS","COMMUNICATION"},
        {"COMPNY","COMPANY"},
        {"ADV", "ADVANCED"},
        {"AGCY", "AGENCY"},
        {"ARCHIT", "ARCHITECTURE"},
        {"BEHAV","BEHAVIOUR"},
        {"BLDG", "BUILDING"},
        {"CATER", "CATERING"},
        {"CHEM", "CHEMICAL"},
        {"CLIN", "CLINIC"},
        {"CONSTR", "CONSTRUCTION"},
        {"CONSULT","CONSULTING"},{"CONSULTANT","CONSULTING"},{"CONSULTANTS","CONSULTING"},
        {"CONTR","CONTRACT"},
        {"COOP","COOPERATIVE"},
        {"CENTER","CENTRE"},
        {"DEPT","DEPARTMENT"},
        {"DIV", "DIVISION"},
        {"ENG","ENGINEER"}, {"ENGINEERING", "ENGINEER"}, {"ENGINE", "ENGINEER"},{"ENGINEERS", "ENGINEER"},
        {"ENT", "ENTERPRISE"},
        {"FAB", "FABRICATION"},{"FABRICATES", "FABRICATION"}, {"FABRICATORS", "FABRICATION"},
        {"FIN", "FINANCE"},{"FINANCING", "FINANCE"}, {"FINANCIAL", "FINANCE"},
        {"GEN", "GENERAL"},
        {"GEO", "GOLOGICAL"},
        {"GP", "GROUP" }, {"GRP", "GROUP"},
        {"INCORP","INCORPORATED"},
        {"IND", "INDUSTRIAL"},{"INDUSTRY", "INDUSTRIAL"},
        {"III", "THIRD"},{"3RD", "THIRD"},
        {"II", "SECOND"},{"2ND", "SECOND"},
        {"1ST","FIRST"},
        {"INST", "INSTITUTE"},{"INSTITUTIONAL", "INTITUTE"},
        {"INSUR", "INSURANCE"},
        {"INTL", "INTERNATIONAL"},
        {"LIAB", "LIABILITY"},
        {"MACH", "MACHINE"},{"MACHINERY","MACHINE"},
        {"MAINT", "MAINTENANCE"},
        {"MGMT", "MANAGMENT"},
        {"MKT", "MARKETING"},{"MKGT", "MARKETING"}, {"MARKET", "MARKETING"},
        {"NATL", "NATIONAL"},
        {"OPS", "OPERATIONS"},
        {"PERS", "PERSONNEL"},
        {"PETRO", "PETROLEUM"},
        {"PROF", "PROFESSIONAL"},
        {"PHOTO", "PHOTOGRAPHIC"},{"PHOTOGRAPHER", "PHOTOGRAPHIC"},{"PHOTOGRAPHY", "PHOTOGRAPHIC"},
        {"PRIV", "PRIVATE"},
        {"PROD", "PRODCUT"},
        {"PRJ", "PROJECT"},{"PROJ", "PROJECT"},
        {"PRTG", "PRINTING"},
        {"REHAB", "REHABILITIATION"},
        {"REP", "REPRESENTATIVE"}, {"REPS", "REPRESENTATIVE"},
        {"SCI", "SCIENCE"}, {"SCIENTIFIC", "SCIENCE"},
        {"SPLY", "SUPPLY"},
        {"STA", "STATION"},
        {"TECH", "TECHNOLOGY"},{"TECHNOLOGICAL", "TECHNOLOGY"}, {"TECHNICAL", "TECHNOLOGY"},
        {"TRANS", "TRANSPORT"},{"TRANSPORTATION","TRANSPORT"},
        {"TRNG", "TRAINING"},
        {"CORP", "CORPORATION"},
        {"CORPORATE", "CORPORATION"},
        {"DEPT", "DEPARTMENT"},
        {"ELEC", "ELECTRIC"}, {"ELECTRICAL", "ELECTRIC"},
        {"ROB", "ROBERT"}

    };
    
    //This list of words is only altered is we are comparing the first
    //word of a phrase
    public static final String[][] FRONT_LIST = {
        {"LORD", ""},
        {"PROF", ""},
        

        };

    //This list of words is only altered is we are comparing the last
    //word of a phrase
        private static final String[][] END_LIST = {
            {"LI",""},
            {"TD",""},
            {"LTD",""},
            {"PLC",""},
            {"LLP",""},
            {"INC",""},
            {"LLC",""},
            {"KTD",""},
            {"LTS",""},
            {"LYD",""},
            {"JR",""},
            {"JNR",""},
            {"SNR",""},
            {"GMBH",""},
            {"LTDS",""},
            {"LIMIT",""},
            {"LIMITED",""},
            {"IMITED",""},
            {"LIMITD",""},
            {"LIMIED",""},
            {"JUNIOR",""},
            {"SENIOR",""},
            {"INCORP",""},
            {"UNLTD",""},
            {"INCORPORATED",""}
                
        };
    
    private static final String[][] BANK_END_LIST = {
            {"LI",""},
            {"TD",""},
            {"LTD",""},
            {"PLC",""},
            {"LLP",""},
            {"INC",""},
            {"LLC",""},
            {"KTD",""},
            {"LTS",""},
            {"LYD",""},
            {"JR",""},
            {"JNR",""},
            {"SNR",""},
            {"GMBH",""},
            {"LTDS",""},
            {"LIMIT",""},
            {"LIMITED",""},
            {"IMITED",""},
            {"LIMITD",""},
            {"LIMIED",""},
            {"JUNIOR",""},
            {"SENIOR",""},
            {"INCORP",""},
            {"UNLTD",""},
            {"INCORPORATED",""},
            {"BUSINESS",""},
            {"ACCOUNT",""},
            {"AC",""},
            {"CONSULTING",""}
    };
    
    /**
     * Returns an alternative standardised format word for 
     * specific word that are often abbreviated or for which there are common alternatives
     * miss-spelling are not usually corrected provided they are phonetically similar or
     * are just one character or interchanged characters.
     * 
     * Implementation is brute force lookup since the table is small and in-memory,
     * if it grows then an alternative indexed or tree strategy might be needed
     * @param original
     * @return new replacement word
     */
    private static String lookup(String original, String[][] WordList){
        int dictionaryLength = WordList.length;
        boolean found = false;
        int f=0;        
        while (f<dictionaryLength && !found){
            
            if (WordList[f][0].equalsIgnoreCase(original)) {
                found = true;
            }
            else { f++; }
        }
        
        return found?WordList[f][1]:original;
 
    }
    
    
}
