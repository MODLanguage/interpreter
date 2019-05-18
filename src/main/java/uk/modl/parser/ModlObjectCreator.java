/*
MIT License

Copyright (c) 2018 NUM Technology Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package uk.modl.parser;

import org.apache.commons.lang3.tuple.ImmutablePair;
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModlObjectCreator {

    public static int MODL_VERSION = 1;


    public static RawModlObject processModlParsed(String input) throws IOException {
        ModlParsed modlParsed = Parser.parse(input);

        return processModlParsed(modlParsed);
    }

    private static RawModlObject processModlParsed(ModlParsed modlParsed) {
        // Go through the ModlParsed object and transform it into a RawModlObject object, using the rules we know about and any config rules
        RawModlObject rawModlObject = new RawModlObject();


        for (ModlParsed.Structure parsedStructure : modlParsed.getStructures()) {
            List<RawModlObject.Structure> structures = processModlParsed(rawModlObject, parsedStructure);
            if (structures != null) {
                for (RawModlObject.Structure structure : structures) {
                    if (structure != null) {
                        rawModlObject.addStructure(structure);
                    }
                }
            }
        }

        return rawModlObject;
    }

    private static List<RawModlObject.Structure> processModlParsed(RawModlObject rawModlObject, ModlParsed.Structure parsedStructure) {
        if (parsedStructure == null) {
            return null;
        }

        List<RawModlObject.Structure> structures = new LinkedList<>();
        RawModlObject.Structure structure = null;

        structure = (processModlParsed(rawModlObject, parsedStructure.getMap()));
        if (structure != null) {
            structures.add(structure);
            return structures;
        }

        structure = (processModlParsed(rawModlObject, parsedStructure.getArray()));
        if (structure != null) {
            structures.add(structure);
            return structures;
        }

        structures = processModlParsed(rawModlObject, parsedStructure.getPair());
        if (structures != null) {
            return structures;
        }

        structure = (processModlParsed(rawModlObject, parsedStructure.getTopLevelConditional()));
        structures = new LinkedList<>();
        structures.add(structure);
        return structures;
    }

    private static RawModlObject.Map processModlParsed(RawModlObject rawModlObject, ModlParsed.Map parsedMap) {
        if (parsedMap == null) {
            return null;
        }

        RawModlObject.Map map = new RawModlObject.Map();

        if (parsedMap.getMapItems() != null) {
            for (ModlParsed.MapItem mapItemParsed : parsedMap.getMapItems()) {
                RawModlObject.Pair pair = processModlParsed(rawModlObject, mapItemParsed);
                if (pair != null) {
                    map.addPair(pair);
                }
            }
        }

        return map;
    }

    private static RawModlObject.Pair processModlParsed(RawModlObject rawModlObject, ModlParsed.MapItem parsedMapItem) {
        if (parsedMapItem == null) {
            return null;
        }
        RawModlObject.Pair pair = null;
        pair = processModlParsed(rawModlObject, parsedMapItem.getMapConditional());
        if (pair != null) {
            return pair;
        }
        List<RawModlObject.Structure> structures = (processModlParsed(rawModlObject, parsedMapItem.getPair()));
        if (structures.size() > 0) {
            return ((RawModlObject.Pair)(structures.get(0)));
        }
        return null;
    }


    private static ModlValue processModlParsed(RawModlObject rawModlObject, ModlParsed.Value parsedValue) {
        if (parsedValue == null) {
            return null;
        }

        ModlValue value = null;

        List<RawModlObject.Structure> pairs = processModlParsed(rawModlObject, parsedValue.getPair());
        if (pairs != null && pairs.size() > 0) {
            return (ModlValue)(pairs.get(0));
        }
        value = processModlParsed(rawModlObject, parsedValue.getMap());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getArray());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getNbArray());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getPrimitive());
        if (value != null) {
            return value;
        }
        return value;

    }

    private static ModlValue processModlParsed(RawModlObject rawModlObject, ModlParsed.Primitive parsedValue) {
        if (parsedValue == null) {
            return null;
        }

        ModlValue value = null;

        value = processModlParsed(rawModlObject, parsedValue.getQuoted());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getNumber());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getTrueVal());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getFalseVal());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getNullVal());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getString());
        if (value != null) {
            return value;
        }
        return value;

    }

    private static ModlValue processModlParsed(RawModlObject rawModlObject, ModlParsed.ArrayValueItem parsedValue) {
        if (parsedValue == null) {
            return null;
        }

        ModlValue value = null;

        List<RawModlObject.Structure> pairs = processModlParsed(rawModlObject, parsedValue.getPair());
        if (pairs != null && pairs.size() > 0) {
            return (ModlValue)(pairs.get(0));
        }
        value = processModlParsed(rawModlObject, parsedValue.getMap());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getArray());
        if (value != null) {
            return value;
        }
        value = processModlParsed(rawModlObject, parsedValue.getPrimitive());
        if (value != null) {
            return value;
        }
        return value;

    }

    private static RawModlObject.ConditionTest processModlParsed(RawModlObject rawModlObject, ModlParsed.ConditionTest conditionTestParsed) {
        if (conditionTestParsed == null) {
            return null;
        }

        RawModlObject.ConditionTest conditionTest = rawModlObject.new ConditionTest();
        // SubConditions are either ConditionGroups or Conditions
        for (ImmutablePair<ModlParsed.SubCondition, ImmutablePair<String, Boolean>> subConditionPair : conditionTestParsed.subConditionList) {
            ModlParsed.SubCondition subCondition = subConditionPair.getLeft();
            ImmutablePair<java.lang.String, Boolean> operatorPair = subConditionPair.getRight();
            String operator = operatorPair.getLeft();
            Boolean shouldNegate = operatorPair.getRight();
            if (subCondition instanceof ModlParsed.ConditionGroup) {
                RawModlObject.ConditionGroup conditionGroup = processModlParsed(rawModlObject, (ModlParsed.ConditionGroup) subCondition);
                conditionTest.addSubCondition(operator, shouldNegate, conditionGroup);
            } else if (subCondition instanceof ModlParsed.Condition) {
                RawModlObject.Condition condition = processModlParsed(rawModlObject, (ModlParsed.Condition) subCondition);
                conditionTest.addSubCondition(operator, shouldNegate, condition);
            }
        }

        return conditionTest;
    }

    private static RawModlObject.Condition processModlParsed(RawModlObject rawModlObject, ModlParsed.Condition conditionParsed) {
        if (conditionParsed == null) {
            return null;
        }

        String key = conditionParsed.key;
        String operator = conditionParsed.operator;
        List<ModlValue> values = new LinkedList<>();
        for (ModlParsed.Value valueParsed : conditionParsed.values) {
            ModlValue value = processModlParsed(rawModlObject, valueParsed);
            values.add(value);
        }
        RawModlObject.Condition condition = rawModlObject.new Condition(key, operator, values);
        return condition;
    }

    private static RawModlObject.ConditionGroup processModlParsed(RawModlObject rawModlObject, ModlParsed.ConditionGroup conditionGroupParsed) {
        if (conditionGroupParsed == null) {
            return null;
        }
        RawModlObject.ConditionGroup conditionGroup = rawModlObject.new ConditionGroup();
        for (org.apache.commons.lang3.tuple.ImmutablePair<ModlParsed.ConditionTest, java.lang.String> conditionTestPair : conditionGroupParsed.conditionsTestList) {
            ModlParsed.ConditionTest conditionTestParsed = conditionTestPair.getLeft();
            String operator = conditionTestPair.getRight();
            RawModlObject.ConditionTest conditionTest = processModlParsed(rawModlObject, conditionTestParsed);
            conditionGroup.addConditionTest(conditionTest, operator);
        }

        return conditionGroup;
    }

    private static List<RawModlObject.Structure> processModlParsed(RawModlObject rawModlObject, ModlParsed.Pair pairParsed) {
        if (pairParsed == null) {
            return null;
        }
        RawModlObject.Pair pair = new RawModlObject.Pair();

        pair.setKey(new RawModlObject.String(pairParsed.getKey()));

        if (pairParsed.getKey() != null &&  (pairParsed.getKey().toLowerCase().equals("*l") || pairParsed.getKey().toLowerCase().equals("*load"))) {
            // Make a new Pair for each valueItem or item in the array for the IMPORT statement!
            return processImportStatement(rawModlObject, pairParsed);
        } else {
            RawModlObject.Map map = (processModlParsed(rawModlObject, pairParsed.getMap()));
            if (map != null) {
                pair.addModlValue(map);
            }

            RawModlObject.Array array = (processModlParsed(rawModlObject, pairParsed.getArray()));
            if (array != null) {
                pair.addModlValue(array);
            }

            if (pairParsed.getValueItem() != null) {
                    pair.addModlValue(processModlParsed(rawModlObject, pairParsed.getValueItem(), pair));
            }

            List<RawModlObject.Structure> pairs = new LinkedList<>();
            pairs.add(pair);
            return pairs;
        }
    }

    private static List<RawModlObject.Structure> processImportStatement(RawModlObject rawModlObject, ModlParsed.Pair pairParsed) {
        // Replace each import file in a single import pair with as many import pairs as there are files in the original import pair
        List<RawModlObject.Structure> structures = new LinkedList<>();
        RawModlObject.Array array = (processModlParsed(rawModlObject, pairParsed.getArray()));
        if (array == null) {
            if (pairParsed.getValueItem() != null && pairParsed.getValueItem().getValue() != null) {
                array = processModlParsed(rawModlObject, pairParsed.getValueItem().getValue().getNbArray());
            }
        }
        if (array != null) {
            int count = 0;
            boolean isFinal = false;
            for (ModlValue v : array.getValues()) {
                if (count++ == array.getValues().size()) {
                    isFinal = true;
                }
                RawModlObject.Pair pair = new RawModlObject.Pair();
                String key = pairParsed.getKey();
                if (!isFinal) {
                    key = key.toLowerCase();
                }
                pair.setKey(new RawModlObject.String(key));
//                if (v instanceof RawModlObject.Number) {
//                    v = new RawModlObject.String(((RawModlObject.Number)v).number);
//                }
                pair.addModlValue(v);
                structures.add(pair);
            }
        } else {
            if (pairParsed.getValueItem() != null) {
                    RawModlObject.Pair pair = new RawModlObject.Pair();
                    pair.setKey(new RawModlObject.String(pairParsed.getKey()));
                    ModlParsed.ValueItem valueParsed = pairParsed.getValueItem();
                    ModlValue v = processModlParsed(rawModlObject, valueParsed, pair);
//                    if (v instanceof RawModlObject.Number) {
//                        v = new RawModlObject.String(((RawModlObject.Number)v).number);
//                    }
                    pair.addModlValue(v);
                    structures.add(pair);
            }
        }
        return structures;
    }

    private static RawModlObject.Array processModlParsed(RawModlObject rawModlObject, ModlParsed.Array arrayParsed) {
        if (arrayParsed == null) {
            return null;
        }
        RawModlObject.Array array = new RawModlObject.Array();

        if (arrayParsed.getAbstractArrayItems() != null) {
            for (ModlParsed.AbstractArrayItem arrayItemParsed : arrayParsed.getAbstractArrayItems()) {
                // TODO Expand NbArrays here?
//                ModlValue value = processModlParsed(rawModlObject, (arrayItemParsed));
//                if (value != null) {
//                    array.addValue(value);
//                }

                if (arrayItemParsed instanceof ModlParsed.ArrayItem) {
                    ModlValue value = processModlParsed(rawModlObject, ((ModlParsed.ArrayItem)arrayItemParsed));
                    if (value != null) {
                        array.addValue(value);
                    }
                } else if (arrayItemParsed instanceof ModlParsed.NbArray) {
                    ModlObject.Array nbArray = new RawModlObject.Array();
                    for (ModlParsed.ArrayItem ai : ((ModlParsed.NbArray)arrayItemParsed).getArrayItems()) {
                        ModlValue value = processModlParsed(rawModlObject, ai);
                        if (value != null) {
//                            array.addValue(value);
                            nbArray.addValue(value);
                        }
                    }
                    array.addValue(nbArray);
                }
            }
        }

        return array;
    }

    private static RawModlObject.Array processModlParsed(RawModlObject rawModlObject, ModlParsed.NbArray nbArray) {
        if (nbArray == null || nbArray.getArrayItems().size() == 0) {
            return null;
        }
        ModlObject.Array array = new RawModlObject.Array();
        for (ModlParsed.ArrayItem arrayItem : nbArray.getArrayItems()) {
            ModlValue value = processModlParsed(rawModlObject, arrayItem);
            array.addValue(value);
        }
        return array;
    }

//    private static ModlValue processModlParsed(RawModlObject rawModlObject, ModlParsed.AbstractArrayItem arrayItemParsed) {
//        if (arrayItemParsed == null) {
//            return null;
//        }
//        if (arrayItemParsed instanceof ModlParsed.ArrayItem) {
//            return processModlParsed(rawModlObject, (ModlParsed.ArrayItem)arrayItemParsed);
//        } else if (arrayItemParsed instanceof ModlParsed.NbArray) {
//            return processModlParsed(rawModlObject, (ModlParsed.NbArray)arrayItemParsed);
//        }
//        throw new IllegalArgumentException();
//    }
//

    private static ModlValue processModlParsed(RawModlObject rawModlObject, ModlParsed.ArrayItem arrayItemParsed) {
        if (arrayItemParsed == null) {
            return null;
        }
        ModlValue value = null;

        if (arrayItemParsed.getArrayConditional() != null) {
            value = (processModlParsed(rawModlObject, arrayItemParsed.getArrayConditional()));
        }
        if (arrayItemParsed.getArrayValueItem() != null) {
            value = (processModlParsed(rawModlObject, arrayItemParsed.getArrayValueItem()));
        }

        return value;
    }

    private static ModlValue processModlParsed(RawModlObject rawModlObject, ModlParsed.ValueItem valueItemParsed,
                                                         RawModlObject.Pair parentPair) {
        if (valueItemParsed == null) {
            return null;
        }
        ModlValue value = null;

        if (valueItemParsed.getValueConditional() != null) {
            value = (processModlParsed(rawModlObject, valueItemParsed.getValueConditional(), parentPair));
        }
        if (valueItemParsed.getValue() != null) {
            value = (processModlParsed(rawModlObject, valueItemParsed.getValue()));
        }

        return value;
    }

    private static RawModlObject.False processModlParsed(RawModlObject rawModlObject, ModlParsed.False falseVal) {
        if (falseVal != null) {
            RawModlObject.False f = new RawModlObject.False();
            return f;
        }
        return null;
    }

    private static RawModlObject.Null processModlParsed(RawModlObject rawModlObject, ModlParsed.Null val) {
        if (val != null) {
            RawModlObject.Null n = new RawModlObject.Null();
            return n;
        }
        return null;
    }

    private static RawModlObject.True processModlParsed(RawModlObject rawModlObject, ModlParsed.True trueVal) {
        if (trueVal != null) {
            RawModlObject.True t = new RawModlObject.True();
            return t;
        }
        return null;
    }

    private static RawModlObject.Number processModlParsed(RawModlObject rawModlObject, ModlParsed.Number number) {
        if (number != null) {
            // TODO Should be number
            /*
            These simple rules seems solid:
If all chars are numbers Then
   int
ElseIf all chars are numbers except one and that char is a dot in position two or higher then it’s a float
Else it’s a string.
End
             */
            RawModlObject.Number n = new RawModlObject.Number(number.string);
            return n;
        }
        return null;
    }

    private static RawModlObject.String processModlParsed(RawModlObject rawModlObject, ModlParsed.String string) {
        if (string != null) {
            RawModlObject.String str = new RawModlObject.String(string.string);
            return str;
        }
        return null;
    }

