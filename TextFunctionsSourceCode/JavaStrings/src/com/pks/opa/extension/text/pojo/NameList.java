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

import java.util.ArrayList;
import java.util.Iterator;


/**
 * <strong>NameList is a simple ordered list of "wordInName" objects that represent words in some name </strong>
 * Words are added once as a phrase and then exposed through simple functions
 * Words may be returned in a variety of resulting strings to meet different uses.
 * These may be extended readily.
 * @author P.K.Shersby

 */
public class NameList implements InameList 
{

    Dictionary lookupTable ;
    boolean stripBrackets ;
    String phrase;
        
    ArrayList <WordInName> words;
    //Dictionary subsitutes;
    boolean isPerson;
    
    
    
     /**
     * A constructor that takes a phrase and creates a word list
     * @param phrase - the phrase to be broken into words that contain only letters and digits
     * @param dictionaryObject - an instance of a Dictionary class to use instead of default.  May be null to use default dictionary
     * @param spaceList - a string with characters that are treated as spaces, if null then defaults to space, dash and ampersand
     * @param isPerson - a parameter that indicates that matching is to be performed on the basis these are names of a real person
     * this alters behaviour of initials and stemming.
     * @param stripLastBracket - set to true if a last word is to be removed if in brackets.  This is typically set to true if
     * the nature of the name means that it might end with a parenthesised country e.g.  if ABC Fencing (UK) is to match ABC Fencing
     */
    
    public NameList (String phrase, Dictionary dictionaryObject, String spaceList, boolean isPerson, boolean stripLastBracket){
        this.words = new ArrayList <WordInName> () ;

        this.isPerson = isPerson;
        this.splitIntoWords(phrase,words,spaceList);
        if (words.isEmpty()) return;

        // mark first and last words since these impact substition  (in original word list)
        words.get(0).setIsFirstWord(true);
        words.get(words.size()-1).setIsLastWord(true);
 
        this.lookupTable = dictionaryObject;
        this.stripBrackets = stripLastBracket;
        this.phrase = phrase;
     }

        /**
     * A constructor that takes a phrase and creates a word list.  This constructor
     * is deprecated in favour of the longer version that allows injection of a custom dictionary
     * @param phrase - the phrase to be broken into words that contain only letters and digits
     * @param spaceList - a string with characters that are treated as spaces, if null then defaults to space, dash and ampersand
     * @param isPerson - a parameter that indicates that matching is to be performed on the basis these are names of a real person
     * this alters behaviour of initials and stemming.
     * @param stripLastBracket - set to true if a last word is to be removed if in brackets.  This is typically set to true if
     * the nature of the name means that it might end with a parenthesised country e.g.  if ABC Fencing (UK) is to match ABC Fencing
     */
    public NameList (String phrase, String spaceList, boolean isPerson, boolean stripLastBracket){
        this (phrase,null,spaceList, isPerson, stripLastBracket);
    }
    
    
    @Override
    public void preprocess() {
        // here we carry out subsitution of words e.g. abreviations and stop words
        this.substitute(lookupTable,words);
        
        
        // get rid of last word if it is bracketed if the option is chosen
        if (words.size()>1 && stripBrackets && phrase.contains("(" + getLastWord() + ")")) {
            words.remove(words.size()-1);
        }
        words.get(words.size()-1).setIsLastWord(true);
        
    }
    
    
    
    
    /**
     * This function runs through all the words providing dictionary substitution
     * and also removing any blank words that result.
     * @param lookup 
     * @param wordList 
     */
    public final void substitute(Dictionary lookup, ArrayList <WordInName> wordList){
        Iterator <WordInName> i;
        Dictionary.Location l = Dictionary.Location.FIRST_WORD;
        WordInName win;
        i = wordList.listIterator();
        while (i.hasNext()){
            win= i.next();
            if (!isPerson & !i.hasNext() && l == Dictionary.Location.MID_WORD ) l = Dictionary.Location.LAST_WORD ;
            if (isPerson & !i.hasNext() && l == Dictionary.Location.MID_WORD ) l = Dictionary.Location.PERSON_END_WORD ;
            
            win.setWord( (lookup==null)? Dictionary.getSubstitute(win.getWord(), l): lookup.getSubstitute(win.getWord(), l));
            l = Dictionary.Location.MID_WORD;
            if (win.isEmpty()) i.remove();
        }
        
        //Bug fix 26-3-18 for when all the names have been stripped leaving nothing to compare
        // if there are no words left then subsitute with "XXXX NO WORDS XXX"
        if (words.isEmpty()) words.add(new WordInName("XXXX NO WORDS XXX", isPerson));
        
        // need to re-mark first and last words since these have meaning in peoples names
        // they may have changes since substitution may have deleted some words
        words.get(0).setIsFirstWord(true);
        words.get(words.size()-1).setIsLastWord(true);
        
        
    }
    
    

    



