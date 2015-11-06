package de.teamrocket.relaxo;

import de.teamrocket.relaxo.util.ThreadSafePythonInterpreter;

import org.junit.Test;
import org.python.core.Py;
import org.python.core.PyObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class PythonInterpreterTest {

    @Test
    public void test() {
        ThreadSafePythonInterpreter interpreter = new ThreadSafePythonInterpreter();
        Map<String, Object> variables = new HashMap<>();
        variables.put("fun", "SWT");
        variables.put("zahl", 17);
        String code = "fun=='SWT' and zahl==17";
        PyObject result = interpreter.eval(code, variables);
        assertTrue(Py.py2boolean(result));
    }
    
    @Test
    public void test2() {
        ThreadSafePythonInterpreter interpreter = new ThreadSafePythonInterpreter();
        Map<String, Object> variables = new HashMap<>();
        variables.put("Datum", new Date());
        String script = "print str(Datum)";
        interpreter.exec(script, variables);
    }
}
