using System;
using System.Collections.Generic;
using System.Collections;
using System.Text;
using Oracle.Determinations.Engine;
using Oracle.Determinations.Masquerade;


namespace PKS.OPA.Extensions.Text
{

    public class CheckNameCompatibility : CustomFunction
    {


        public override object Evaluate(EntityInstance instance, object[] os)
        {
            string name = os[0].ToString().ToUpper().Trim();
            string name2 = os[1].ToString().ToUpper().Trim();
            bool isPerson = ((Oracle.Determinations.Masquerade.Lang.Boolean)os[2]).BooleanValue();
            NameCompare.NameMatch result = new NameCompare().compareNames(name, name2, isPerson);
            if (result == NameCompare.NameMatch.DIFFERS && isPerson)
            {
                result = PartnerCheck( name,  name2, isPerson);

            }

            if (result == NameCompare.NameMatch.MATCH) return Oracle.Determinations.Masquerade.Lang.Boolean.TRUE;
            if (result == NameCompare.NameMatch.DIFFERS) return Oracle.Determinations.Masquerade.Lang.Boolean.FALSE;
            return Uncertain.INSTANCE;
        }

        //Extra Checks for partners
        public NameCompare.NameMatch PartnerCheck(string name, string name2, bool isPerson)
        {
           
            if (name.EndsWith("PARTNERSHIP"))
            {
                NameCompare.NameMatch result = new NameCompare().compareNames(name.Substring(0, name.Length - 11), name2, isPerson);
                if (result == NameCompare.NameMatch.MATCH) return result;
            }
            if (name.EndsWith("PARTNERS"))
            {
                NameCompare.NameMatch result = new NameCompare().compareNames(name.Substring(0, name.Length - 8), name2, isPerson);
                if (result == NameCompare.NameMatch.MATCH) return result;
            }
            if (name2.EndsWith("PARTNERSHIP"))
            {
                NameCompare.NameMatch result = new NameCompare().compareNames(name, name2.Substring(0, name2.Length - 11), isPerson);
                if (result == NameCompare.NameMatch.MATCH) return result;
            }
            if (name2.EndsWith("PARTNERS"))
            {
                NameCompare.NameMatch result = new NameCompare().compareNames(name, name2.Substring(0, name2.Length - 8), isPerson);
                if (result == NameCompare.NameMatch.MATCH) return result;
            }

            return NameCompare.NameMatch.DIFFERS;

        }
    }




    public class CheckBankNameCompatibility : CheckNameCompatibility
    {
        public override object Evaluate(EntityInstance instance, object[] os)
        {
            string name = os[0].ToString().ToUpper().Trim();
            string bank = os[1].ToString().ToUpper().Trim();
            string entity = os[2].ToString().ToUpper().Trim();
            string exclude = os[3].ToString().ToUpper().Trim();


            int entityType = 0;
            if (entity.Equals("PERSON")) { entityType = 1; }
            if (entity.StartsWith("PARTNER")) { entityType = 2; }

            NameCompare.NameMatch result = new NameCompare().compareNames(name, bank, entityType, true);

            string bank1 = bank;
            //Now Since this is a bank, if the param 3 is not an empty string and the result is currently DIFFERS then
            //we need to retry without the exclusioon string on the end
            if (result == NameCompare.NameMatch.DIFFERS && exclude.Length > 0 && bank.Length > exclude.Length)
            {
                if (bank.EndsWith(exclude))
                {
                    bank1 = bank.Substring(0, bank.Length - exclude.Length);
                    result = new NameCompare().compareNames(name, bank1, entityType, true);
                }
            }

            // These attempt the removal of partner, partnership for partnerships!
            if (result == NameCompare.NameMatch.DIFFERS && entityType == 2)
            {
                result = PartnerCheck(name, bank, true);

            }
            if (result == NameCompare.NameMatch.DIFFERS && entityType == 2)
            {
                result = PartnerCheck(name, bank1, true);

            }

  


            if (result == NameCompare.NameMatch.MATCH) return Oracle.Determinations.Masquerade.Lang.Boolean.TRUE;
            if (result == NameCompare.NameMatch.DIFFERS) return Oracle.Determinations.Masquerade.Lang.Boolean.FALSE;
            return Uncertain.INSTANCE;
        }


    }



