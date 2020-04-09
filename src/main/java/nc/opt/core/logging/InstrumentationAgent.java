package nc.opt.core.logging;

import java.lang.instrument.Instrumentation;

/**
 * Classe pertant de calculer la taille d'un objet
 * Remplace ObjectSizeCalculator non supporter par OpenJDK
 */
public class InstrumentationAgent {

    private static volatile Instrumentation globalInstrumentation;

    public static void premain(final String agentArgs, final Instrumentation inst) {
        globalInstrumentation = inst;
    }

    public static long getObjectSize(final Object object) {
        if (globalInstrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        return globalInstrumentation.getObjectSize(object);
    }
}
