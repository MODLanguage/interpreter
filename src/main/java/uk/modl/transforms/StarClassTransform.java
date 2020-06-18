/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.transforms;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import uk.modl.model.*;

@RequiredArgsConstructor
public class StarClassTransform {

    private static final Vector<String> RESERVED_CLASS_NAMES = Vector.of(
            "arr",
            "map",
            "str",
            "num",
            "bool",
            "null"
    );

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
     * @param ctx TransformationContext
     * @param p   Structure
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
                            assign = extractAssign(p);
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
                    throw new RuntimeException("Expected a Pair but found a " + mi.getClass());
                }
            }

            validateAssign(assign);

            if (id != null && RESERVED_CLASS_NAMES.contains(id.toLowerCase())) {
                throw new RuntimeException("Reserved class id - cannot redefine: " + id);
            }
            if (name != null && RESERVED_CLASS_NAMES.contains(name.toLowerCase())) {
                throw new RuntimeException("Reserved class name - cannot redefine: " + name);
            }
            TransformationContext newCtx = ctx.addClassInstruction(ClassInstruction.of(id, name, superclass, assign, pairs));
            if (pair.getKey()
                    .startsWith("*C")) {
                newCtx = newCtx.withStarClassImmutable(true);
            }
            return newCtx;
        } else {
            throw new RuntimeException("Expected a map for " + pair.getKey() + " but found a " + pair.getValue()
                    .getClass());
        }
    }

    private Vector<ArrayItem> extractAssign(final Pair p) {
        final Vector<ArrayItem> assign;
        if (p.getValue() instanceof Array) {
            assign = ((Array) p.getValue()).getArrayItems()
                    .map(ai -> {
                        if (ai instanceof Array) {
                            return ai;
                        } else {
                            throw new RuntimeException("*assign statement should be an Array of Arrays");
                        }
                    });
        } else {
            throw new RuntimeException("*assign statement should be an Array of Arrays");
        }
        return assign;
    }

    private void validateAssign(final Vector<ArrayItem> assigns) {
        int lastLen = 0;
        for (final ArrayItem assign : assigns) {
            final Array array = (Array) assign;
            @NonNull final Vector<ArrayItem> arrayItems = array.getArrayItems();
            if (arrayItems.size() <= lastLen) {
                final Vector<String> strings = arrayItems.map(ai -> "\"" + ai.toString() + "\"")
                        .intersperse(", ");
                final String arrayStr = strings.foldLeft("[", (l, r) -> l + r) + "]";
                throw new RuntimeException("Error: Key lists in *assign are not in ascending order of list length: " + arrayStr);
            }
            lastLen = arrayItems.size();
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
