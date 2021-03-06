package org.ow2.parscript;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import static javax.script.ScriptEngine.ENGINE;
import static javax.script.ScriptEngine.LANGUAGE;
import static javax.script.ScriptEngine.NAME;

/**
 * R implementation of ScriptEngineFactory for the ProActive Scheduler Worker.
 *
 * @author Activeeon Team
 */
public final class PARScriptFactory implements ScriptEngineFactory {

    public static final String ENGINE_NAME = "R";
    private static final String R_ENGINE_VERSION = "1";
    private static final String R_LANGUAGE_NAME = "R";
    private static final String R_LANGUAGE_VERSION = "2";
    private static final List<String> R_FILE_EXTENSIONS = Arrays.asList("R", "r", "parscript");
    private static final List<String> ENGINE_NAMES = Arrays.asList("R", "r", "parscript");
    private static final List<String> R_MIME_TYPES = Collections.singletonList("text/x-R");

    @Override
    public String getEngineName() {
        return ENGINE_NAME;
    }

    @Override
    public List<String> getNames() {
        return ENGINE_NAMES;
    }

    @Override
    public Object getParameter(String key) {
        if (key == null) {
            return null;
        }
        if (key.equals(NAME)) {
            return ENGINE_NAME;
        } else if (key.equals(ENGINE)) {
            return getEngineName();
        } else if (key.equals(ScriptEngine.ENGINE_VERSION)) {
            return getEngineVersion();
        } else if (key.equals(LANGUAGE)) {
            return getLanguageName();
        } else if (key.equals(ScriptEngine.LANGUAGE_VERSION)) {
            return getLanguageVersion();
        } else {
            return null;
        }
    }

    @Override
    public ScriptEngine getScriptEngine() {
        try {
            return PARScriptEngine.create(this);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create the PARScriptEngine", e);
        }
    }

    @Override
    public String getEngineVersion() {
        return R_ENGINE_VERSION;
    }

    @Override
    public List<String> getExtensions() {
        return R_FILE_EXTENSIONS;
    }

    @Override
    public List<String> getMimeTypes() {
        return R_MIME_TYPES;
    }

    @Override
    public String getLanguageName() {
        return R_LANGUAGE_NAME;
    }

    @Override
    public String getLanguageVersion() {
        return R_LANGUAGE_VERSION;
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        StringBuilder ret = new StringBuilder(m + "(" + obj);
        for (String arg : args) {
            ret.append(",").append(arg);
        }
        ret.append(");");
        return ret.toString();
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return "cat('" + toDisplay + "');";
    }

    @Override
    public String getProgram(String... statements) {
        StringBuilder ret = new StringBuilder();
        for (String str : statements) {
            ret.append(str).append(";\n");
        }
        return ret.toString();
    }
}
