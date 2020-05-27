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
package com.pks.opa.extension.text.pojo;


/**
 * <p>Word in name represents a single word within a name.</p>
 Encapsulating it makes it easier to manipulate in the context of a name
 This class implements the usual equals() and EqualsIgnoreCase that you would expect but also 
 adds a extentOfMatch which handles initials (for people names), STEMS ( for organisation names), typos and similar sounding
 * @author p k shersby

 */
public class WordInName{
    
    private  String word ;
    private boolean isPerson;
    private boolean isLastWord = false;
    private boolean isFirstWord = false;
    
    public static enum MATCH_TYPE {
        FULL, STEM, TYPO, PHONO, INITIAL, NONE
    }
    
    private static final String[] STEMS = {"ATION", "MENTS", 
        "MENT", "INGS", "IALS", "IONS", "INGS", "IEST",
        "ING", "IAL", "IAN", "TRY",  "ION", "ICS", "ILE", "IED", "IES", "ARY", "ISM", "ARD", "ATE","ERY",  "EST", "ITY", "IVE", "IZE", 
        "ED", "EN", "ER", "ES", "AL", "IA", "IC", "S"
        
    };
 
  
    /**
     * Constructor for a word in a name.  It is mandatory to provide a word and indicate
     * if this is a part of a persons name or an organisational or object name
     * @param word The word that represents the name
     * @param isPerson true if this is part of a person's name
     */
    public WordInName(String word, boolean isPerson) {
        this.word = word;
        this.isPerson = isPerson;
    }

    /**
     * The name is set using the constructor but this setter allows the name to be changed
     * @param word the name to set
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * this enables the word stored to be recalled
     * @return the name returned as a STring
     */
    public String getWord() {
        return word;
    }
    
    /**
     * This indicates if the name stored represents a real person
     * @return true is a person, false if impersonal (it)
     */
    public boolean isIsPerson() {
        return isPerson;
    }

    /**
     * Allows the nature of the word to be changed to or from personal
     * @param isPerson 
     */
    public void setIsPerson(boolean isPerson) {
        this.isPerson = isPerson;
    }

    /**
     * By default words are not understood in isolation to be a first or last word
     * but in the event of a personal name this can matter since early names may be replaced with 
     * initials.
     * @return true if this is the last word
     */
    public boolean isLastWord() {
        return isLastWord;
    }
    
        /**
     * By default words are not understood in isolation to be a first or last word
     * but in the event of a personal name this can matter since early names may be replaced with 
     * initials but not the last word.
     * @param isLastWord set to true if this is the last word in a name
     */

    public void setIsLastWord(boolean isLastWord) {
        this.isLastWord = isLastWord;
    }
     
    /**
     * By default words are not understood in isolation to be a first or last word
     * but in the event of a personal name this can matter since early names may be replaced with 
     * initials and the first initial has particular importance since usually we need at least
     * to names, the first and last and they must be in the right order and distinct.
     * @return true if this is the first word
     */
    public boolean isFirstWord() {
        return isFirstWord;
    }

         /**
     * By default words are not understood in isolation to be a first or last word
     * but in the event of a personal name this can matter since early names may be replaced with 
     * initials and the first initial has particular importance since usually we need at least
     * to names, the first and last and they must be in the right order and distinct.
     * @param isFirstWord set to true if this is the first word

     */
    public void setIsFirstWord(boolean isFirstWord) {
        this.isFirstWord = isFirstWord;
    }
    
    /**
     * return true if this is empty
     * @return 
     */
    public boolean isEmpty() {
        return word.isEmpty();
    }
    
    /**
     * a function to get the length of the word
     * @return the length
     */
    public int length(){
        return word.length();
    }
    
    /**
     * return true if this is just a single character, i.e. an initial
     * Note that to be an initial requires not only that it is one character but that it is a person
     * @return 
     */
    public boolean isInitial() {
        return (word.length()==1 && this.isPerson);
    }
    
    /**
     * This is simply the inverse of if it is an initial.  
     * Note that to be an initial requires not only that it is one character but that it is a person
     * @return 
     */
    public boolean isRealWord() {
        return !this.isInitial();
    }
    
    /**
     * return the initial of the word
     * @return 
     */
    public char getInitial(){
        return word.charAt(0);
    }

    /**
     * Checks for equality.  This is based on a string equality test of the 
     * object and this object.  It uses the java platform string equality test
     * @param o
     * @return true if equal
     */
    @Override
    public boolean equals (Object o) {
        if (!(o instanceof WordInName)){ return false; }
        return ((WordInName)o).getWord().equals(word); 
    }
    
    /**
     * Compare this WordInName with a String.  It returns true if the string and the name that this
     * instance represents are typographically the same, but ignoring case
     * @param o
     * @return true if the same letter sequence ignoring case
     */
    public boolean equalsIgnoreCase (String o) {
        return o.equalsIgnoreCase(word); 
    }    

