package uk.modl.transforms;

import io.vavr.Function1;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.Map;
import uk.modl.model.MapItem;
import uk.modl.model.Pair;
import uk.modl.model.Structure;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class StarMethodTransform implements Function1<Structure, Structure> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Check whether the key represents a *method instruction
     *
     * @param key the String to check
     * @return true if the key represents a method instruction
     */
    public static boolean isMethodInstruction(final String key) {
        final String lowerCase = key.toLowerCase();
        return lowerCase
                .equals("*m") || lowerCase
                .equals("*method");
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param structure argument 1
     * @return the result of function application
     */
    @Override
    public Structure apply(final Structure structure) {
        if (structure instanceof Pair) {
            final Pair p = (Pair) structure;
            if (isMethodInstruction(p.key)) {
                accept(p);
            }
        }
        return structure;
    }

    public void seCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Extract a Method instruction from a Pair
     *
     * @param pair the Pair
     */
    public void accept(final Pair pair) {
        if (isMethodInstruction(pair.key)) {
            if (pair.value instanceof Map) {
                String name = null;
                String id = null;
                String transform = null;

                for (final MapItem mi : ((Map) pair.value).mapItems) {
                    if (mi instanceof Pair) {
                        final Pair p = (Pair) mi;
                        if (p.key.equals("*i") || p.key.equals("*id")) {
                            id = p.value.toString();
                        }
                        if (p.key.equals("*n") || p.key.equals("*name")) {
                            name = p.value.toString();
                        }
                        if (p.key.equals("*t") || p.key.equals("*transform")) {
                            transform = p.value.toString();
                        }
                    } else {
                        throw new InterpreterError("Expected a Pair but found a " + mi.getClass());
                    }
                }

                final MethodInstruction m = new MethodInstruction(id, name, transform);
                ctx.addMethodInstruction(m);
            } else {
                throw new InterpreterError("Expected a map for " + pair.key + " but found a " + pair.value.getClass());
            }
        }
    }

    @RequiredArgsConstructor
    static class MethodInstruction {
        public final String id;
        public final String name;
        public final String transform;
    }

}
