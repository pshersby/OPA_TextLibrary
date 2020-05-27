using Microsoft.VisualStudio.TestTools.UnitTesting;
using PKS.OPA.Extensions.Text;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PKS.OPA.Extensions.Text.Tests
{
    [TestClass()]
    public class CheckBankNameCompatibilityTests
    {
        [TestMethod()]
        public void EvaluateTest()
        {
            string name1 = "John Tony Smith";
            string name2 = "John Smith";
            Oracle.Determinations.Masquerade.Lang.Boolean isPerson = Oracle.Determinations.Masquerade.Lang.Boolean.TRUE;
            Object[] os = { name1, name2, "PERSON", "GBP" };
            CheckBankNameCompatibility Instance = new CheckBankNameCompatibility();
            Object result = Instance.Evaluate(null, os);

            Assert.IsTrue(System.Convert.ToBoolean(result));
        }

        [TestMethod()]
        public void EvaluateTestGBP()
        {
            string name1 = "John Tony Smith";
            string name2 = "John Smith GBP";
            Oracle.Determinations.Masquerade.Lang.Boolean isPerson = Oracle.Determinations.Masquerade.Lang.Boolean.TRUE;
            Object[] os = { name1, name2, "PERSON", "GBP" };
            CheckBankNameCompatibility Instance = new CheckBankNameCompatibility();
            Object result = Instance.Evaluate(null, os);

            Assert.IsTrue(System.Convert.ToBoolean(result));
        }

        [TestMethod()]
        public void EvaluateTestBrackets()
        {
            string name1 = "Atom Filters (UK) Limited";
            string name2 = "Atom Filters LTD";
            
            Object[] os = { name1, name2, "COMPANY", "GBP" };
            CheckBankNameCompatibility Instance = new CheckBankNameCompatibility();
            Object result = Instance.Evaluate(null, os);

            Assert.IsTrue(System.Convert.ToBoolean(result));
        }

        [TestMethod()]
        public void EvaluateTestFirstLast()
        {
            string name1 = "John Tony Smith";
            string name2 = "Smith, John";
            Oracle.Determinations.Masquerade.Lang.Boolean isPerson = Oracle.Determinations.Masquerade.Lang.Boolean.TRUE;
            Object[] os = { name1, name2, "PERSON", "GBP" };
            CheckBankNameCompatibility Instance = new CheckBankNameCompatibility();
            Object result = Instance.Evaluate(null, os);

            Assert.IsTrue(System.Convert.ToBoolean(result));
        }

    }
}