     /**
     * Compare this WordInName with another.  It returns true if the y are
     * instance represents are typographically the same, but ignoring case
     * @param o
     * @return true if the same letter sequence ignoring case
     */
    public boolean equalsIgnoreCase (WordInName o) {
        return ((WordInName)o).getWord().equalsIgnoreCase(word); 
    }    
    
    /**
     * ret = FULL, STEM, TYPO, PHONO, INITIAL, NONE
     * @param w the word in name to be compared
     * @return one of the enumeration WordInName.MATCHTYPE with options of FULL, STEM, TYPO, PHONO, INITIAL, NONE
     * <p> Note that in this implementation the PHONO Match type is not implemented.</p>
     */
    public MATCH_TYPE extentOfMatch(WordInName w){
        if (this.equalsIgnoreCase(w)) return MATCH_TYPE.FULL;
        
        String shortestWord = (word.length()<=w.getWord().length())?word : w.getWord();
        String longestWord = (word.length()<=w.getWord().length())? w.getWord(): word;
        
        // Now we do an initials test only if the word relates to a person and is not he last word
        // and one of the words at least is just an initial (we don't check initials if there are real words)
        // it is ok to have initials in a "thing", but we just don't shorten any words to try and match it!
        if (this.isPerson && !this.isLastWord() && !w.isLastWord()) {
            if (w.isInitial() && this.word.startsWith(w.getWord())) return MATCH_TYPE.INITIAL;
            if (this.isInitial() && w.getWord().startsWith(this.word)) return MATCH_TYPE.INITIAL;
        }
        
        
        // Special case for shortened first names
        if (this.isPerson && this.isFirstWord() && w.isFirstWord() && shortestWord.length()>=2) {
            if (longestWord.startsWith(shortestWord)) return MATCH_TYPE.INITIAL;
        }
        

        // Now to handle stemming which we apply only for organisations
        if (!this.isPerson && longestWord.length() >3) {
            
            String stemMe = this.stemmer(longestWord);

            //First compare the basic STEMS
            if (stemMe.equalsIgnoreCase(shortestWord)) return MATCH_TYPE.STEM;
            // Now take off any ending from this word and see if it matches the first 
            // part of the test word and no more than 3 characters at end left over
            if (shortestWord.startsWith(stemMe)&& Math.abs(w.getWord().length() - stemMe.length())<4 ) return MATCH_TYPE.STEM;
 
        }
        
        // Now to handle the limited etc plonked on endwithout a space 
        // Which is only for organisations
        if (!this.isPerson && this.isLastWord && longestWord.length() >8 && shortestWord.length()>2 && longestWord.length() > shortestWord.length()+3) {
            
            
            
            String wordEnd = longestWord.substring(longestWord.length()- shortestWord.length());
            String endWord = Dictionary.getSubstitute(wordEnd, Dictionary.Location.LAST_WORD);
            if (longestWord.startsWith(shortestWord) && endWord.isEmpty() ) return MATCH_TYPE.TYPO;
 
        }  
        
        
        
        int maxGrowth = 1;
        int growth = Math.abs(word.length() - w.getWord().length());
        if (this.word.length()>=8)  maxGrowth = 2;
        if (this.word.length()>20)  maxGrowth = 3;
        
        // Do a typo comparison using an edit distance algorithm Levenshtein provided that there are
        // more than 4 characters in each word.  However we will only permit the comparison if the supposed errors
        // has not made the word grow too much.  we are interested in finding spelling errors not STEMS!
        if (this.word.length()>4 && w.getWord().length()>4  && growth<=maxGrowth){
        
            LevenshteinDistance ld = new LevenshteinDistance (5);  // capping this improves performance
            int distance = ld.apply(word, w.getWord());
            if (distance ==-1) distance=100000;
            
            if (this.word.length()<8 && distance <3)  return MATCH_TYPE.TYPO;
            if (this.word.length()>=8 && this.word.length()<20  && distance <4)  return MATCH_TYPE.TYPO;
            if (this.word.length()>=20 &&  distance <5)  return MATCH_TYPE.TYPO;
            
        }

        // TODO Possible future development  - Do a phonetic comparison.  This is similar to soundex but more 
        // specialised to names

        return MATCH_TYPE.NONE;
    }
    
    
    /**
     * A very simple stemmer that removes patterns as follows
     * INGS, ING, IED, IES, ES, ED, S etc
     * <br/>It does not attempt to repair the word by putting on a new ending!
     * @param s
     * @return 
     */
    private String stemmer(String s){
        
      
        for (String stem : STEMS){
            if (s.endsWith(stem)) return s.substring(0,s.length()-stem.length());
        }
        return s;
        

    }
    

    /**
     * object comparison requires a has code
     * @return a hash code representing this instance
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + word.hashCode();
        return hash;
    }
   
}
