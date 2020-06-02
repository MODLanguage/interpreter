package uk.modl.transforms;

import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;
import uk.modl.parser.errors.InterpreterError;

@RequiredArgsConstructor
public class StarClassTransform implements Function2<TransformationContext, Structure, Tuple2<TransformationContext, Structure>> {

    private final ReferencesTransform referencesTransform = new ReferencesTransform();

    /**
     * Check whether the key represents a *class instruction
     *
     * @param key the String to check
     * @return true if the key represents a class instruction
     */
    public static boolean isClassInstruction(final @NonNull String key) {
        return StringUtils.equalsAnyIgnoreCase(key, "*c", "*class");
    }


    /**
     * Applies this function to one argument and returns the result.
     *
     * @param p argument 1
     * @return the result of function application
     */
    public Tuple2<TransformationContext, Structure> apply(final TransformationContext ctx, final @NonNull Structure p) {
        if (p instanceof Pair) {
            final Pair pair = (Pair) p;
            if (isClassInstruction(pair.getKey())) {
                final TransformationContext updatedContext = accept(ctx, pair);
                return Tuple.of(updatedContext, null);
            }
        }
        return Tuple.of(ctx, p);
    }

    /**
     * Extract a Class instruction from a Pair
     *
     * @param pair the Pair
     * @return TransformationContext
     */
    private TransformationContext accept(final TransformationContext ctx, final @NonNull Pair pair) {
        if (pair.getValue() instanceof Map) {
            String id = null;
            String name = null;
            String superclass = null;
            Vector<ArrayItem> assign = Vector.empty();
            io.vavr.collection.Map<String, Pair> pairs = LinkedHashMap.empty();

            for (final MapItem mi : ((Map) pair.getValue()).getMapItems()) {
                if (mi instanceof Pair) {
                    final Pair p = (Pair) mi;
                    switch (p.getKey()
                            .toLowerCase()) {
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
                            if (p.getValue()
                                    .toString()
                                    .contains("%")) {
                                final Tuple2<TransformationContext, Structure> result = referencesTransform.apply(ctx, (Structure) p);
                                pairs = pairs.put(p.getKey(), (Pair) result._2);
                            } else {
                                pairs = pairs.put(p.getKey(), p);
                            }
                    }
                } else {
                    throw new InterpreterError("Expected a Pair but found a " + mi.getClass());
                }
            }

            return ctx.addClassInstruction(ClassInstruction.of(id, name, superclass, assign, pairs));
        } else {
            throw new InterpreterError("Expected a map for " + pair.getKey() + " but found a " + pair.getValue()
                    .getClass());
        }
    }

    @Value(staticConstructor = "of")
    public static class ClassInstruction {

        @NonNull
        String id;

        String name;

        String superclass;

        @NonNull
        Vector<ArrayItem> assign;

        @NonNull
        io.vavr.collection.Map<String, Pair> pairs;

        final String getNameOrId() {
            return (name == null) ? id : name;
        }

    }

}
