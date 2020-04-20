package uk.modl.transforms;

import io.vavr.Function1;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.Map;
import uk.modl.model.MapItem;
import uk.modl.model.Pair;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class StarMethodTransform implements Function1<Pair, Pair> {

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
     * @param p argument 1
     * @return the result of function application
     */
    public Pair apply(final Pair p) {
        if (p != null && isMethodInstruction(p.getKey())) {
            accept(p);
            return null;
        }
        return p;
    }

    public void setCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Extract a Method instruction from a Pair
     *
     * @param pair the Pair
     */
    private void accept(final Pair pair) {
        if (pair.getValue() instanceof Map) {
            String name = null;
            String id = null;
            String transform = null;

            for (final MapItem mi : ((Map) pair.getValue()).getMapItems()) {
                if (mi instanceof Pair) {
                    final Pair p = (Pair) mi;
                    switch (p.getKey()) {
                        case "*i":
                        case "*id":
                            id = p.getValue()
                                    .toString();
                            break;
                        case "*n":
                        case "*name":
                            name = p.getValue()
                                    .toString();
                            break;
                        case "*t":
                        case "*transform":
                            transform = p.getValue()
                                    .toString();
                            break;
                    }
                } else {
                    throw new InterpreterError("Expected a Pair but found a " + mi.getClass());
                }
            }

            final MethodInstruction m = new MethodInstruction(id, name, transform);
            ctx.addMethodInstruction(m);
        } else {
            throw new InterpreterError("Expected a map for " + pair.getKey() + " but found a " + pair.getValue()
                    .getClass());
        }
    }

    @RequiredArgsConstructor
    static class MethodInstruction {
        public final String id;
        public final String name;
        public final String transform;
    }

}
