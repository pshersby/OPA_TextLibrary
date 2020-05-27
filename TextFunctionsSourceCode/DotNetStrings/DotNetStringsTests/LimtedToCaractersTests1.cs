using Microsoft.VisualStudio.TestTools.UnitTesting;
using PKS.OPA.Extensions.Text;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Oracle.Determinations.Engine;

namespace PKS.OPA.Extensions.Text.Tests
{
    [TestClass()]
    public class LimtedToCharactersTests1
    {
        [TestMethod()]
        public void EvaluateLimitedToCharacterTest()
        {
            Object[] os = { "+44 1472314 eryrtgfu333", "0123456789" };
            Object expResult = "441472314333";
            CustomFunction Instance = new LimitedToCharacters();
            Object result = Instance.Evaluate(null, os);

            Assert.AreEqual(expResult, result);
        }
    }
}