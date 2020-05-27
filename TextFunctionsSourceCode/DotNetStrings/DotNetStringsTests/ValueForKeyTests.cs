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
    public class ValueForKeyTests
    {
        [TestMethod()]
        public void EvaluateValueForKeyTest()
        {
  
            Object[] os = { "RC=1:COMPQ=1:BF=REALL AN :X3=17", "BF", ":" };
            ValueForKey Instance = new ValueForKey();
            Object result = Instance.Evaluate(null, os);

            Assert.AreEqual("REALL AN", result);
        }
    }
}