    /**
     * <p>Acceptable match compare two names (held in InameList instances) that are expected to
     * represent the same person albeit they may have formatting differences arising from 
     * different domains of usage. e.g. a bank account name may use different naming standards 
     * than a club membership.
     * </p> <p>
     * This particular implementation performs the following, all not case sensitive:
     * <ul>
     * <li> equality </li>
     * <li> equality test if spaces removed, in case a space was omitted in data input </li>
     * <li> Missing middle initials case for people only, but first must match and last name must match and all provided intiials in order </li>
     * <li> non-person name count matches </li>
     * <li> people must match on 2 or more names </li>
     * <li> words must be in same order and each word must be a good match (not necessarily perfect),  
     * the definition of a good match is set in the WordInName implementation which has word comparators.</li>
     * <li> a special case for individuals in which initials are merged in one name but not the other</li>
     * 
     * </ul></p>
     * 
     * @param nl :  the nameList against which to compare.  This object
     * should be the person/thing clearest identifier and the parameter should be the candidate.
     * @return true if the match is acceptable or the same, false otherwise
     */
    @Override
    public boolean acceptableMatch (InameList nl) {
        boolean matchSoFar = true;
        if (this.length()==0 || nl.length()==0 ) return false;
        
        
        
        // quick tests
        // If they are typographically the same then they match!
        if (this.equals(nl)) return true;
        if (this.getNameWithoutSpaces().equals(nl.getNameWithoutSpaces())) return true;
        
        
        //Special CAse that a person might have formatted initials differently aaround missed middle names
        // E.g.   JA Smith should match John Andrew Smith OR John Smith but not John Big Smith
        if (isPerson && this.length()==2 && nl.length() >=2  && this.getLastWord().equals(nl.getLastWord())) {
            if (this.getFirstWord().length()<3) {
                int g;
                String testString = "";
                for (g = 0; g<nl.length()-1; g++){
                    testString+= nl.getWord(g).getInitial();
                }
                if (this.getFirstWord().startsWith(testString)) return true;
            }
        }
        
        
        
        //If we are testing a person and one only has one name and the other more then
        // we won't be able to find a good match
        if (this.isPerson && this.length()<2 && nl.length()>=2) return false;
        if (this.isPerson && this.length()>=2 && nl.length()<2) return false;
        // If we re testing something not a person and they differ in length then they dont match
        if (!this.isPerson && this.length() != nl.length()) return false;
        
        // Having gone through the basic eliminations then we move on to more
        // complicated tests that will differ depending if we are dealign with a person or a thing.
        
        // First we compare as though we are lookign a "things" because this also works for people
        // The basic test is we expect all the same words in the sdame order and each word should be a
        //good match although not necessarily perfect.  At this point we ignore initial checks
        // this also applies if the workds like "limited" have been put ont he last word without a space, because
        //"limited" will have been stripped off one making them the same length"
        
        int f;
        if (this.length()== nl.length()){
            for (f=0; f<words.size(); f++){
                if (words.get(f).extentOfMatch(nl.getWord(f))== WordInName.MATCH_TYPE.NONE) matchSoFar = false;
            }
            return matchSoFar;  // we can exit here because they must have been the same length therefore
                                // irrespective of person or thing then the words should match int he same order
  
        }
        
        // Here we know we have candidates that are differing lengths and that
        // we are dealing with a person not a thing!
        // There must be at least 2 names in both because the single name comparison is already done
        

        InameList shortestName = (this.length() <= nl.length())? this : nl;
        InameList longestName = (this.length() > nl.length())? this : nl;
        
        //A special case for individuals is that the last name is the same and the first word in the shortest is
        //a concatenation of the firs n initials of th elongest!  e. John Big Smith  = JB Smith
        if (this.isPerson && shortestName.length()< longestName.length() && shortestName.length()==2){
            int initialsToCount = longestName.length()-1;
            
            String combinedInitials = "";
            for (f=0; f<initialsToCount; f++){
                combinedInitials += longestName.getWord(f).getInitial();
            }
            if (shortestName.getWord(0).equalsIgnoreCase(combinedInitials) && shortestName.getWord(1).equalsIgnoreCase(longestName.getWord(initialsToCount)) ) {return true;}
            
        }
        
        
        // Cannot match if first name differs or last name differs
        if (words.get(0).extentOfMatch(nl.getWord(0))== WordInName.MATCH_TYPE.NONE) return false;
        if (words.get(this.length()-1).extentOfMatch(nl.getWord(nl.length()-1))== WordInName.MATCH_TYPE.NONE) return false;
        
        int lastFoundPosition = 0;
        int g;
        boolean matchword = false;
        // Now check the middle bits!
        for (f=1; f<shortestName.length()-1; f++) {
            matchword = false;
            for (g=lastFoundPosition+1; g<nl.length()-1 && !matchword; g++) {  // compare the name against the remaining bit of the nl.
                if (words.get(f).extentOfMatch(nl.getWord(g))!=WordInName.MATCH_TYPE.NONE){
                    lastFoundPosition = g;  // shorten nl
                    matchword = true;
                }
            } //for g loop 
            if (!matchword) return false; // I didn't find a match for a word
        } // for f loop
        // if we got here then
        // we know forst and last match and all the bits of the shortest appear
        // in the right order within the longest.  So we have to conclude that the names 
        // actually match!!!!
        
        return true;
 
    }

