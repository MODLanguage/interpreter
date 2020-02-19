package uk.modl.parser;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.tree.ParseTree;
import uk.modl.model.*;
import uk.modl.parser.antlr.MODLParser;
import uk.modl.parser.errors.InterpreterError;
import uk.modl.utils.Util;

/**
 * Parser for a MODLParser.ModlContext object
 */
@Log4j2
public class ModlParsedVisitor {

    /**
     * Immutable result
     */
    public final Modl modl;

    private int inConditional = 0;

    /**
     * Constructor
     *
     * @param ctx a MODLParser.ModlContext generated by Antlr
     */
    public ModlParsedVisitor(final MODLParser.ModlContext ctx) {
        log.trace("ModlParsedVisitor()");

        final Vector<Structure> structures = Vector.ofAll(ctx.modl_structure()
                .stream()
                .map(this::visitStructure));

        modl = new Modl(structures);
    }

    /**
     * Parse Structures
     *
     * @param ctx the context
     * @return a Structure
     */
    private Structure visitStructure(final MODLParser.Modl_structureContext ctx) {
        log.trace("visitStructure()");

        return (ctx.modl_array() != null) ?
                visitArray(ctx.modl_array()) :
                (ctx.modl_map() != null) ?
                        visitMap(ctx.modl_map()) :
                        (ctx.modl_pair() != null) ?
                                visitPair(ctx.modl_pair()) :
                                (ctx.modl_top_level_conditional() != null) ?
                                        visitTopLevelConditional(ctx.modl_top_level_conditional()) :
                                        null;
    }

    /**
     * Parse a TopLevelConditional
     *
     * @param ctx the context
     * @return a TopLevelConditional
     */
    private TopLevelConditional visitTopLevelConditional(final MODLParser.Modl_top_level_conditionalContext ctx) {
        log.trace("visitTopLevelConditional()");

        final Vector<ConditionTest> tests = (ctx.modl_condition_test() != null) ?
                Vector.ofAll(ctx.modl_condition_test()
                        .stream()
                        .map(this::visitConditionTest)) :
                null;

        final Vector<TopLevelConditionalReturn> returns = (ctx.modl_top_level_conditional_return() != null) ?
                Vector.ofAll(ctx.modl_top_level_conditional_return()
                        .stream()
                        .map(this::visitTopLevelConditionalReturn)) :
                null;

        return new TopLevelConditional(tests, returns);
    }

    /**
     * Parse a MapConditional
     *
     * @param ctx the context
     * @return a MapConditional
     */
    private MapConditional visitMapConditional(final MODLParser.Modl_map_conditionalContext ctx) {
        log.trace("visitMapConditional()");
        final Vector<ConditionTest> tests = (ctx.modl_condition_test() != null) ?
                Vector.ofAll(ctx.modl_condition_test()
                        .stream()
                        .map(this::visitConditionTest)) :
                null;

        final Vector<MapConditionalReturn> returns = (ctx.modl_map_conditional_return() != null) ?
                Vector.ofAll(ctx.modl_map_conditional_return()
                        .stream()
                        .map(this::visitMapConditionalReturn)) :
                null;

        return new MapConditional(tests, returns);
    }

    /**
     * Parse a MapConditionalReturn
     *
     * @param ctx the context
     * @return a MapConditionalReturn
     */
    private MapConditionalReturn visitMapConditionalReturn(final MODLParser.Modl_map_conditional_returnContext ctx) {
        log.trace("visitMapConditionalReturn()");
        final Vector<MapItem> items = Vector.ofAll(ctx.modl_map_item()
                .stream()
                .map(this::visitMapItem));
        return new MapConditionalReturn(items);
    }

    /**
     * Parse a TopLevelConditionalReturn
     *
     * @param ctx the context
     * @return a TopLevelConditionalReturn
     */
    private TopLevelConditionalReturn visitTopLevelConditionalReturn(final MODLParser.Modl_top_level_conditional_returnContext ctx) {
        log.trace("visitTopLevelConditionalReturn()");

        final Vector<Structure> structures = (ctx.modl_structure() != null) ?
                Vector.ofAll(ctx.modl_structure()
                        .stream()
                        .map(this::visitStructure)) :
                null;

        return new TopLevelConditionalReturn(structures);
    }

