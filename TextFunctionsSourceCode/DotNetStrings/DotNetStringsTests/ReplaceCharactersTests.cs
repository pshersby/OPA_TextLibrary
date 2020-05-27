using Microsoft.VisualStudio.TestTools.UnitTesting;
using Oracle.Determinations.Engine;
using PKS.OPA.Extensions.Text;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PKS.OPA.Extensions.Text.Tests
{
    [TestClass()]
    public class ReplaceCharactersTests
    {
        [TestMethod()]
        public void EvaluateReplaceCharacterTest()
        {
            Object[] os = { "(1234)-[567+8]", "[,],+,-,(,)", "","," };
            Object expResult = "12345678";
            CustomFunction Instance = new ReplaceCharacters();
            Object result = Instance.Evaluate(null, os);

            Assert.AreEqual(expResult, result);
        }


        [TestMethod()]
        public void EvaluateReplaceCharacterTestcountryCode()
        {
            Object[] os = { "+4412345678", "+44", "0", "," };
            Object expResult = "012345678";
            CustomFunction Instance = new ReplaceCharacters();
            Object result = Instance.Evaluate(null, os);

            Assert.AreEqual(expResult, result);
        }


    }
}