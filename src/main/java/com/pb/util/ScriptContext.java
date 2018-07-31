package com.pb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 *
 */
public class ScriptContext {

    private static Logger logger = LoggerFactory.getLogger(ScriptContext.class);

    private final String script;
    private final ScriptEvaluator scriptEvaluator;

    public ScriptContext(String script) {
        this.script = script;
        this.scriptEvaluator = createNewScriptEvaluator("javascript");
    }

    /**
     * Evaluate Script Expression
     * @param bindings
     * @return
     */
    public Object evaluateScriptExpression(Map<String, Object> bindings) {
        Object result = null;

        try {
            result = scriptEvaluator.evaluate(script, bindings);
        } catch (ScriptException e) {
            logger.error("Error during execution of script \"{}\" occurred.", script);
        }

        return result;
    }


    /**
     * Creates a new script executor for the given language.
     * @param languageName A JSR 223 language name.
     * @return A newly created script executor for the given language.
     * @throws ScriptException In case no JSR 223 compatible engine for the given language could be found.
     */
    private ScriptEvaluator createNewScriptEvaluator(String languageName) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName(languageName);

        if (engine == null) {
            logger.error("No JSR 223 script engine found for language \"{}\".", languageName);
        }

        return new ScriptEvaluator(engine);
    }
}