     /**
     * Obtain the last word in the name
     * @return the last word as a string
     */
    @Override
    public String getLastWord(){
        if (words.isEmpty()) return null;
        return words.get(words.size()-1).getWord();
    }
    
     /**
     * Obtain the first word in a name
     * @return the first word as a string or null
     */
    @Override
    public String getFirstWord(){
        if (words.isEmpty()) return null;
        return words.get(0).getWord();
    }
    
    /**
     * Move the first name to the last position.  used if the surname is presented first
     * e.g. Smith, John Paul  Changes to John Paul Smith
     */
    @Override
    public void firstToLast () {
        String firstword = words.get(0).getWord();
        int f;
        for (f = 0; f <words.size()-1; f++){
            words.get(f).setWord(words.get(f+1).getWord());
        }
        words.get(words.size()-1).setWord(firstword);
        
    }

    

    
      /**
     * This function is used to split up a string into words.  We could
     * use a tokenizer but this approach is fairly efficient and easy to configure
     * and also easy to replicate into C#
     * @param phrase
     * @return an ArrayList containing the extracted words
     */
    private void splitIntoWords(String phrase, ArrayList <WordInName> wordList, String spaceList){
 
        int length = phrase.length();
        int f;
        StringBuilder extractedName = new StringBuilder();
        boolean wordDone = false;

        //Improvement Dec 2019 People are missing out spaces around commas and full stops so
        //put in the space 
        phrase = phrase.replace(",", ", ");
        phrase = phrase.replace(".", ". ");
        
        //Iterate throught the name removing any nasty characters along the way and
        //Converting to upper case to improve comparative checks later
        for (f=0; f<length; f++){
            if (Character.isLetterOrDigit(phrase.charAt(f))){
                extractedName.append(Character.toUpperCase(phrase.charAt(f)));
                wordDone=false;
                if (f == length-1) wordDone = true;
            }
            else {  // We have not found a character we like! but is it a spacer or to be ignored
                if (f == length-1 ||Character.isWhitespace(phrase.charAt(f)) || spaceList.indexOf(phrase.charAt(f))>=0) 
                {
                    wordDone=true;
                }
            }
   
            if (wordDone && extractedName.length()>0) {
                wordList.add(new WordInName(extractedName.toString(), this.isPerson));
                    extractedName.delete(0, extractedName.length());
            }

        }
    }

       /**
     * Return the length of the list, that is the number of words in the name
     * @return an integer representing the count of words in the name
     */
    @Override
    public int length (){
        return this.words.size();
    }
    
    /**
     * Obtain a WordInName Object from a specif index.  The first word has an index of 0
     * @param index
     * @return the WOordInNmae Obejct for the index provided or null;
     */
    @Override
    public WordInName getWord(int index){
        if (index <0 || index >= words.size()) return null;
        return words.get(index);
    }
    

    
    /**
     * an equality test between name lists.  This performs only basic
     * one to one tests of the lists and requires full matching of the words
     * @param nl the name list against which to compare
     * @return true if they are the same, false if they differ
     */
    @Override
    public boolean equals (InameList nl){
        if (nl.length() ==0 || this.length() ==0) return false;
        if (nl.length()!= this.length()) return false;
        
        boolean retval = true;
        
        int f;
        for (f=0; f<words.size(); f++){
            if (!words.get(f).equalsIgnoreCase(nl.getWord(f))) retval =  false;
        }
        
         return retval;
    }

 

    
    
    /**
     * A utility function to return all the names concatenated without spaces
     * @return 
     */
    @Override
    public String getNameWithoutSpaces() {
        if (this.length()==0) return "";
        String retval = "";
        for (WordInName w : this.words){
            retval += w.getWord();
        }
        return retval;
    }

    
    /**
     * Replace any errant spaces that are causing false extra words!!
     * ******************************TODO*****************
     * the idea is to look at word pairs in this, 
     * see if they exist in the other as a sequcence at same place or withing 3 later - is so leave alone
     * if not exist, see if exist if concatenated together in same place or within 3 later, if so concatenate, delete 2nd from this
     * and continue from next word - note we are changing THIS collection of words
     * @param testNameList 
     */
    @Override
    public void fixSpaces(InameList testNameList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * returns the name with spaces nicely formatted
     * @return 
     */
    @Override
    public String getName() {
        if (this.length()==0) return "";
        String retval = "";
        for (WordInName w : this.words){
            retval += w.getWord();
            if (!w.isLastWord()) retval += " ";
        }
        return retval;
    }
   

    
}
