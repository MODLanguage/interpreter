package uk.modl.transforms;

import io.vavr.Function1;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class StarClassTransform implements Function1<Pair, Pair> {

    /**
     * The context for this invocation of the interpreter
     */
    @NonNull
    private TransformationContext ctx;

    /**
     * Check whether the key represents a *class instruction
     *
     * @param key the String to check
     * @return true if the key represents a class instruction
     */
    public static boolean isClassInstruction(final String key) {
        final String lowerCase = key.toLowerCase();
        return lowerCase
                .equals("*c") || lowerCase
                .equals("*class");
    }


    /**
     * Applies this function to one argument and returns the result.
     *
     * @param p argument 1
     * @return the result of function application
     */
    public Pair apply(final Pair p) {
        if (isClassInstruction(p.getKey())) {
            accept(p);
            return null;
        }
        return p;
    }

    public void setCtx(final TransformationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Extract a Class instruction from a Pair
     *
     * @param pair the Pair
     */
    private void accept(final Pair pair) {
        if (pair.getValue() instanceof Map) {
            String id = null;
            String name = null;
            String superclass = null;
            Vector<ArrayItem> assign = null;
            Vector<Pair> pairs = Vector.empty();

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
                        case "*s":
                        case "*superclass":
                            superclass = p.getValue()
                                    .toString();
                            break;
                        case "*a":
                        case "*assign":
                            if (p.getValue() instanceof Array) {
                                assign = ((Array) p.getValue()).getArrayItems()
                                        .map(ai -> {
                                            if (ai instanceof Array) {
                                                return ai;
                                            } else {
                                                throw new InterpreterError("*assign statement should be an Array of Arrays");
                                            }
                                        });
                            } else {
                                throw new InterpreterError("*assign statement should be an Array of Arrays");
                            }
                            break;
                        default:
                            pairs = pairs.append(p);
                    }
                } else {
                    throw new InterpreterError("Expected a Pair but found a " + mi.getClass());
                }
            }

            final ClassInstruction c = new ClassInstruction(id, name, superclass, assign, pairs);
            ctx.addClassInstruction(c);
        } else {
            throw new InterpreterError("Expected a map for " + pair.getKey() + " but found a " + pair.getValue()
                    .getClass());
        }
    }

    @Value
    static class ClassInstruction {
        String id;
        String name;
        String superclass;
        Vector<ArrayItem> assign;
        Vector<Pair> pairs;

        ClassInstruction(final String id, final String name, final String superclass, final Vector<ArrayItem> assign, final Vector<Pair> pairs) {
            this.id = id;
            this.name = name;
            this.superclass = superclass;
            this.assign = assign;
            this.pairs = pairs;
        }
    }
}
