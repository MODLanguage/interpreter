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
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.modl.model.*;
import uk.modl.utils.Util;

@RequiredArgsConstructor
public class ConditionalsTransform {

    private final StarLoadTransform starLoadTransform;

    private final ReferencesTransform referencesTransform;

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param ctx TransformationContext
     * @param tlc argument 1
     * @return the result of function application
     */
    public Tuple2<TransformationContext, Structure> apply(final TransformationContext ctx, final TopLevelConditional tlc) {
        if (tlc.getTests()
                .size() == 1) {
            if (evaluate(ctx, tlc.getTests()
                    .get(0))) {
                Vector<Structure> vector = Vector.empty();

                TransformationContext newCtx = ctx;
                for (final Structure structure : tlc.getReturns()
                        .get(0)
                        .getStructures()) {
                    final Tuple2<TransformationContext, Structure> refsResult = referencesTransform.apply(newCtx, structure);
                    newCtx = refsResult._1;

                    vector = vector.append(refsResult._2);
                }

                return getToplevelConditionalResult(newCtx, tlc, vector);
            } else {
                if (tlc.getReturns()
                        .size() > 1) {
                    Vector<Structure> vector = Vector.empty();

                    TransformationContext newCtx = ctx;
                    for (final Structure structure : tlc.getReturns()
                            .get(1)
                            .getStructures()) {
                        final Tuple2<TransformationContext, Structure> refsResult = referencesTransform.apply(newCtx, structure);
                        newCtx = refsResult._1;

                        vector = vector.append(refsResult._2);
                    }

                    return getToplevelConditionalResult(newCtx, tlc, vector);
                }
            }
        } else {
            int i = 0;
            TransformationContext newCtx = ctx;
            Vector<Structure> structures = Vector.empty();

            for (final ConditionTest test : tlc.getTests()) {
                if (evaluate(ctx, test)) {

                    @NonNull final Vector<Structure> vector = tlc.getReturns()
                            .get(i)
                            .getStructures();

                    for (final Structure structure : vector) {
                        final Tuple2<TransformationContext, Structure> result = handleNestedTopLevelConditionals(newCtx, structure);
                        newCtx = result._1;
                        final Tuple2<TransformationContext, Structure> refsResult = referencesTransform.apply(newCtx, result._2);
                        newCtx = refsResult._1;
                        structures = structures.append(refsResult._2);
                    }

                    newCtx = conditionalFileLoading(newCtx, structures);

                }
                i += 1;
            }
            return Tuple.of(newCtx, tlc.with(ctx.getAncestry(), structures));
        }
        return Tuple.of(ctx, tlc);
    }

    private Tuple2<TransformationContext, Structure> getToplevelConditionalResult(final TransformationContext ctx, final TopLevelConditional tlc, final @NonNull Vector<Structure> vector) {
        TransformationContext newCtx = ctx;
        Vector<Structure> structures = Vector.empty();

        for (final Structure structure : vector) {
            final Tuple2<TransformationContext, Structure> result = handleNestedTopLevelConditionals(newCtx, structure);
            newCtx = result._1;
            structures = structures.append(result._2);
        }

        newCtx = conditionalFileLoading(newCtx, structures);

        return Tuple.of(newCtx, tlc.with(ctx.getAncestry(), structures));
    }

    private TransformationContext conditionalFileLoading(final TransformationContext ctx, final Vector<Structure> structures) {

        TransformationContext newCtx = ctx;
        for (final Structure structure : structures) {
            final Tuple2<TransformationContext, Structure> loadResult = starLoadTransform.apply(newCtx, structure);
            newCtx = loadResult._1;
        }

        return newCtx;

    }

    private Tuple2<TransformationContext, Structure> handleNestedTopLevelConditionals(final TransformationContext ctx, final Structure structure) {
        if (structure instanceof TopLevelConditional) {
            return apply(ctx, (TopLevelConditional) structure);
        }
        if (structure instanceof Pair) {
            final Pair p = (Pair) structure;
            @NonNull final PairValue value = p.getValue();

            if (value instanceof ValueConditional) {
                final ValueConditional result = apply(ctx, (ValueConditional) value);
                final Pair resultPair = p.with(ctx.getAncestry(), result);
                return Tuple.of(ctx, resultPair);
            }
        }
        return Tuple.of(ctx, structure);
    }

