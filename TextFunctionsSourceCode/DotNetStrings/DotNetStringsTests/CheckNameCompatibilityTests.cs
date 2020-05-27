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
    public class CheckNameCompatibilityTests
    {
        [TestMethod()]
        public void EvaluateTest()
        {
            string name1 = "Ms";
            string name2 = "John Smith";
            Oracle.Determinations.Masquerade.Lang.Boolean isPerson = Oracle.Determinations.Masquerade.Lang.Boolean.TRUE;
            Object[] os = { name1, name2, isPerson };
            CheckNameCompatibility Instance = new CheckNameCompatibility();
            Object result = Instance.Evaluate(null, os);

            Assert.IsFalse(System.Convert.ToBoolean(result));
        }

        [TestMethod()]
        public void EvaluateTest1()
        {

        }
    }
}