    /**
     * Parse a ConditionTest
     *
     * @param ctx the context
     * @return a ConditionTest
     */
    private ConditionTest visitConditionTest(final MODLParser.Modl_condition_testContext ctx) {
        log.trace("visitConditionTest()");

        Vector<Tuple2<ConditionOrConditionGroupInterface, String>> subConditionList = Vector.empty();

        String lastOperator = null;
        boolean shouldNegate = false;
        for (final ParseTree child : ctx.children) {
            if (child instanceof MODLParser.Modl_condition_groupContext) {
                if (shouldNegate) {
                    final NegatedConditionGroup conditionGroup = visitNegatedConditionGroup((MODLParser.Modl_condition_groupContext) child);
                    subConditionList = subConditionList.append(Tuple.of(conditionGroup, lastOperator));
                } else {
                    final ConditionGroup conditionGroup = visitConditionGroup((MODLParser.Modl_condition_groupContext) child);
                    subConditionList = subConditionList.append(Tuple.of(conditionGroup, lastOperator));
                }

                lastOperator = null;
                shouldNegate = false;
            } else if (child instanceof MODLParser.Modl_conditionContext) {
                if (shouldNegate) {
                    final NegatedCondition condition = visitNegatedCondition(((MODLParser.Modl_conditionContext) child));
                    subConditionList = subConditionList.append(Tuple.of(condition, lastOperator));
                } else {
                    final Condition condition = visitCondition((MODLParser.Modl_conditionContext) child);
                    subConditionList = subConditionList.append(Tuple.of(condition, lastOperator));
                }

                lastOperator = null;
                shouldNegate = false;
            } else {
                if (child
                        .getText()
                        .equals("!")) {
                    shouldNegate = true;
                } else {
                    lastOperator = child.getText();
                }
            }

        }

        return new ConditionTest(subConditionList);
    }

    /**
     * Parse a ConditionGroup
     *
     * @param ctx the context
     * @return a ConditionGroup
     */
    private ConditionGroup visitConditionGroup(final MODLParser.Modl_condition_groupContext ctx) {
        log.trace("visitConditionGroup()");
        final Vector<Tuple2<ConditionTest, String>> subConditionList = handleConditionGroup(ctx);
        return new ConditionGroup(subConditionList);
    }

    /**
     * Convert a ConditionGroup context to a list of subconditions
     *
     * @param ctx the context
     * @return a list of subconditions
     */
    private Vector<Tuple2<ConditionTest, String>> handleConditionGroup(final MODLParser.Modl_condition_groupContext ctx) {
        log.trace("handleConditionGroup()");
        Vector<Tuple2<ConditionTest, String>> subConditionList = Vector.empty();

        String lastOperator = null;
        for (final ParseTree child : ctx.children) {
            if (child instanceof MODLParser.Modl_condition_testContext) {
                final ConditionTest conditionGroup = visitConditionTest((MODLParser.Modl_condition_testContext) child);
                subConditionList = subConditionList.append(Tuple.of(conditionGroup, lastOperator));

                lastOperator = null;
            } else {
                lastOperator = child.getText();
            }
        }
        return subConditionList;
    }

    /**
     * Parse a ConditionGroup
     *
     * @param ctx the context
     * @return a ConditionGroup
     */
    private NegatedConditionGroup visitNegatedConditionGroup(final MODLParser.Modl_condition_groupContext ctx) {
        log.trace("visitNegatedConditionGroup()");
        final Vector<Tuple2<ConditionTest, String>> subConditionList = handleConditionGroup(ctx);
        return new NegatedConditionGroup(subConditionList);
    }

    /**
     * Parse a Condition
     *
     * @param ctx the context
     * @return a Condition
     */
    private Condition visitCondition(final MODLParser.Modl_conditionContext ctx) {
        log.trace("visitCondition()");
        inConditional++;
        final Operator op = (ctx.modl_operator() != null) ? visitOperator(ctx.modl_operator()) : null;

        final Vector<ValueItem> values = (ctx.modl_value() != null) ? Vector.ofAll(ctx.modl_value()
                .stream()
                .map(this::visitValue)) : Vector.empty();

        final String lhs = (ctx.STRING() != null) ? ctx.STRING()
                .getText() : null;

        inConditional--;
        return new Condition(lhs, op, values);
    }

    /**
     * Parse a Condition
     *
     * @param ctx the context
     * @return a Condition
     */
    private NegatedCondition visitNegatedCondition(final MODLParser.Modl_conditionContext ctx) {
        log.trace("visitNegatedCondition()");
        final Operator op = (ctx.modl_operator() != null) ? visitOperator(ctx.modl_operator()) : null;

        final Vector<ValueItem> values = (ctx.modl_value() != null) ? Vector.ofAll(ctx.modl_value()
                .stream()
                .map(this::visitValue)) : Vector.empty();

        return new NegatedCondition(op, values);
    }