    private boolean evaluate(final TransformationContext ctx, final ConditionTest test) {
        final Vector<Tuple2<Boolean, String>> partial = test.getConditions()
                .map(cond -> evaluate(ctx, cond));
        return evaluate(partial);
    }

    private Tuple2<Boolean, String> evaluate(final TransformationContext ctx, final Tuple2<ConditionOrConditionGroupInterface, String> tuple) {
        if (tuple._1 instanceof Condition) {
            return Tuple.of(evaluate(ctx, (Condition) tuple._1), tuple._2);
        } else {
            return Tuple.of(evaluate(ctx, (ConditionGroup) tuple._1), tuple._2);
        }
    }

    private boolean evaluate(final TransformationContext ctx, final ConditionGroup cg) {

        final Vector<Tuple2<Boolean, String>> partial = cg.getSubConditionList()
                .map(sc -> Tuple.of(evaluate(ctx, sc._1), sc._2));

        return (cg.isShouldNegate()) != evaluate(partial);
    }

    private boolean evaluate(final Vector<Tuple2<Boolean, String>> partial) {
        boolean first = true;
        boolean result = false;
        String lastOp = null;
        for (Tuple2<Boolean, String> partialItem : partial) {
            if (first) {
                first = false;
                result = partialItem._1;
            } else {
                switch (lastOp) {
                    case "&":
                        result = result && partialItem._1;
                        break;
                    case "|":
                        result = result || partialItem._1;
                        break;
                    default:
                        throw new RuntimeException("Invalid operation in conditional: " + lastOp);
                }
            }
            lastOp = partialItem._2;
        }
        return result;
    }

    private boolean evaluate(final TransformationContext ctx, final Condition c) {
        final Operator op = c.getOp();
        final boolean shouldNegate = c.isShouldNegate();

        final Primitive lhs = c.getLhs();

        @NonNull final Vector<ValueItem> values = c.getValues();

        if (isSingleValueConditional(op, lhs)) {
            return handleSingleValueConditional(ctx, c, shouldNegate, values);
        }
        if (op instanceof GreaterThanOperator) {
            return shouldNegate != Util.greaterThanAll(lhs, values);
        }
        if (op instanceof GreaterThanOrEqualsOperator) {
            return shouldNegate != Util.greaterThanOrEqualToAll(lhs, values);
        }
        if (op instanceof LessThanOperator) {
            return shouldNegate != Util.lessThanAll(lhs, values);
        }
        if (op instanceof LessThanOrEqualsOperator) {
            return shouldNegate != Util.lessThanOrEqualToAll(lhs, values);
        }

        final int count = countMatches(c, lhs);
        if (op instanceof EqualsOperator) {
            return shouldNegate != (count > 0);
        }
        if (op instanceof NotEqualsOperator) {
            return shouldNegate != (count == 0);
        }
        return shouldNegate;
    }

    private boolean isSingleValueConditional(final Operator op, final Primitive lhs) {
        return op == null && lhs == null || lhs.toString() == null;
    }

    private boolean handleSingleValueConditional(final TransformationContext ctx, final Condition c, final boolean shouldNegate, final @NonNull Vector<ValueItem> values) {
        // Is the RHS true?
        return shouldNegate != values.map(v -> {
            final Option<Boolean> simpleBoolean = maybeBoolean(v);
            if (simpleBoolean.isDefined()) {
                return simpleBoolean.get();
            }

            final String key = v.toString();

            if (v instanceof StringPrimitive) {
                final Pair pair = findReferencedPair(ctx, c, key);

                if (pair != null) {

                    @NonNull final PairValue pairValue = pair.getValue();
                    final Option<Boolean> simpleBoolean2 = maybeBoolean(pairValue);
                    if (simpleBoolean2.isDefined()) {
                        return simpleBoolean2.get();
                    }
                    if (pairValue instanceof ValueConditional) {
                        return checkValueConditionalResult((ValueConditional) pairValue);
                    }
                    return true;
                }
            }
            return false;
        })
                .getOrElse(false);
    }

