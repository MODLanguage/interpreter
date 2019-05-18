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

package uk.modl.interpreter;

import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;
import uk.modl.parser.RawModlObject;

import java.util.*;

public class ModlClassLoader {

    public static void loadClass(RawModlObject.Structure structure, Map<String, Map<String, Object>> klasses, Interpreter interpreter) {
        if (structure instanceof ModlObject.Pair) {
            ModlObject.Pair pair = (ModlObject.Pair) structure;
            if (pair == null || (!(((pair.getKey().string.toLowerCase().equals("*class")) ||
                    (pair.getKey().string.toLowerCase().equals("*c")))))) {
                throw new RuntimeException("Expecting '*class' in ModlClassLoader");
            }
//            interpreter.addToUpperCaseInstructions(pair.getKey().string);
            loadClassStructure(structure, klasses, interpreter);
        }
    }

    private static void loadClassStructure(RawModlObject.Structure structure, Map<String, Map<String, Object>> klasses, Interpreter interpreter) {
        // Load in the new klass
        HashMap<String, Object> values = new LinkedHashMap<>();
        String id = getPairValueFor(structure, "*id", interpreter);
        if (id == null) {
            id = getPairValueFor(structure, "*i", interpreter);
        }
        if (id == null) {
            throw new RuntimeException("Can't find *id in *class");
        }
        klasses.put(id, values);
        values.put("*id", id);
        String superclass = getPairValueFor(structure, "*superclass", interpreter);
        if (superclass == null) {
            superclass = getPairValueFor(structure, "*s", interpreter);
        }
        values.put("*superclass", superclass);
        String name = getPairValueFor(structure, "*name", interpreter);
        if (name == null) {
            name = getPairValueFor(structure, "*n", interpreter);
        }
        if (name == null) {
            name = id;
        }
        values.put("*name", name); // TODO ???
        // Go through the structure and find all the new values and add them (replacing any already there from superklass)
        for (RawModlObject.Pair mapItem : ((ModlObject.Map) ((ModlObject.Pair) structure).getModlValue()).getPairs()) {
            // Remember to avoid "_id" and "_sc" !
            if (mapItem.getKey().string.toLowerCase().equals("*id") || mapItem.getKey().string.toLowerCase().equals("*i") ||
                    mapItem.getKey().string.toLowerCase().equals("*superclass") || mapItem.getKey().string.toLowerCase().equals("*s")) {
                continue;
            }
            if (mapItem.getKey().string.toLowerCase().equals("*assign") || mapItem.getKey().string.toLowerCase().equals("*a")) {
                interpreter.addToUpperCaseInstructions(mapItem.getKey().string);
                if (mapItem.getModlValue() instanceof ModlObject.Array) {
                    loadParams(values, (ModlObject.Array) (mapItem.getModlValue()));
                }
                continue;
            }
            // Now add the new value
            if (mapItem.getKey().string.toLowerCase().equals("*n") || mapItem.getKey().string.toLowerCase().equals("*name")) {
                values.put("*name", mapItem.getModlValue());
            } else {
                values.put(mapItem.getKey().string, mapItem.getModlValue());
            }
        }
    }

    private static Map<String, Object> findSuperclassByNameOrId(final Map<String, Map<String, Object>> klasses,
                                                                final String idOrName) {
        // Try getting by ID
        Map<String, Object> parentClass = klasses.get(idOrName);
        if(parentClass == null) {
            // Failed so search by name instead
            for(final Map<String, Object> parent : klasses.values()) {
                final Object parentName = parent.get("*name");
                if(parentName != null) {
                    if(parentName instanceof ModlObject.String) {
                        if (((ModlObject.String)parentName).string.equals(idOrName)) {
                            parentClass = parent;
                            break;
                        }
                    }
                    if(parentName instanceof String) {
                        if ((parentName).equals(idOrName)) {
                            parentClass = parent;
                            break;
                        }
                    }
                }
            }
        }
        return parentClass;
    }

    private static void loadParams(HashMap<String, Object> values, RawModlObject.Array array) {
        // _params : add like _params<n> where n is number of values in array
        for (ModlValue v : array.getValues()) {
            RawModlObject.Array a = (ModlObject.Array) v;
            String key = "*params" + a.getValues().size();
            List<ModlValue> vs = new LinkedList<>();
            for (ModlValue ai : a.getValues()) {
                vs.add(ai);
            }
            values.put(key, vs);
        }
    }

    public static String getPairValueFor(RawModlObject.Structure structure, String pairValue, Interpreter interpreter) {
        for (RawModlObject.Pair mapItem : ((ModlObject.Map) ((ModlObject.Pair) structure).getModlValue()).getPairs()) {
            if (mapItem.getKey().string.toLowerCase().equals(pairValue.toLowerCase())) {
                interpreter.addToUpperCaseInstructions(mapItem.getKey().string);
                // TODO This does not need to be a String!
                return ((ModlObject.String) mapItem.getModlValue()).string;
            }
        }
        return null;
    }

    public static void loadModlKlassO(RawModlObject rawModlObject, Map<String, Map<String, Object>> klasses) {
                /*
        klass(
            _id=o;
            _sc=map;
            _name=object;
              _help="All objects without a class use this class";
            _output=m
          )
         */
        // TODO Get this to work for including more files during the load. Anything to do?
        Map<String, Object> o = new LinkedHashMap<>();
        RawModlObject.String superclass = new ModlObject.String("map");
        o.put("*superclass", superclass);
        RawModlObject.String name = new ModlObject.String("o");
        o.put("*name", name);
        RawModlObject.String output = new ModlObject.String("map");
        o.put("*output", output);


        klasses.put("o", o);
    }
}