   public class NameCompare 
    {
        public enum NameMatch
        {
            MATCH, DIFFERS, UNCERTAIN
        }


        public static string[] TRADENAME_PHRASES = {  " TA ", " T/A ", " T/AS ", " TRADING AS ", " TRD AS ", " T.A. ", " T.A " };

        public NameMatch compareNames(String primary, String secondary, int EntityType)
        {
            return compareNames(primary, secondary, EntityType, false);
        }


        public NameMatch compareNames(String primary, String secondary, Boolean isPerson)
        {
            return compareNames(primary, secondary, isPerson ? 1 : 0, false);
        }

        public NameMatch compareNames(string primary, string secondary, int EntityType, Boolean isBank)
        {
            bool isPerson = (EntityType == 1) ? true : false;

            //Sanity Checks and ensuring we have safe parameters
            if (primary.Length==0 || secondary.Length == 0) return NameMatch.UNCERTAIN;
            if (primary.Equals(secondary,StringComparison.OrdinalIgnoreCase)) return NameMatch.MATCH;

            //Extract the basic names from the inputs  these have to be parsed 
            //because they may be a combination of a name and a trading name
            //We don't worry about spaces because we will get rid of these when we turn
            //them into lists of individual words for comaprison
            string primaryName = extractTradingName(primary, TRADENAME_PHRASES)[0].ToUpper();
            string primaryTradingName = extractTradingName(primary, TRADENAME_PHRASES)[1].ToUpper();
            string testName = extractTradingName(secondary, TRADENAME_PHRASES)[0].ToUpper();
            string testTradingName = extractTradingName(secondary, TRADENAME_PHRASES)[1].ToUpper();


            //Check for a quick win situations
            //Note that if the trading names match each other (could be blank) then
            //the name prefixing this must also match so John Smith Trading as 
            //Chips does not incorrectly match Peter trading as Chips
            if (primaryName.Equals(testName,StringComparison.OrdinalIgnoreCase)) { return NameMatch.MATCH; }
            if (primaryName.Length > 8 && testName.Length< 3 && testName.Length > 0) { return NameMatch.DIFFERS; }
            if (primaryTradingName.Equals(testName, StringComparison.OrdinalIgnoreCase)) { return NameMatch.MATCH; }
            if (primaryName.Equals(testTradingName,StringComparison.OrdinalIgnoreCase)) { return NameMatch.MATCH; }


            //There can be a problem comparing a single name f a person with a group 
            //of people so we handle this using the presence of "and"
            //
            if (isPerson)
            {
                if ((primaryName.Contains(" AND ") || primaryName.Contains("&")) != (testName.Contains(" AND ") || testName.Contains("&"))) { return NameMatch.DIFFERS; }
            }


            // We convert the 4 possible names into name word lists
            //during this there is automatic removel of non letter/digit characters an
            // also word subsitution

            InameList primaryNameList = null;
            InameList testNameList = null;
            InameList primaryTradingAsList = null;
            InameList testTradingAsList = null;

            //set up a loop that conditionally re-trys all the tests without a bracketted word
            // note the primary name or trading contains the bracket not the bank name and it only
            //applies to bank checks
            int bracketTestLimit = 0;
            if (!isPerson && primaryName.Contains("(")) bracketTestLimit = 1;
            int bracketStripper;
            for (bracketStripper = 0; bracketStripper <= bracketTestLimit; bracketStripper++)
            {

                int f;
                string spaceList = " -&";
                // This loop controlls multiple iterations with slight changes 
                // 1 = alternative hyphen handling
                // 2 = revese surnname order if a person
                for (f = 0; f < 4; f++)
                {
                    spaceList = " -&";

                    if (f == 1 || f == 3) spaceList = " -&";



                    //we should really have a nameListFactory serve up the right type
                    //we could impllemtn that for partnerships
                    if (isBank)
                    {
                        primaryNameList = new BankNameList(primaryName, spaceList, isPerson, bracketStripper == 1);
                        testNameList = new BankNameList(testName, spaceList, isPerson, false);
                        primaryTradingAsList = new BankNameList(primaryTradingName, spaceList, false, bracketStripper == 1);
                        testTradingAsList = new BankNameList(testTradingName, spaceList, false, false);
                    }
                    else
                    {
                        primaryNameList = new NameList(primaryName, spaceList, isPerson, false);
                        testNameList = new NameList(testName, spaceList, isPerson, false);
                        primaryTradingAsList = new NameList(primaryTradingName, spaceList, false, false);
                        testTradingAsList = new NameList(testTradingName, spaceList, false, false);
                    }

                    // Here we swap the word order of primary name to see if it will match!
                    if (isPerson && testName.StartsWith(primaryNameList.getWord(primaryNameList.length() - 1).getWord()) && (f == 2 || f == 3))
                    {
                        testNameList.firstToLast();
                    }
          

                    // Finally we need to do the comparisons.  This is done in the
                    // Name List itself since it should be able to compare itself to another list.


                    if (primaryNameList.acceptableMatch(testNameList)) return NameMatch.MATCH;
                    if (primaryNameList.acceptableMatch(testTradingAsList)) return NameMatch.MATCH;
                    if (primaryTradingAsList.acceptableMatch(testNameList)) return NameMatch.MATCH;
                    if (primaryTradingAsList.acceptableMatch(testTradingAsList)) return NameMatch.MATCH;

                }
            }

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
        public String[] extractTradingName(string originalName, string[] phrases)
        {
            String[] result = { originalName, "" };
            int p = -1;
            int f;
            if (originalName.Length < 6) { return result; }  // too short to have trading as


            for (f = 0; f < phrases.Length && p == -1; f++)
            {
                p = originalName.IndexOf(phrases[f], 3);
                if (p >= 0)
                {
                    result[0] = originalName.Substring(0, p);
                    result[1] = originalName.Substring(p + phrases[f].Length);
                }
            }

            return result;


        }




    }