    private Boolean checkValueConditionalResult(final ValueConditional pairValue) {
        return pairValue.getResult()
                .map(vcResult -> {
                    final Option<Boolean> simpleBoolean3 = maybeBoolean(vcResult);
                    if (simpleBoolean3.isDefined()) {
                        return simpleBoolean3.get();
                    }
                    return false;
                })
                .getOrElse(false);
    }

    private Option<Boolean> maybeBoolean(final Object o) {
        if (o instanceof TruePrimitive) {
            return Option.of(Boolean.TRUE);
        } else if (o instanceof FalsePrimitive || o instanceof NullPrimitive) {
            return Option.of(Boolean.FALSE);
        }
        return Option.none();
    }

    private Pair findReferencedPair(final TransformationContext ctx, final Condition c, final String key) {
        return ctx.getAncestry()
                .findReferencedPair(ctx, c, key);
    }

    private int countMatches(final Condition c, final ValueItem lhs) {
        return c.getValues()
                .count(v -> {
                    if (!v.toString()
                            .contains("*")) {
                        return v.toString()
                                .equals(lhs.toString());
                    } else {
                        final String regexStr = v.toString()
                                .replaceAll("\\*", ".*");
                        return c.getLhs()
                                .toString()
                                .matches(regexStr);
                    }
                });
    }

    public ValueConditional apply(final TransformationContext ctx, final ValueConditional vc) {
        if (vc.getTests()
                .size() == 1) {
            if (evaluate(ctx, vc.getTests()
                    .get(0))) {
                return handleValueConditionalTrue(ctx, vc);
            } else {
                return handleValueConditionalFalse(ctx, vc);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : vc.getTests()) {
                if (evaluate(ctx, test)) {

                    Vector<ValueItem> items = Vector.empty();

                    for (final ValueItem vi : vc.getReturns()
                            .get(i)
                            .getItems()
                            .map(nested -> handleNestedValueConditionals(ctx, nested))) {
                        final ValueItem refsResult = referencesTransform.apply(ctx, vi);

                        items = items.append(refsResult);
                    }
                    return vc.with(ctx.getAncestry(), items);
                }
                i += 1;
            }
        }
        return vc;
    }

    private ValueConditional handleValueConditionalFalse(final TransformationContext ctx, final ValueConditional vc) {
        if (vc.getReturns()
                .size() == 0) {
            return vc.with(ctx.getAncestry(), Vector.of(FalsePrimitive.instance));
        }

        Vector<ValueItem> items = Vector.empty();

        for (final ValueItem vi : vc.getReturns()
                .get(1)
                .getItems()
                .map(nested -> handleNestedValueConditionals(ctx, nested))) {
            final ValueItem refsResult = referencesTransform.apply(ctx, vi);

            items = items.append(refsResult);
        }
        return vc.with(ctx.getAncestry(), items);
    }

    private ValueConditional handleValueConditionalTrue(final TransformationContext ctx, final ValueConditional vc) {
        if (vc.getReturns()
                .size() == 0) {
            return vc.with(ctx.getAncestry(), Vector.of(TruePrimitive.instance));
        }

        Vector<ValueItem> items = Vector.empty();

        for (final ValueItem vi : vc.getReturns()
                .get(0)
                .getItems()
                .map(nested -> handleNestedValueConditionals(ctx, nested))) {
            final ValueItem refsResult = referencesTransform.apply(ctx, vi);

            items = items.append(refsResult);
        }

        return vc.with(ctx.getAncestry(), items);
    }

    private ValueItem handleNestedValueConditionals(final TransformationContext ctx, final ValueItem vi) {
        if (vi instanceof ValueConditional) {
            return apply(ctx, (ValueConditional) vi);
        }
        return vi;
    }

    private ArrayItem handleNestedArrayConditionals(final TransformationContext ctx, final ArrayItem vi) {
        if (vi instanceof ArrayConditional) {
            return apply(ctx, (ArrayConditional) vi);
        }
        return vi;
    }

    private MapItem handleNestedMapConditionals(final TransformationContext ctx, final MapItem vi) {
        if (vi instanceof MapConditional) {
            return apply(ctx, (MapConditional) vi);
        }
        return vi;
    }