    /**
     * Parse an Operator
     *
     * @param ctx the context
     * @return an Operator
     */
    private Operator visitOperator(final MODLParser.Modl_operatorContext ctx) {
        log.trace("visitOperator()");

        final boolean equals = ctx.EQUALS() != null;
        final boolean negate = ctx.EXCLAM() != null;
        final boolean gthan = ctx.GTHAN() != null;
        final boolean lthan = ctx.LTHAN() != null;

        if (equals) {
            if (gthan) {
                return new GreaterThanOrEqualsOperator();
            }
            if (lthan) {
                return new LessThanOrEqualsOperator();
            }
            if (negate) {
                return new NotEqualsOperator();
            }
            return new EqualsOperator();
        }
        if (gthan) {
            return new GreaterThanOperator();
        }
        if (lthan) {
            return new LessThanOperator();
        }
        return null;// Should never get here unless the grammar changes.
    }

    /**
     * Parse ModlArrays
     *
     * @param ctx the context
     * @return a list of ArrayItems
     */
    private Array visitArray(final MODLParser.Modl_arrayContext ctx) {
        log.trace("visitArray()");

        final Vector<ArrayItem> items = Vector.ofAll(ctx.modl_array_item()
                .stream()
                .map(this::visitArrayItem))
                .appendAll(Vector.ofAll(ctx.modl_nb_array()
                        .stream()
                        .map(this::visitNbArray)));

        return new Array(items);
    }

    /**
     * Parse NbArrays
     *
     * @param ctx the context
     * @return a list of ArrayItems
     */
    private Array visitNbArray(final MODLParser.Modl_nb_arrayContext ctx) {
        log.trace("visitNbArray()");

        final Vector<ArrayItem> items = Vector.ofAll(ctx.modl_array_item()
                .stream()
                .map(this::visitArrayItem));

        return new Array(items);
    }

    /**
     * Parse ArrayItems
     *
     * @param ctx the context
     * @return a list of ArrayItems
     */
    private ArrayItem visitArrayItem(final MODLParser.Modl_array_itemContext ctx) {
        log.trace("visitArrayItem()");

        return (ctx.modl_array_conditional() != null) ?
                visitArrayConditional(ctx.modl_array_conditional()) :
                (ctx.modl_array_value_item() != null) ?
                        visitArrayValueItem(ctx.modl_array_value_item()) :
                        null;
    }

    /**
     * Parse ArrayValue
     *
     * @param ctx the context
     * @return an ArrayItem
     */
    private ArrayItem visitArrayValueItem(final MODLParser.Modl_array_value_itemContext ctx) {
        log.trace("visitArrayValueItem()");

        return (ctx.modl_array() != null) ?
                visitArray(ctx.modl_array()) :
                (ctx.modl_map() != null) ?
                        visitMap(ctx.modl_map()) :
                        (ctx.modl_pair() != null) ?
                                visitPair(ctx.modl_pair()) :
                                (ctx.modl_primitive() != null) ?
                                        (ArrayItem) visitPrimitive(ctx.modl_primitive()) : null;
    }

    /**
     * Parse ArrayConditionalContext
     *
     * @param ctx the context
     * @return an ArrayItem
     */
    private ArrayConditional visitArrayConditional(final MODLParser.Modl_array_conditionalContext ctx) {
        log.trace("visitArrayConditional()");
        final Vector<ConditionTest> tests = (ctx.modl_condition_test() != null) ?
                Vector.ofAll((ctx.modl_condition_test()
                        .stream()
                        .map(this::visitConditionTest))) :
                null;

        final Vector<ArrayConditionalReturn> returns = (ctx.modl_array_conditional_return() != null) ?
                Vector.ofAll((ctx.modl_array_conditional_return()
                        .stream()
                        .map(this::visitArrayConditionalReturn))) :
                null;

        return new ArrayConditional(tests, returns);
    }

    /**
     * Parse ArrayConditionalReturn
     *
     * @param ctx the context
     * @return an ArrayConditionalReturn
     */
    private ArrayConditionalReturn visitArrayConditionalReturn(final MODLParser.Modl_array_conditional_returnContext ctx) {
        log.trace("visitMapConditionalReturn()");
        final Vector<ArrayItem> items = Vector.ofAll(ctx.modl_array_item()
                .stream()
                .map(this::visitArrayItem));
        return new ArrayConditionalReturn(items);
    }

    /**
     * Parse ModlMaps
     *
     * @param ctx the context
     * @return a list of MapItems
     */
    private Map visitMap(final MODLParser.Modl_mapContext ctx) {
        log.trace("visitMap()");

        final Vector<MapItem> items = Vector.ofAll(ctx.modl_map_item()
                .stream()
                .map(this::visitMapItem));

        return new Map(items);
    }

