using System;
using System.Collections.Generic;
using System.Text;
using Oracle.Determinations.Engine;
using Oracle.Determinations.Masquerade;
using System.Text.RegularExpressions;
using Oracle.Determinations.Masquerade.Util;

namespace PKS.OPA.Extensions.Text
{

    //A class that filers a string to only permit specified characters
    // Due to a problem with OPA we appear not to be able to use regex so having to do a different lless elegant implementation
    public class LimitedToCharacters : CustomFunction
    {
        public override Object Evaluate(EntityInstance entityInstance, object[] objects)
        {
            string text = (string)objects[0];
            string permittedCharacters = (string)objects[1];

            StringBuilder result = new StringBuilder();

            for (int i = 0; i < text.Length; i++)
            {
                char c = text[i];
                if (permittedCharacters.Contains(c.ToString()))
                {
                    result.Append(c);
                }
   
            }

            return result.ToString();


        }

    }



    // Due to a problem with OPA we appear not to be able to use regex so having to do a different lless elegant implementation
    public class ContainsAnyOf : CustomFunction
    {
        public override Object Evaluate(EntityInstance entityInstance, object[] objects)
        {
            string original = (string)objects[0];
            string searchStrings = (string)objects[1];
            string delimiter = (string)objects[2];
            string[] textToFind;
            textToFind = searchStrings.Split(delimiter.ToCharArray());

            foreach (string e in textToFind)
            {
                if (original.Contains(e)) {   return Oracle.Determinations.Masquerade.Lang.Boolean.TRUE; }
            }
            return Oracle.Determinations.Masquerade.Lang.Boolean.FALSE;


        }

    }



    // Due to a problem with OPA we appear not to be able to use regex so having to do a different lless elegant implementation
    public class ReplaceCharacters : CustomFunction
    {
        public override Object Evaluate(EntityInstance entityInstance, object[] objects)
        {
            string original = (string)objects[0];
            string searchStrings = (string)objects[1];
            string replacement = (string)objects[2];
            string delimiter = (string)objects[3];
            string[] textToChange;
            textToChange = searchStrings.Split(delimiter.ToCharArray());

            foreach (string e in textToChange)   {
                original = original.Replace(e, replacement);
            }
            return original;


        }

    }

    // Due to a problem with OPA we appear not to be able to use regex so having to do a different lless elegant implementation
    public class ReplaceSubStringList : CustomFunction
    {
        public override Object Evaluate(EntityInstance entityInstance, object[] objects)
        {
            string original = (string)objects[0];
            string searchStrings = (string)objects[1];
            string replacementStrings = (string)objects[2];
            string delimiter = (string)objects[3];
            string[] textToChange;
            string[] textToPutIn;
            textToChange = searchStrings.Split(delimiter.ToCharArray());
            textToPutIn  = replacementStrings.Split(delimiter.ToCharArray());
            int f;

            for (f=0; f< textToChange.Length; f++)
            
            {
                original = original.Replace(textToChange[f], textToPutIn[f]);
            }
            return original;


        }

    }




    public class ValueForKey : CustomFunction
    {
        public override Object Evaluate(EntityInstance entityInstance, object[] objects)
        {


            string propertyList = ((string)objects[0]).Trim();
            string keyName = ((string)objects[1]).Trim(); ;
            string delimiter = (string)objects[2];
            string[] propertyArray;
            string keyFound;

            if (delimiter.Length == 0) { delimiter = ":"; }

            propertyArray = propertyList.Split(delimiter.ToCharArray());

            foreach ( string e in propertyArray ) {
                keyFound = e.Split('=')[0].Trim();
                if(keyFound.Equals(keyName, StringComparison.CurrentCultureIgnoreCase)) { return e.Split('=')[1].Trim(); }
            }

            return null;

        }

    }




    public class CapitaliseAll : CustomFunction
    {
        public override Object Evaluate(EntityInstance entityInstance, object[] objects)
        {
            string text = (string)objects[0];
            return text.ToUpper();

        }

    }

    public class CapitaliseFirstLetter : CustomFunction
    {
        public override Object Evaluate(EntityInstance entityInstance, object[] objects)
        {
            string text = (string)objects[0];
            StringBuilder result = new StringBuilder();

            bool capitalizeNext = true;
            for (int i = 0; i < text.Length; i++)
            {
                char c = text[i];
                if (char.IsLetter(c) && capitalizeNext)
                {
                    result.Append(char.ToUpper(c));
                    capitalizeNext = false;
                }
                else
                {
                    result.Append(c);
                }
            }

            return result.ToString();
        }
    }