    public ArrayConditional apply(final TransformationContext ctx, final ArrayConditional ac) {
        if (ac.getTests()
                .size() == 1) {
            if (evaluate(ctx, ac.getTests()
                    .get(0))) {
                if (ac.getReturns()
                        .size() == 0) {
                    return ac.with(ctx.getAncestry(), Vector.of(TruePrimitive.instance));
                }
                Vector<ArrayItem> items = Vector.empty();

                for (final ArrayItem ai : ac.getReturns()
                        .get(0)
                        .getItems()
                        .map(nested -> handleNestedArrayConditionals(ctx, nested))) {
                    final ValueItem refsResult = referencesTransform.apply(ctx, (ValueItem) ai);

                    items = items.append((ArrayItem) refsResult);
                }

                return ac.with(ctx.getAncestry(), items);
            } else {
                if (ac.getReturns()
                        .size() == 0) {
                    return ac.with(ctx.getAncestry(), Vector.of(FalsePrimitive.instance));
                }

                Vector<ArrayItem> items = Vector.empty();

                for (final ArrayItem ai : ac.getReturns()
                        .get(1)
                        .getItems()
                        .map(nested -> handleNestedArrayConditionals(ctx, nested))) {
                    final ValueItem refsResult = referencesTransform.apply(ctx, (ValueItem) ai);

                    items = items.append((ArrayItem) refsResult);
                }

                return ac.with(ctx.getAncestry(), items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : ac.getTests()) {
                if (evaluate(ctx, test)) {
                    Vector<ArrayItem> items = Vector.empty();

                    for (final ArrayItem ai : ac.getReturns()
                            .get(i)
                            .getItems()
                            .map(nested -> handleNestedArrayConditionals(ctx, nested))) {
                        final ValueItem refsResult = referencesTransform.apply(ctx, (ValueItem) ai);

                        items = items.append((ArrayItem) refsResult);
                    }

                    return ac.with(ctx.getAncestry(), items);
                }
                i += 1;
            }
        }
        return ac;
    }

    public MapConditional apply(final TransformationContext ctx, final MapConditional mc) {
        if (mc.getTests()
                .size() == 1) {
            if (evaluate(ctx, mc.getTests()
                    .get(0))) {
                if (mc.getReturns()
                        .size() == 0) {
                    return mc.with(ctx.getAncestry(), Vector.of((MapItem) TruePrimitive.instance));
                }
                Vector<MapItem> items = Vector.empty();

                for (final MapItem mi : mc.getReturns()
                        .get(0)
                        .getItems()
                        .map(nested -> handleNestedMapConditionals(ctx, nested))) {
                    final ValueItem refsResult = referencesTransform.apply(ctx, (ValueItem) mi);

                    items = items.append((MapItem) refsResult);
                }
                return mc.with(ctx.getAncestry(), items);
            } else {
                if (mc.getReturns()
                        .size() == 0) {
                    return mc.with(ctx.getAncestry(), Vector.of((MapItem) FalsePrimitive.instance));
                }
                Vector<MapItem> items = Vector.empty();

                for (final MapItem mi : mc.getReturns()
                        .get(1)
                        .getItems()
                        .map(nested -> handleNestedMapConditionals(ctx, nested))) {
                    final ValueItem refsResult = referencesTransform.apply(ctx, (ValueItem) mi);

                    items = items.append((MapItem) refsResult);
                }
                return mc.with(ctx.getAncestry(), items);
            }
        } else {
            int i = 0;
            for (final ConditionTest test : mc.getTests()) {
                if (evaluate(ctx, test)) {

                    Vector<MapItem> items = Vector.empty();

                    for (final MapItem mi : mc.getReturns()
                            .get(i)
                            .getItems()
                            .map(nested -> handleNestedMapConditionals(ctx, nested))) {
                        final ValueItem refsResult = referencesTransform.apply(ctx, (ValueItem) mi);

                        items = items.append((MapItem) refsResult);
                    }
                    return mc.with(ctx.getAncestry(), items);
                }
                i += 1;
            }
        }
        return mc;
    }

}