    class Dictionary
    {
        public enum Location
        {
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
         * @return 
         */
        public static string getSubstitute(string originalWord, Location locationOfword)
        {
            String[,] WordList;
            String found = originalWord;
            switch (locationOfword)
            {
                case Location.FIRST_WORD:
                    WordList = FRONT_LIST;
                    found = lookup(originalWord, WordList);
                    break;
                case Location.LAST_WORD:
                    WordList = END_LIST;
                    found = lookup(originalWord, WordList);
                    break;
                case Location.PERSON_END_WORD:
                    WordList = BANK_END_LIST;
                    found = lookup(originalWord, WordList);
                    break;
            }

            //Having optionally looked for a front or back replacment we now
            //look for an "any word" replacment only if we have not already
            //changed the word

            if (found.Equals(originalWord))
            {
                found = lookup(originalWord, WORD_LIST);
            }
            return found;



        }


        //This list of words handles any word in the phrase including those at start and end and in the middle
        private static string[,]
        WORD_LIST = {
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
        public static string[,] FRONT_LIST = {
        {"LORD", ""},
        {"PROF", ""}

        };

    //This list of words is only altered is we are comparing the last
    //word of a phrase
    // miss-spelling of Limited found in production added 8th July 2019
        private static  string[,]
        END_LIST = {
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
            {"IMITED",""},
            {"LIMITD",""},
            {"LIMITED","" },
            {"LIMIED",""},
            {"JUNIOR",""},
            {"SENIOR",""},
            {"INCORP",""},
            {"UNLTD",""},
            {"LIMIED",""},
            {"LIMTED",""},
            {"LIIMITED",""},
            {"LIMITIED",""},
            {"LTF",""},
            {"COLTD",""},
            {"LILMITED",""},
            {"LINTED",""},
            {"LIMNITED",""},
            {"LIMITEED",""},
            {"LMTED",""},
            {"IMITED",""},
            {"LIMTD",""},
            {"LIMERTED",""},
            {"LIMITADA",""},
            {"LIMITITED",""},
            {"LITED",""},
            {"LIMITE",""},
            {"LMITED",""},
            {"LIMIITED",""},
            {"LIMIETD",""},
            {"LOMITED",""},
            {"LIMYED",""},
            {"LIMTRED",""},
            {"GONGSI",""},
            {"GOG SI",""},
            {"CYF",""},
            {"CYFYNGEDIG",""},
            {"INCORPORATED",""}
                
        };

        //This list of words is only altered is we are comparing the last
        //word of a phrase for a bank account for an individual

        private static string[,]
        BANK_END_LIST = {
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
            {"IMITED",""},
            {"LIMITD",""},
            {"LIMITED","" },
            {"LIMIED",""},
            {"JUNIOR",""},
            {"SENIOR",""},
            {"INCORP",""},
            {"UNLTD",""},
            {"LIMIED",""},
            {"LIMTED",""},
            {"LIIMITED",""},
            {"LIMITIED",""},
            {"LTF",""},
            {"COLTD",""},
            {"LILMITED",""},
            {"LINTED",""},
            {"LIMNITED",""},
            {"LIMITEED",""},
            {"LMTED",""},
            {"IMITED",""},
            {"LIMTD",""},
            {"LIMERTED",""},
            {"LIMITADA",""},
            {"LIMITITED",""},
            {"LITED",""},
            {"LIMITE",""},
            {"LMITED",""},
            {"LIMIITED",""},
            {"LIMIETD",""},
            {"LOMITED",""},
            {"LIMYED",""},
            {"LIMTRED",""},
            {"GONGSI",""},
            {"GOG SI",""},
            {"CYF",""},
            {"CYFYNGEDIG",""},
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
         * @return 
         */
        private static String lookup(string original, string[,] WordList)
        {
            int dictionaryLength = WordList.GetLength(0);
            Boolean found = false;
            int f = 0;
            while (f < dictionaryLength && !found)
            {

                if (WordList[f,0].Equals(original,StringComparison.OrdinalIgnoreCase))
                {
                    found = true;
                }
                else { f++; }
            }

            return found ? WordList[f,1] : original;

        }

    }


    class LevenshteinDistance
    {
        private static uint MIN3(uint a, uint b, uint c)
        {
            return ((a) < (b) ? ((a) < (c) ? (a) : (c)) : ((b) < (c) ? (b) : (c)));
        }

        public int apply (string s1, string s2)
        {
            uint s1len, s2len, x, y, lastdiag, olddiag;
            s1len = (uint)s1.Length;
            s2len = (uint)s2.Length;
            uint[] column = new uint[s1len + 1];

            for (y = 1; y <= s1len; ++y)
                column[y] = y;

            for (x = 1; x <= s2len; ++x)
            {
                column[0] = x;

                for (y = 1, lastdiag = x - 1; y <= s1len; ++y)
                {
                    olddiag = column[y];
                    column[y] = MIN3(column[y] + 1, column[y - 1] + 1, (uint)(lastdiag + (s1[(int)(y - 1)] == s2[(int)(x - 1)] ? 0 : 1)));
                    lastdiag = olddiag;
                }
            }

            return (int)(column[s1len]);
        }
    }

    public interface InameList
    {
        bool acceptableMatch(InameList nl);

        /**
         * an equality test between name lists.  This performs only basic
         * one to one tests of the lists and requires full matching of the words
         * @param nl the name list against which to compare
         * @return true if they are the same, false if they differ
         */
        bool equals(InameList nl);

        /**
         * A utility function to return all the names concatenated without spaces
         * @return
         */
        String getNameWithoutSpaces();

        WordInName getWord(int index);

        int length();
        void firstToLast();
        string getFirstWord();
        string getLastWord();
    }


    public class BankNameList : NameList 
    {
        // Currently just calls the case class but pre-processing can go here!
        public BankNameList (string phrase, string spaceList, Boolean isPerson, Boolean stripLastBracket) :base(phrase, spaceList, isPerson,  stripLastBracket)
        {
            // for the bank we re-substitute if a person to strip business account etc
            // need to mark first and last words since these have meaning in peoples names
            // they may have changes since substitution may have deleted some words
            if (isPerson)
            {
                this.substitute();
                words[0].setIsFirstWord(true);
                words[words.Count - 1].setIsLastWord(true);
            }
        }
    }


    public class NameList : InameList
    {


        protected List<WordInName> words;
        
        Boolean isPerson;

        /**
         * A constructor that takes a phrase and creates a word list
         * @param phrase - the phrase to be broken into words using only letters and digits
         * @param substitutes - a dictionary of words that are substituted.  If null implements a default Dictionary instance
         * @param spaceList - a string with characters that are treated as spaces, if null then defaults to space, dash and ampersand
         * @param isPerson - a parameter that indicates that matching is to be performed on the basis these are names of a real person
         * this alters behaviour of initials and stemming.
         */
        public NameList(string phrase, string spaceList, Boolean isPerson, Boolean stripLastBracket)
        {
            this.words = new List<WordInName>();
           
            this.isPerson = isPerson;
            this.splitIntoWords(phrase, spaceList);
            if (words.Count==0) return;

            // mark first and last words since thes impact substition
            words[0].setIsFirstWord(true);
            words[words.Count - 1].setIsLastWord(true);
            this.substitute();


            //Bug fix 26-3-18 for when all the names have been stripped leaving nothing to compare
            // if there are no words left then subsitute with "XXXX NO WORDS XXX"
            if (words.Count ==0) words.Add(new WordInName("XXXX NO WORDS XXX", isPerson));



            // need to mark first and last words since these have meaning in peoples names
            // they may have changes since substitution may have deleted some words
            words[0].setIsFirstWord(true);
            
            // get rid of last word if it is bracketed
            if (words.Count> 1 && stripLastBracket && phrase.Contains("(" + getLastWord() + ")"))
            {
                words.RemoveAt(words.Count - 1);
            }


            words[words.Count - 1].setIsLastWord(true);
        }


   
    public String getLastWord()
        {
            if (words.Count == 0) return null;
            return words[words.Count - 1].getWord();
        }

        public String getFirstWord()
        {
            if (words.Count == 0) return null;
            return words[0].getWord();
        }
            /**
             * This function runs through all the words providing dictionary substitution
             * and also removing any blank words that result.
             * @param lookup 
             */
            public void substitute()
        {
            int f;
            Dictionary.Location l = Dictionary.Location.LAST_WORD;
            WordInName win;

            //This is a backward iteration since we have to delete and C# does not have safe iterators!
            for (f = words.Count-1; f>=0; f--)
            {
                if (f==0) l = Dictionary.Location.FIRST_WORD;
                if (f == words.Count - 1) { l = isPerson? Dictionary.Location.PERSON_END_WORD:Dictionary.Location.LAST_WORD; } else { l = Dictionary.Location.MID_WORD; }
                win = words[f];
                win.setWord( Dictionary.getSubstitute(win.getWord(), l));
                if (win.isEmpty()) words.RemoveAt(f);

            }



        }



        /**
       * This function is used to split up a string into words.  We could
       * use a tokenizer but this approach is fairly efficient and easy to configure
       * and also easy to replicate into C#
       * @param phrase
       * @return an ArrayList containing the extracted words
       */
        private void splitIntoWords(string phrase, string spaceList)
        {

            int length = phrase.Length;
            int f;
            StringBuilder extractedName = new StringBuilder();
            Boolean wordDone = false;
            char c;

            //Improvement Dec 2019 People are missing out spaces around commas and full stops so
            //put in the space 
            phrase = phrase.Replace(",", ", ");
            phrase = phrase.Replace(".", ". ");

            //Iterate throught the name removing any nasty characters along the way and
            //Converting to upper case to improve comparative checks later
            for (f = 0; f < length; f++)
            {
                c = phrase[f];
                if (char.IsLetterOrDigit(c))
                {
                    extractedName.Append(c.ToString().ToUpper());
                    wordDone = false;
                    if (f == length - 1) wordDone = true;
                }
                else
                {  // We have not found a character we like! but is it a spacer or to be ignored
                    if (f == length - 1 || char.IsWhiteSpace(c) || spaceList.IndexOf(c) >= 0)
                    {
                        wordDone = true;
                    }
                }

                if (wordDone && extractedName.Length > 0)
                {
                    words.Add(new WordInName(extractedName.ToString(), this.isPerson));
                    extractedName = new StringBuilder();  // There is a clear method but it throws and exception!
                }

            }
        }

        //Verison 1.4 allows for surname first
        public void firstToLast()
        {
            string firstWord = words[0].getWord();
            int f;
            for (f = 0;  f < (words.Count - 1); f++)
            {
                words[f].setWord(words[f + 1].getWord());
            }
            words[words.Count - 1].setWord(firstWord);
        }

        public int length()
        {
            return this.words.Count;
        }

        public WordInName getWord(int index)
        {
            if (index < 0 || index >= words.Count) return null;
            return words[index];
        }


        /**
         * an equality test between name lists.  This performs only basic
         * one to one tests of the lists and requires full matching of the words
         * @param nl the name list against which to compare
         * @return true if they are the same, false if they differ
         */
        public Boolean equals(InameList nl)
        {
            if (nl.length() != this.length()) return false;

            int f;
            for (f = 0; f < words.Count; f++)
            {
                if (!words[f].equalsIgnoreCase(nl.getWord(f).getWord())) return false;
            }

            return true;
        }

        public Boolean acceptableMatch(InameList nl)
        {
            Boolean matchSoFar = true;
            if (this.length() == 0 || nl.length() == 0) return false;



            // quick tests
            // If they are typographically the same then they match!
            if (this.equals(nl)) return true;
            if (this.getNameWithoutSpaces().Equals(nl.getNameWithoutSpaces())) return true;

            //Special CAse that a person might have formatted initials differently aaround missed middle names
            // E.g.   JA Smith should match John Andrew Smith OR John Smith but not John Big Smith
            if (isPerson && this.length() == 2 && nl.length() >= 2 && this.getLastWord().Equals(nl.getLastWord()))
            {
                if (this.getFirstWord().Length < 3)
                {
                    int gh;
                    String testString = "";
                    for (gh = 0; gh < nl.length() - 1; gh++)
                    {
                        testString += nl.getWord(gh).getInitial();
                    }
                    if (this.getFirstWord().StartsWith(testString)) return true;
                }
            }



            //If we are testing a person and one only has one name and the other more then
            // we won't be able to find a good match
            if (this.isPerson && this.length() < 2 && nl.length() >= 2) return false;
            if (this.isPerson && this.length() >= 2 && nl.length() < 2) return false;
            // If we re testing something not a person and they differ in length then they dont match
            if (!this.isPerson && this.length() != nl.length()) return false;

            // Having gone through the basic eliminations then we move on to more
            // complicated tests that will differ depending if we are dealign with a person or a thing.

            // First we compare as though we are lookign a "things" because this also works for people
            // The basic test is we expect all the same words in the sdame order and each word should be a
            //good match although not necessarily perfect.  At this point we ignore initial checks

            int f;
            if (this.length() == nl.length())
            {
                for (f = 0; f < words.Count; f++)
                {
                    if (words[f].extentOfMatch(nl.getWord(f)) == MATCH_TYPE.NONE) matchSoFar = false;
                }
                return matchSoFar;  // we can exit here because they must have been the same length therefore
                                    // irrespective of person or thing then the words should match int he same order

            }

            // Here we know we have candidates that are differing lengths and that
            // we are dealing with a person not a thing!
            // There must be at least 2 names in both because the single name comparison is already done

            

            InameList shortestName = (this.length() <= nl.length()) ? this : nl;
            InameList longestName = (this.length() > nl.length()) ? this : nl;

            //A special case for individuals is that the last name is the same and the first word in the shortest is
            //a concatenation of the firs n initials of th elongest!  e. John Big Smith  = JB Smith
            if (this.isPerson && shortestName.length() < longestName.length() && shortestName.length() == 2)
            {
                int initialsToCount = longestName.length() - 1;

                String combinedInitials = "";
                for (f = 0; f < initialsToCount; f++)
                {
                    combinedInitials += longestName.getWord(f).getInitial();
                }
                if (shortestName.getWord(0).equalsIgnoreCase(combinedInitials) && shortestName.getWord(1).equalsIgnoreCase(longestName.getWord(initialsToCount).getWord())) { return true; }

            }






            // Cannot match if first name differs or last name differs
            if (words[0].extentOfMatch(nl.getWord(0)) == MATCH_TYPE.NONE) return false;
            if (words[this.length() - 1].extentOfMatch(nl.getWord(nl.length() - 1)) == MATCH_TYPE.NONE) return false;

            int lastFoundPosition = 0;
            int g;
            Boolean matchword = false;
            // Now check the middle bits!
            for (f = 1; f < shortestName.length() - 1; f++)
            {
                matchword = false;
                for (g = lastFoundPosition + 1; g < nl.length() - 1 && !matchword; g++)
                {  // compare the name against the remaining bit of the nl.
                    if (words[f].extentOfMatch(nl.getWord(g)) != MATCH_TYPE.NONE)
                    {
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
        * A utility function to return all the names concatenated without spaces
        * @return 
        */
        public String getNameWithoutSpaces()
        {
            if (this.length() == 0) return "";
            String retval = "";
            foreach (WordInName w in this.words)
            {
                retval += w.getWord();
            }
            return retval;
        }



    }

    public class WordInName
    {
        private string word;
        private Boolean isPerson;
        private Boolean isLastWord = false;
        private Boolean isFirstWord = false;

        private String[] STEMS = {"ATION", "MENTS", 
        "MENT", "INGS", "IALS", "IONS", "INGS", "IEST",
        "ING", "IAL", "IAN", "TRY",  "ION", "ICS", "ILE", "IED", "IES", "ARY", "ISM", "ARD", "ATE","ERY",  "EST", "ITY", "IVE", "IZE", 
        "ED", "EN", "ER", "ES", "AL", "IA", "IC", "S"
        
    };

        /**
         * Constructor for a word in a name.  It is mandatory to provide a word and
         * if this is a part of a persons name or an organisational or object name
         * @param word The word that represents the name
         * @param isPerson true if this is part of a person's name
         */
        public WordInName (string word, Boolean isPerson)
        {
            this.word = word;
            this.isPerson = isPerson;
        }

        /**
         * The name is set using the constructor but this setter allows the name to be changed
         * @param word the name to set
         */
        public void setWord(string word)
        {
            this.word = word;
        }

        /**
         * this enables the word stored to be recalles
         * @return the name returnesd as a STring
         */
        public string getWord()
        {
            return word;
        }
        /**
   * This indicates if the name stored represents a real person
   * @return true is a person, false if impersonal (it)
   */
        public Boolean getIsPerson()
        {
            return isPerson;
        }

        /**
         * Allows the nature of the word to be changed to or from personal
         * @param isPerson 
         */
        public void setIsPerson(Boolean isPerson)
        {
            this.isPerson = isPerson;
        }

        /**
         * by defualt words are not understood in isolation to be a first or last word
         * but in the event of a personal name this can matter since early names may be replaced with 
         * initials.
         * @return true if this is the last word
         */
        public Boolean getIsLastWord()
        {
            return isLastWord;
        }

        /**
     * by defualt words are not understood in isolation to be a first or last word
     * but in the event of a personal name this can matter since early names may be replaced with 
     * initials but not the last word.
     * @param isLastWord set to true if this is the last word in a name
     */

        public void setIsLastWord(Boolean isLastWord)
        {
            this.isLastWord = isLastWord;
        }

        /**
         * by defualt words are not understood in isolation to be a first or last word
         * but in the event of a personal name this can matter since early names may be replaced with 
         * initials and the first initial has particular importance since usually we need at least
         * to names, the first and last and they must be in the right order and distinct.
         * @return true if this is the first word
         */
        public Boolean getIsFirstWord()
        {
            return isFirstWord;
        }

        /**
    * by default words are not understood in isolation to be a first or last word
    * but in the event of a personal name this can matter since early names may be replaced with 
    * initials and the first initial has particular importance since usually we need at least
    * to names, the first and last and they must be in the right order and distinct.
    * @param isFirstWord set to true if this is the first word

    */
        public void setIsFirstWord(Boolean isFirstWord)
        {
            this.isFirstWord = isFirstWord;
        }

        /**
         * return true if this is empty
         * @return 
         */
        public Boolean isEmpty()
        {
            return word.Length<1;
        }

        /**
         * a function to get the length of the word
         * @return the length
         */
        public int length()
        {
            return word.Length;
        }

        /**
         * return true if this is just a single character, i.e. an initial
         * Note that to be an initial requires not only that it is one character but that it is a person
         * @return 
         */
        public Boolean isInitial()
        {
            return (word.Length == 1 && this.isPerson);
        }

        /**
         * This is simply the inverse of if it is an initial.  
         * Note that to be an initial requires not only that it is one character but that it is a person
         * @return 
         */
        public Boolean isRealWord()
        {
            return !this.isInitial();
        }

        /**
         * return the initial of the word
         * @return 
         */
        public string getInitial()
        {
            return word.Substring(0,1);
        }

        /**
         * Checks for equality.  This is based on a string equality test of the 
         * object and this object.  It uses the java platform string equality test
         * @param o
         * @return true if equal
         */

    public Boolean equals(WordInName o)
        {
            
            return ((WordInName)o).getWord().Equals(word);
        }

        public Boolean equals(String o)
        {

            return o.Equals(word);
        }

        public Boolean equalsIgnoreCase(String o)
        {

            return o.Equals(word, StringComparison.OrdinalIgnoreCase);
        }

        /**
         * A very simple stemmer that removes patterns as follows
         * INGS, ING, IED, IES, ES, ED, S etc
         * <br/>It does not attempt to repair the word by putting on a new ending!
         * @param s
         * @return 
         */
        private string stemmer(string s)
        {

            foreach (String stem in STEMS)
            {
                if (s.EndsWith(stem)) return s.Substring(0, s.Length - stem.Length);
            }
            return s;

        }


        public MATCH_TYPE extentOfMatch(WordInName w)
        {
            if (this.equalsIgnoreCase(w.getWord())) return MATCH_TYPE.FULL;

            string shortestWord = (word.Length <= w.getWord().Length) ? word : w.getWord();
            string longestWord = (word.Length <= w.getWord().Length) ?  w.getWord() : word;

            // Now we do an initials test only if the word relates to a person and is not he last word
            // and one of the words at least is just an initial (we don't check initials if there are real words)
            // it is ok to have initials in a "thing", but we just don't shorten any words to try and match it!
            if (this.isPerson && !this.getIsLastWord() && !w.getIsLastWord())
            {
                if (w.isInitial() && this.word.StartsWith(w.getWord())) return MATCH_TYPE.INITIAL;
                if (this.isInitial() && w.getWord().StartsWith(this.word)) return MATCH_TYPE.INITIAL;
            }

            // Special case for shortened first names
            if (this.isPerson && this.getIsFirstWord() && w.getIsFirstWord() && shortestWord.Length >= 2)
            {
                if (longestWord.StartsWith(shortestWord)) return MATCH_TYPE.INITIAL;
            }


            // Now to handle stemming wich we apply only for organisations
            if (!this.isPerson && longestWord.Length >3)
            {
                String stemMe = this.stemmer(longestWord);
                
                //First compare the basic stems
                if (stemMe.Equals(shortestWord)) return MATCH_TYPE.STEM;
                // Now take off any ending from this word and see if it matches the first 
                // part of the test word and no more than 3 characters at end left over
                // then repeat the other way around
                if (shortestWord.StartsWith(stemMe) && Math.Abs(w.getWord().Length - stemMe.Length) < 4) return MATCH_TYPE.STEM;
                
            }

            // Now to handle the limited etc plonked on end of last word
            // Which is only for organisations
            if (!this.isPerson && this.isLastWord && longestWord.Length > 8 && shortestWord.Length > 2 && longestWord.Length > (shortestWord.Length +3))
            {
               
                string wordEnd = longestWord.Substring(longestWord.Length - shortestWord.Length);
                string endWord = Dictionary.getSubstitute(wordEnd, Dictionary.Location.LAST_WORD);
                if (longestWord.StartsWith(shortestWord) && endWord.Length==0) return MATCH_TYPE.TYPO;

            }

            // Do a typo comparison using an edit distance algorithm Levenshtein provided that there are
            // more than 4 characters in each word.  However we also wan to ensure that size has not changed too much - these are names
            // so typos tend to be subsitution and the odd extra character not massive error.
            int maxGrowth = 1;
            int growth = Math.Abs(word.Length - w.getWord().Length);
            if (this.word.Length >= 8) maxGrowth = 2;
            if (this.word.Length > 20) maxGrowth = 3;

            if (this.word.Length > 4 && w.getWord().Length > 4 && growth <= maxGrowth)
            {

                LevenshteinDistance ld = new LevenshteinDistance();  // capping this improves performance
                int distance = ld.apply(word, w.getWord());
                if (distance == -1) distance = 100000;

                if (this.word.Length < 8 && distance < 3) return MATCH_TYPE.TYPO;
                if (this.word.Length >= 8 && this.word.Length < 20 && distance < 4) return MATCH_TYPE.TYPO;
                if (this.word.Length >= 20 && distance < 5) return MATCH_TYPE.TYPO;

            }

            // TODO Possible future development  - Do a phonetic comparison.  This is similar to soundex but more 
            // specialised to names

            return MATCH_TYPE.NONE;
        }


    }


    public enum MATCH_TYPE
    {
        FULL, STEM, TYPO, PHONO, INITIAL, NONE
    }




}