    public class CapitaliseEachWord : CustomFunction
    {
        public override Object Evaluate(EntityInstance entityInstance, object[] objects)
        {
            string text = (string)objects[0];
            StringBuilder result = new StringBuilder();

            bool capitalizeNext = true;
            for (int i = 0; i < text.Length; i++)
            {
                char c = text[i];
                if (char.IsLetter(c) && capitalizeNext)
                {
                    result.Append(char.ToUpper(c));
                    capitalizeNext = false;
                }
                else
                {
                    result.Append(c);
                    if (char.IsWhiteSpace(c))
                    {
                        capitalizeNext = true;
                    }
                }
            }

            return result.ToString();
        }

    }

    public class Trim : CustomFunction
    {
        public override Object Evaluate(EntityInstance entityInstance, object[] objects)
        {
            string text = (string)objects[0];
            return text.Trim();

        }
    }


    public class PositionOf : CustomFunction
    {
        public override object Evaluate(EntityInstance instance, object[] os)
        {

            String searchedForText = (string)os[0];
            String searchedText = (string)os[1];
            int startPos = ((Oracle.Determinations.Masquerade.Lang.Double)os[2]).IntValue();
            int position = searchedText.IndexOf(searchedForText, startPos);
            if (position == -1) { return Uncertain.INSTANCE; }
            return Oracle.Determinations.Masquerade.Lang.Double.ValueOf(position);

        }
    }

    public class AssembleName : CustomFunction
    {
        public override object Evaluate(EntityInstance instance, object[] os)
        {
            String name0 = "";
            String name1 = "";
            String name2 = "";
            if (null != os[0] && !(os[0] == Uncertain.INSTANCE)) name0 = ((String)os[0]).Trim() + " ";
            if (null != os[1] && !(os[1] == Uncertain.INSTANCE)) name1 = ((String)os[1]).Trim() + " ";
            if (null != os[2] && !(os[2] == Uncertain.INSTANCE)) name2 = ((String)os[2]).Trim();

            return name0 + name1 + name2;

        }

        public override Boolean RequireKnownParameters()
        {
            return false;
        }

    }


    /**
     * Concatenates a list of strings from entities
     */
      
    public class InstanceTextValueList : CustomFunction
    {
        public override object Evaluate(EntityInstance ei, object[] os)
        {
            String relationshipName = "";
            String attributeName = "";
            String delimiter = "";
            int qty = ((Oracle.Determinations.Masquerade.Lang.Double)os[3]).IntValue();
            relationshipName = os[0].ToString();
            attributeName =os[1].ToString();
            delimiter = os[2].ToString();
            String result = "";

            Entity ent, currEntity;
            Relationship rel = null;
            Oracle.Determinations.Masquerade.Util.List targets;
            Oracle.Determinations.Masquerade.Util.List relationships;
            Oracle.Determinations.Masquerade.Util.List attributes;
            //RBAttr currAttr = null; 

            // We have to access relationships from the entity not the entity instance so we get the entity
            ent = ei.GetEntity();
            relationships = ent.GetRelationships();
            rel = ent.GetRelationship(relationshipName);

            //Relationships can only be directly accessed by public id, so we get them all and iterate to find the sentence form
            if (rel == null) {
                foreach (Relationship possibleRelationship in relationships)
                {
                    if (possibleRelationship.GetText().Equals(relationshipName)) rel = possibleRelationship;
                }
            }
            if (rel == null) return "";
            //Debug - return rel.GetName();
            
            //Now we need to find a list of entity Instances that are all the know instances on the far end of the relationship for our specific entity innstance start point
            targets = rel.GetKnownTargets(ei);

            //Now we iterate the instances to obtain the attribute - again we have to get the enity from th instance, then we can gat the attribute
            foreach (EntityInstance targetEntity in targets)
            {
                currEntity = targetEntity.GetEntity();
                
                // First see if we can get the attibute using a public name
                RBAttr currAttr = null;
                currAttr = currEntity.GetAttribute(attributeName);

                //We use an iteator so we can match by text name and so we can exit early if we get a match, obviously a lot slower!
                if (currAttr == null)
                {
                    attributes = currEntity.GetAttributes();
                    Iterator iAttributes = attributes.Iterator();

                    while (currAttr == null && iAttributes.HasNext())
                    {
                        RBAttr a = (RBAttr)iAttributes.Next();
                        if (a.GetText(SentenceForm.BASIC).Equals(attributeName)) currAttr = a;

                    }
                }
                if (currAttr == null) return "";

                Object attributeValue = currAttr.GetValue(targetEntity);
                if (attributeValue != null && attributeValue != Uncertain.INSTANCE   )
                {
                    if (result == null || result == "")
                    {
                        result = attributeValue.ToString();
                    }
                    else
                    {
                        result += (delimiter + attributeValue.ToString());
                    }
                }
            }




            return result;

        }

