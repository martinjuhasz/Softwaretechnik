package de.teamrocket.relaxo.util;

import java.util.Map;
import java.util.Map.Entry;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 * Ein Thread-Sicherer Python-Interpreter.
 */
public class ThreadSafePythonInterpreter {

    // Vars

    /**
     * Der PythonInterpreter.
     */
    private final PythonInterpreter interpreter;

    // Construct

    /**
     * Konstruktor des ThreadSafePythonInterpreter, welches den PythonInterpreter instanziiert.
     */
    public ThreadSafePythonInterpreter() {
        this.interpreter = new PythonInterpreter();
        
    }

    // Methods

    /**
     * Evaluiert den übergebenen Code mit den entsprechenden Parametern und gibt ein PyObject zurück.
     * @param code Der Python-Code.
     * @param variables Eine Map mit Variablen.
     * @return Das PyObject.
     */
    public synchronized PyObject eval(final String code, final Map<String, Object> variables) {
        for (Entry<String, Object> entry : variables.entrySet()) {
            interpreter.set(entry.getKey(), entry.getValue());
        }
        PyObject result = interpreter.eval(code);

        interpreter.cleanup();
        return result;
    }
    
    /**
     * Führt den übergebenen Code mit den entsprechenden Parametern aus.
     * @param code Der Python-Code.
     * @param variables Eine Map mit Variablen.
     */
    public synchronized void exec(final String code, final Map<String, Object> variables) {
        for (Entry<String, Object> entry : variables.entrySet()) {
            interpreter.set(entry.getKey(), entry.getValue());
        }
        interpreter.exec(code);

        interpreter.cleanup();
    }
}