/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.ow2.parscript.PARScriptEngine;
import org.ow2.parscript.PARScriptFactory;
import org.ow2.proactive.scripting.ScriptResult;
import org.ow2.proactive.scripting.SimpleScript;
import org.ow2.proactive.scripting.TaskScript;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests that the message of stop() is printed in the error writer
 *
 * @author Activeeon Team
 */
public class TestStopInScript {

    @Test
    public void test() throws Exception {
        HashMap<String, Serializable> variablesMap = new HashMap<String, Serializable>(1);
        variablesMap.put("toto", "badValue");

        String errorMessage = "bad input";
        String expectedVariableValue = "goodValue";

        String rScript = "variables$toto='" + expectedVariableValue + "';warning('attention');stop('" + errorMessage + "');";

        Map<String, Object> aBindings = Collections.singletonMap(PARScriptEngine.TASK_SCRIPT_VARIABLES, (Object) variablesMap);

        SimpleScript ss = new SimpleScript(rScript, PARScriptFactory.ENGINE_NAME);
        TaskScript taskScript = new TaskScript(ss);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ScriptResult<Serializable> res = taskScript.execute(aBindings,
          System.out, new PrintStream(output));

        Assert.assertTrue("The script exception must contain the message of stop() function", res.getException().getMessage().contains(errorMessage));
        Assert.assertTrue("The script error output must contain the stop message", output.toString().contains(errorMessage));
        Assert.assertEquals("Even if the script is stoppped with stop() all changes in the variables map must be done", expectedVariableValue, variablesMap.get("toto"));

    }

}
