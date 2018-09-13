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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.tuple.ImmutablePair;
import uk.modl.parser.printers.RawModlObjectJsonSerializer;

import java.util.*;

@JsonSerialize(using = RawModlObjectJsonSerializer.class)
public class RawModlObject {

    List<RawModlObject.Structure> structures = new LinkedList<>();

    public List<RawModlObject.Structure> getStructures() {
        return structures;
    }

    public void addStructure(Structure structure) { structures.add(structure); }

    public class Structure {
        Map map;
        Array array;
        TopLevelConditional topLevelConditional;
        Pair pair;

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }

        public Array getArray() {
            return array;
        }

        public void setArray(Array array) {
            this.array = array;
        }

        public TopLevelConditional getTopLevelConditional() {
            return topLevelConditional;
        }

        public void setTopLevelConditional(TopLevelConditional topLevelConditional) {
            this.topLevelConditional = topLevelConditional;
        }

        public Pair getPair() {
            return pair;
        }

        public void setPair(Pair pair) {
            this.pair = pair;
        }

    }

    public class Value {
        RawModlObject.Quoted quoted;
        RawModlObject.Number number;
        RawModlObject.True trueVal;
        RawModlObject.False falseVal;
        RawModlObject.Null nullVal;
        RawModlObject.String string;
        RawModlObject.Map map;
        RawModlObject.Array array;
        RawModlObject.Pair pair;

        public void setQuoted(Quoted quoted) {
            this.quoted = quoted;
        }

        public void setNumber(Number number) {
            this.number = number;
        }

        public void setTrueVal(True trueVal) {
            this.trueVal = trueVal;
        }

        public void setFalseVal(False falseVal) {
            this.falseVal = falseVal;
        }

        public void setNullVal(Null nullVal) {
            this.nullVal = nullVal;
        }

        public void setString(String string) {
            this.string = string;
        }

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }

        public Array getArray() {
            return array;
        }

        public void setArray(Array array) {
            this.array = array;
        }

        public Pair getPair() {
            return pair;
        }

        public void setPair(Pair pair) {
            this.pair = pair;
        }

        public RawModlObject.Quoted getQuoted() {
            return quoted;
        }

        public RawModlObject.Number getNumber() {
            return number;
        }

        public RawModlObject.True getTrueVal() {
            return trueVal;
        }

        public RawModlObject.False getFalseVal() {
            return falseVal;
        }

        public RawModlObject.Null getNullVal() {
            return nullVal;
        }

        public RawModlObject.String getString() {
            return string;
        }
    }

    public class Map {
        List<MapItem> mapItems;

        public List<MapItem> getMapItems() {
            return mapItems;
        }

        public void addMapItem(MapItem mapItem) {
            if (mapItems == null) {
                mapItems = new LinkedList<>();
            }
            mapItems.add(mapItem);
        }

    }

    public class MapItem {
        Pair pair;
        MapConditional mapConditional;

        public Pair getPair() {
            return pair;
        }

        public void setPair(Pair pair) {
            this.pair = pair;
        }

        public MapConditional getMapConditional() {
            return mapConditional;
        }

        public void setMapConditional(MapConditional mapConditional) {
            this.mapConditional = mapConditional;
        }
    }

    public class ValueItem {
        Value value;
        List<ValueItem> valueItems;
        ValueConditional valueConditional;

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }

        public ValueConditional getValueConditional() {
            return valueConditional;
        }

        public void setValueConditional(ValueConditional valueConditional) {
            this.valueConditional = valueConditional;
        }

        public List<ValueItem> getValueItems() {
            return valueItems;
        }

        public void addValueItem(ValueItem valueItem) {
            if (valueItems == null) {
                valueItems = new LinkedList<>();
            }
            valueItems.add(valueItem);
        }

        public void resetValueItems() {
            valueItems = null;
        }
    }

    public class Pair {
        java.lang.String key;
        List<ValueItem> valueItems;
        Map map;
        Array array;


        public List<RawModlObject.ValueItem> getValueItems() {
            return valueItems;
        }

        public java.lang.String getKey() {
            return key;
        }

        public void addValueItem( ValueItem valueItem) {
            if (valueItems == null) {
                valueItems = new LinkedList<>();
            }
//            if (values.get(key) != null) {
//                // TODO Deal with duplicate keys!!!
//            } else {
                valueItems.add(valueItem);
//            }
        }

        public void resetValueItems() {
            valueItems = null;
        }

        public void setKey(java.lang.String key) {
            this.key = key;
        }

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }

        public Array getArray() {
            return array;
        }

        public void setArray(Array array) {
            this.array = array;
        }

    }

    public class String {
        public java.lang.String string;

        public String(java.lang.String string) {
            this.string = string;
        }
    }

    public class Number {
        // TODO Should be number
        public java.lang.String string;

        public Number(java.lang.String string) {
            this.string = string;
        }
    }

    public class Quoted {
        public java.lang.String string;

        public Quoted(java.lang.String string) {
            this.string = string;
        }
    }

    public interface SubCondition {}

    public class ConditionTest {
        java.util.Map<RawModlObject.SubCondition, ImmutablePair<java.lang.String, Boolean>> subConditionMap = new HashMap<>();

        public void addSubCondition(java.lang.String operator, boolean shouldNegate, SubCondition subCondition) {
            subConditionMap.put(subCondition, new ImmutablePair<java.lang.String, Boolean>(operator, shouldNegate));
        }

        public java.util.Map<SubCondition, ImmutablePair<java.lang.String, Boolean>> getSubConditionMap() {
            return subConditionMap;
        }
    }

    public class Condition implements SubCondition {
        java.lang.String key;
        java.lang.String operator;
        List<Value> values = new LinkedList<>();

        public Condition(java.lang.String key, java.lang.String operator, List<Value> values) {
            this.key = key;
            this.operator = operator;
            this.values = values;
        }

        public java.lang.String getKey() {
            return key;
        }

        public java.lang.String getOperator() {
            return operator;
        }

        public List<Value> getValues() {
            return values;
        }
    }

    public class ConditionGroup implements SubCondition {
        java.util.List<ImmutablePair<ConditionTest, java.lang.String>> conditionsTestList = new LinkedList<>();

        public void addConditionTest(ConditionTest conditionTest, java.lang.String operator) {
            conditionsTestList.add(new ImmutablePair<ConditionTest, java.lang.String>(conditionTest, operator));
        }

        public List<ImmutablePair<ConditionTest, java.lang.String>> getConditionsTestList() {
            return conditionsTestList;
        }
    }

    public class Array {
        List<RawModlObject.ArrayItem> arrayItems;

        public void addArrayItem(ArrayItem arrayItem) {
            if (arrayItems == null) {
                arrayItems = new LinkedList<>();
            }
            arrayItems.add(arrayItem);
        }

        public List<RawModlObject.ArrayItem> getArrayItems() {
            return arrayItems;
        }
    }

    public class ArrayItem {
        Value value;
        ArrayConditional arrayConditional;

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }

        public ArrayConditional getArrayConditional() {
            return arrayConditional;
        }

        public void setArrayConditional(ArrayConditional arrayConditional) {
            this.arrayConditional = arrayConditional;
        }
    }


    public class True {

    }

    public class False {

    }

    public class Null {

    }

    public class ValueConditional {
        java.util.Map<ConditionTest, ValueConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, ValueConditionalReturn> getConditionals() {
            return conditionals;
        }

        public void addConditional(ConditionTest conditionTest, ValueConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public class ValueConditionalReturn {
        List<ValueItem> valueItems;

        public void addValueItem(ValueItem valueItem) {
            if (valueItems == null) {
                valueItems = new LinkedList<>();
            }
            valueItems.add(valueItem);
        }

        public List<ValueItem> getValueItems() {
            return valueItems;
        }
    }

    public class ArrayConditional {
        java.util.Map<ConditionTest, ArrayConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, ArrayConditionalReturn> getConditionals() {
            return conditionals;
        }

        public void addConditional(ConditionTest conditionTest, ArrayConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public class ArrayConditionalReturn {
        List<ArrayItem> arrayItems;

        public void addArrayItem(ArrayItem arrayItem) {
            if (arrayItems == null) {
                arrayItems = new LinkedList<>();
            }
            arrayItems.add(arrayItem);
        }

        public List<ArrayItem> getArrayItems() {
            return arrayItems;
        }
    }

    public class MapConditional {
        java.util.Map<ConditionTest, MapConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, MapConditionalReturn> getConditionals() {
            return conditionals;
        }

        public void addConditional(ConditionTest conditionTest, MapConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public class MapConditionalReturn {
        List<MapItem> mapItems;

        public void addMapItem(MapItem mapItem) {
            if (mapItems == null) {
                mapItems = new LinkedList<>();
            }
            mapItems.add(mapItem);
        }

        public List<MapItem> getMapItems() {
            return mapItems;
        }
    }

    public class TopLevelConditional {
        java.util.Map<ConditionTest, TopLevelConditionalReturn> conditionals;

        public java.util.Map<ConditionTest, TopLevelConditionalReturn> getConditionals() {
            return conditionals;
        }

        public void addConditional(ConditionTest conditionTest, TopLevelConditionalReturn conditionalReturn) {
            if (conditionals == null) {
                conditionals = new LinkedHashMap<>();
            }
            conditionals.put(conditionTest, conditionalReturn);
        }
    }

    public class TopLevelConditionalReturn {
        List<Structure> structures;

        public void addStructure(Structure structure) {
            if (structures == null) {
                structures = new LinkedList<>();
            }
            structures.add(structure);
        }

        public List<Structure> getStructures() {
            return structures;
        }
    }

}