//    private static RawModlObject.Quoted processModlParsed(RawModlObject rawModlObject, ModlParsed.Quoted quoted) {
    private static RawModlObject.String processModlParsed(RawModlObject rawModlObject, ModlParsed.Quoted quoted) {
        if (quoted != null) {
            String s = quoted.string;
            if (s.startsWith("\"") && s.endsWith("\"")) {
                s = s.substring(1, s.length() - 1);
            }
//            RawModlObject.Quoted q = rawModlObject.new Quoted(s);
//            return q;
            RawModlObject.String str = new RawModlObject.String(s);
            return str;
        }
        return null;
    }

    private static RawModlObject.ValueConditional processModlParsed(RawModlObject rawModlObject, ModlParsed.ValueConditional conditionalParsed,
                                                                    RawModlObject.Pair parentPair) {
        if (conditionalParsed == null) {
            return null;
        }
        RawModlObject.ValueConditional conditional = rawModlObject.new ValueConditional();
        for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.ValueConditionalReturn> conditionalParsedEntry : conditionalParsed.getValueConditionalReturns().entrySet()) {
            conditional.addConditional(processModlParsed(rawModlObject, conditionalParsedEntry.getKey()),
                    processModlParsed(rawModlObject, conditionalParsedEntry.getValue(), parentPair));
        }
        return conditional;
    }

    private static RawModlObject.ValueConditionalReturn processModlParsed(RawModlObject rawModlObject, ModlParsed.ValueConditionalReturn conditionalReturnParsed,
                                                                          RawModlObject.Pair parentPair) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        RawModlObject.ValueConditionalReturn conditionalReturn = rawModlObject.new ValueConditionalReturn();
        if (conditionalReturnParsed.getValueItems() != null) {
            for (ModlParsed.ValueItem valueParsed : conditionalReturnParsed.getValueItems()) {
                ModlValue value = processModlParsed(rawModlObject, valueParsed, parentPair);
                conditionalReturn.addValue(value);
            }
        }
        return conditionalReturn;
    }

    private static ModlValue processModlParsed(RawModlObject rawModlObject, ModlParsed.ArrayConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        RawModlObject.ArrayConditional conditional = new RawModlObject.ArrayConditional();
        if (conditionalParsed.getArrayConditionalReturns() != null) {
            for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.ArrayConditionalReturn> conditionalParsedEntry : conditionalParsed.getArrayConditionalReturns().entrySet()) {
                conditional.addConditional(processModlParsed(rawModlObject, conditionalParsedEntry.getKey()),
                        processModlParsed(rawModlObject, conditionalParsedEntry.getValue()));
            }
        }
        return conditional;
    }

    private static RawModlObject.ArrayConditionalReturn processModlParsed(RawModlObject rawModlObject, ModlParsed.ArrayConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        RawModlObject.ArrayConditionalReturn conditionalReturn = new RawModlObject.ArrayConditionalReturn();
        if (conditionalReturnParsed.getArrayItems() != null) {
            for (ModlParsed.ArrayItem valueParsed : conditionalReturnParsed.getArrayItems()) {
                ModlValue value = processModlParsed(rawModlObject, valueParsed);
                conditionalReturn.addValue(value);
            }
        }
        return conditionalReturn;
    }

    private static RawModlObject.MapConditional processModlParsed(RawModlObject rawModlObject, ModlParsed.MapConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        RawModlObject.MapConditional conditional = new RawModlObject.MapConditional();
        for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.MapConditionalReturn> conditionalParsedEntry : conditionalParsed.getMapConditionals().entrySet()) {
            conditional.addConditional(processModlParsed(rawModlObject, conditionalParsedEntry.getKey()),
                    processModlParsed(rawModlObject, conditionalParsedEntry.getValue()));
        }
        return conditional;
    }

    private static RawModlObject.Map processModlParsed(RawModlObject rawModlObject, ModlParsed.MapConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        RawModlObject.Map map = new RawModlObject.Map();
        if (conditionalReturnParsed.getMapItems() != null) {
            for (ModlParsed.MapItem valueParsed : conditionalReturnParsed.getMapItems()) {
                RawModlObject.Pair p = processModlParsed(rawModlObject, valueParsed);
                map.addPair(p);
            }
        }
        return map;
    }

    private static RawModlObject.TopLevelConditional processModlParsed(RawModlObject rawModlObject, ModlParsed.TopLevelConditional conditionalParsed) {
        if (conditionalParsed == null) {
            return null;
        }
        RawModlObject.TopLevelConditional conditional = rawModlObject.new TopLevelConditional();
        for (Map.Entry<ModlParsed.ConditionTest, ModlParsed.TopLevelConditionalReturn> conditionalParsedEntry : conditionalParsed.getTopLevelConditionalReturns().entrySet()) {
            conditional.addConditional(processModlParsed(rawModlObject, conditionalParsedEntry.getKey()),
                    processModlParsed(rawModlObject, conditionalParsedEntry.getValue()));
        }
        return conditional;
    }

    private static RawModlObject.TopLevelConditionalReturn processModlParsed(RawModlObject rawModlObject, ModlParsed.TopLevelConditionalReturn conditionalReturnParsed) {
        if (conditionalReturnParsed == null) {
            return null;
        }
        RawModlObject.TopLevelConditionalReturn conditionalReturn = rawModlObject.new TopLevelConditionalReturn();
        if (conditionalReturnParsed.getStructures() != null) {
            for (ModlParsed.Structure valueParsed : conditionalReturnParsed.getStructures()) {
                List<RawModlObject.Structure> structures = processModlParsed(rawModlObject, valueParsed);
                conditionalReturn.addStructure(structures.get(0));
            }
        }
        return conditionalReturn;
    }
}