    /**
     * Parse MapItems
     *
     * @param ctx the context
     * @return a MapItem
     */
    private MapItem visitMapItem(MODLParser.Modl_map_itemContext ctx) {
        log.trace("visitMapItem()");

        return (ctx.modl_pair() != null) ?
                visitPair(ctx.modl_pair()) :
                (ctx.modl_map_conditional() != null) ?
                        visitMapConditional(ctx.modl_map_conditional()) :
                        null;
    }

    /**
     * Parse a ModlPair
     *
     * @param ctx the context
     * @return a Pair
     */
    private Pair visitPair(final MODLParser.Modl_pairContext ctx) {
        log.trace("visitPair()");
        final String key = (ctx.QUOTED() != null) ? ctx.QUOTED()
                .getText() : (ctx.STRING() != null) ? ctx.STRING()
                .getText() : null;

        if (inConditional == 0 && key != null && (key.contains("%") || key.contains(" "))) {
            throw new InterpreterError("Invalid key - spaces and % characters are not allowed: " + key);
        }

        final PairValue value;
        if (ctx.modl_array() != null) {
            value = visitArray(ctx.modl_array());
        } else if (ctx.modl_map() != null) {
            value = visitMap(ctx.modl_map());
        } else if (ctx.modl_value_item() != null) {
            value = visitValueItem(ctx.modl_value_item());
        } else {
            value = null;
        }
        return new Pair(key, value);
    }

    /**
     * Parse ValueItems
     *
     * @param ctx the context
     * @return a ValueItem
     */
    private ValueItem visitValueItem(final MODLParser.Modl_value_itemContext ctx) {
        log.trace("visitValueItem()");
        if (ctx.modl_value() != null) {
            return visitValue(ctx.modl_value());
        }
        if (ctx.modl_value_conditional() != null) {
            return visitValueConditional(ctx.modl_value_conditional());
        }
        return null;
    }

    /**
     * Parse ValueConditional
     *
     * @param ctx the context
     * @return a ValueItem
     */
    private ValueConditional visitValueConditional(final MODLParser.Modl_value_conditionalContext ctx) {
        log.trace("visitValueConditional()");
        final Vector<ConditionTest> tests = (ctx.modl_condition_test() != null) ?
                Vector.ofAll(ctx.modl_condition_test()
                        .stream()
                        .map(this::visitConditionTest)) :
                null;

        final Vector<ValueConditionalReturn> returns = (ctx.modl_value_conditional_return() != null) ?
                Vector.ofAll(ctx.modl_value_conditional_return()
                        .stream()
                        .map(this::visitValueConditionReturn)) :
                null;

        return new ValueConditional(tests, returns);
    }

    /**
     * Parse ValueConditionalReturn
     *
     * @param ctx the context
     * @return a ValueConditionalReturn
     */
    private ValueConditionalReturn visitValueConditionReturn(final MODLParser.Modl_value_conditional_returnContext ctx) {
        log.trace("visitValueConditionalReturn()");

        final Vector<ValueItem> items = Vector.ofAll(ctx.modl_value_item()
                .stream()
                .map(this::visitValueItem));

        return new ValueConditionalReturn(items);
    }

    /**
     * Parse Values
     *
     * @param ctx the context
     * @return a Value
     */
    private ValueItem visitValue(final MODLParser.Modl_valueContext ctx) {
        log.trace("visitValue()");
        return (ctx.modl_array() != null) ?
                visitArray(ctx.modl_array()) :
                (ctx.modl_map() != null) ?
                        visitMap(ctx.modl_map()) :
                        (ctx.modl_pair() != null) ?
                                visitPair(ctx.modl_pair()) :
                                (ctx.modl_nb_array() != null) ?
                                        visitNbArray(ctx.modl_nb_array()) :
                                        (ctx.modl_primitive() != null) ?
                                                visitPrimitive(ctx.modl_primitive()) :
                                                null;
    }

    /**
     * Parse Primitive
     *
     * @param ctx the context
     * @return a ValueItem
     */
    private ValueItem visitPrimitive(final MODLParser.Modl_primitiveContext ctx) {
        log.trace("visitPrimitive()");

        return (ctx.FALSE() != null) ?
                FalsePrimitive.instance :
                (ctx.TRUE() != null) ?
                        TruePrimitive.instance :
                        (ctx.STRING() != null) ?
                                new StringPrimitive(ctx.STRING()
                                        .getText()) :
                                (ctx.NULL() != null) ?
                                        NullPrimitive.instance :
                                        (ctx.NUMBER() != null) ?
                                                new NumberPrimitive(ctx.NUMBER()
                                                        .getText()) :
                                                (ctx.QUOTED() != null) ? new StringPrimitive(Util.unquote(ctx.QUOTED()
                                                        .getText())) :
                                                        null;
    }

}