        public override Boolean RequireKnownParameters()
        {
            return false;
        }

    }






    /*
    *
    *
    */

    public class ExtractPropertyKeyOrValue : CustomFunction
    {
        public override object Evaluate(EntityInstance instance, object[] os)
        {
            String propertyString = ((String)os[0]).Trim();
            Boolean getKey = ((Oracle.Determinations.Masquerade.Lang.Boolean)os[1]).BooleanValue();

            if (propertyString.Equals("")) return Uncertain.INSTANCE;
            if (!propertyString.Contains("=")) return Uncertain.INSTANCE;
            int pos = propertyString.IndexOf("=");
            if (pos < 0) return Uncertain.INSTANCE;

            if (getKey)
            {
                if (pos == 0) return "";  // there is no key name
                return propertyString.Split('=')[0].Trim();
            }
            else
            {
                if (pos == propertyString.Length - 1) return "";  // there is no value
                return propertyString.Split('=')[1].Trim();
            }

        }

    }


    /**
     * OPA Extension to return the length of the longest word in a string.  Unknown and uncertain are 0
     */
    public class LongestWord : CustomFunction
    {


        public override object Evaluate(EntityInstance instance, object[] os)
        {
            int longest = 0;
            int letterCount = 0;
            int f;

            if (os[0] == null) return new Oracle.Determinations.Masquerade.Lang.Double(0.0);
            if (os[0] == Uncertain.INSTANCE) return new Oracle.Determinations.Masquerade.Lang.Double(0.0);
            String w = ((String)os[0]).Trim();
            if (w.Length < 1) return new Oracle.Determinations.Masquerade.Lang.Double(0.0);

            for (f = 0; f < w.Length; f++)
            {
                if (char.IsWhiteSpace(w[f]))
                {
                    if (letterCount > longest) longest = letterCount;
                    letterCount = 0;
                }
                else
                {
                    letterCount++;
                }
                if (letterCount > longest) longest = letterCount;
            }
            return new Oracle.Determinations.Masquerade.Lang.Double(longest);

        }

        public override Boolean RequireKnownParameters()
        {
            return false;
        }

    }


  
     /* OPA Extension to return async version string based on 4 numerics.  This is so that 
     * when deployed as a service we can easily verify that the extensions are loaded
     */
    public class GetVersionString : CustomFunction
        {


            public override object Evaluate(EntityInstance instance, object[] os)
            {
                string s = ((Oracle.Determinations.Masquerade.Lang.Double)os[0]).IntValue() + "." +
                    ((Oracle.Determinations.Masquerade.Lang.Double)os[1]).IntValue() + "." +
                    ((Oracle.Determinations.Masquerade.Lang.Double)os[2]).IntValue() + "." +
                    ((Oracle.Determinations.Masquerade.Lang.Double)os[3]).IntValue();
            return s;

            }
    }




    /**
     * OPA Extension to return if a text contains any visible characters i.e. not whitespace.  Can be
     * configured with the second parameter to either rturn false if ther text is unknown/uncertain or unknown/uncertain
     *
     */
    public class ContainsVisibleCharacters : CustomFunction
    {


        public override object Evaluate(EntityInstance instance, object[] os)
        {
            bool handleUncertainUnknown = ((Oracle.Determinations.Masquerade.Lang.Boolean)os[1]).BooleanValue();
            Oracle.Determinations.Masquerade.Lang.Boolean retval = Oracle.Determinations.Masquerade.Lang.Boolean.FALSE;
            
            if (!handleUncertainUnknown)
            {
                if (os[0] == null) return os[0];
                if (os[0] == Uncertain.INSTANCE) return os[0];
            }

            // we replace unlnwn/uncertain with false

            if (os[0] == null) return retval;
            if (os[0] == Uncertain.INSTANCE) return retval;



            String w = ((String)os[0]).Trim();
            if (w.Length < 1) return retval;
            return Oracle.Determinations.Masquerade.Lang.Boolean.TRUE;


        }

        public override Boolean RequireKnownParameters()
        {
            return false;
        }

    }

    /**
    * OPA Extension that simply returns the version number of the text library that is deployed.
    * Takes no parameters
    */
    public class GetTextLibraryVersion : CustomFunction
    {


        public override object Evaluate(EntityInstance instance, object[] os)
        {
            return "1.6.0";


        }

        

    }



}
