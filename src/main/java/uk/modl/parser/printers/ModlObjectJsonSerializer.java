package uk.modl.parser.printers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import uk.modl.interpreter.ModlObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by alex on 13/09/2018.
 */
public class ModlObjectJsonSerializer extends JsonSerializer<ModlObject> {
    @Override
    public void serialize(ModlObject modlObject, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (modlObject.getStructures().size() > 1) {
            gen.writeStartArray();
        }
        for (ModlObject.Structure structure : modlObject.getStructures()) {
            serialize(structure, gen, serializers);
        }
        if (modlObject.getStructures().size() > 1) {
            gen.writeEndArray();
        }
    }

    public void serialize(ModlObject.Structure structure, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serialize(structure, gen, serializers, true);
    }

    public void serialize(ModlObject.Structure structure, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (structure == null) {
            return;
        }
        if (structure instanceof ModlObject.Array) {
            serialize((ModlObject.Array)structure, gen, serializers);
        }
        if (structure instanceof ModlObject.Pair) {
            serialize((ModlObject.Pair)structure, gen, serializers, startObject);
        }
        if (structure instanceof ModlObject.Map) {
            serialize((ModlObject.Map)structure, gen, serializers);
        }
    }

    private void serialize(ModlObject.Map map, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (map == null) {
            return;
        }
        if (map.getPairs() != null) {
            ModlObject modlObject = new ModlObject();
            gen.writeStartObject();
            for (ModlObject.Pair pair : map.getPairs()) {
                serialize(pair, gen, serializers, false);
            }
            gen.writeEndObject();
        }
//        if (map.getPairs() != null) {
//            ModlObject modlObject = new ModlObject();
//            gen.writeStartObject();
//            for (java.util.Map.Entry<ModlObject.String, ModlObject.Value> mapEntry : map.getPairs().entrySet()) {
//                ModlObject.Pair pair = modlObject.new Pair();
//                pair.setKey(mapEntry.getKey());
//                pair.setValue(mapEntry.getValue());
//                serialize(pair, gen, serializers, false);
//            }
//            gen.writeEndObject();
//        }
        else {
            gen.writeStartObject();
            gen.writeEndObject();
        }
    }

    public void serialize(ModlObject.Value value, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (value == null) {
            return;
        }
        if (value instanceof ModlObject.Pair) {
            serialize((ModlObject.Pair)value, gen, serializers, startObject);
        }
        if (value instanceof ModlObject.Map) {
            serialize((ModlObject.Map)value, gen, serializers);
        }
        if (value instanceof ModlObject.Array) {
            serialize((ModlObject.Array)value, gen, serializers);
        }
        if (value instanceof ModlObject.Number) {
            serialize((ModlObject.Number)value, gen, serializers);
        }
        if (value instanceof ModlObject.True) {
            serialize((ModlObject.True)value, gen, serializers);
        }
        if (value instanceof ModlObject.False) {
            serialize((ModlObject.False)value, gen, serializers);
        }
        if (value instanceof ModlObject.Null) {
            serialize((ModlObject.Null)value, gen, serializers);
        }
        if (value instanceof ModlObject.String) {
            serialize((ModlObject.String)value, gen, serializers);
        }
    }

    private void serialize(ModlObject.Pair pair, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (pair == null) {
            return;
        }
        if (startObject) {
            gen.writeStartObject();
        }
        gen.writeFieldName(pair.getKey().string);
        if (pair.getValues().size() > 0 && pair.getValues().get(0) instanceof ModlObject.Pair) {
            gen.writeStartObject();
        }
        if (pair.getValues().size() > 1 && !(pair.getValues().get(0) instanceof ModlObject.Pair)
                && !(pair.getValues().get(0) instanceof ModlObject.Map)) {
            gen.writeStartArray();
        }
        serialize(pair.getValues(), gen, serializers, startObject);
        if (pair.getValues().size() > 0 && pair.getValues().get(0) instanceof ModlObject.Pair) {
            gen.writeEndObject();
        }
        if (pair.getValues().size() > 1 && !(pair.getValues().get(0) instanceof ModlObject.Pair)
                && !(pair.getValues().get(0) instanceof ModlObject.Map)) {
            gen.writeEndArray();
        }
//        // TODO Do we have an array?
//        if (pair.getValues() != null) {
//            if (pair.getValues().size() > 1) {
//                gen.writeStartArray();
//            }
//            for (RawModlObject.ValueItem value : pair.getValues()) {
//                serialize(value, gen, serializers);
//            }
//            if (pair.getValues().size() > 1) {
//                gen.writeEndArray();
//            }
//        } else if (pair.getArray() != null) {
//            serialize(pair.getArray(), gen, serializers);
//        } else if (pair.getMap() != null) {
//            serialize(pair.getMap(), gen, serializers);
//        }
        if (startObject) {
            gen.writeEndObject();
        }
    }

    private void serialize(List<ModlObject.Value> values, JsonGenerator gen, SerializerProvider serializers, boolean startObject) throws IOException {
        if (values == null || values.size() == 0) {
            return;
        }
//        if (values.size() > 1) {
//            gen.writeStartArray();
//        }
//        if (startObject) {
//            gen.writeStartObject();
//        }
        for (ModlObject.Value value : values) {
//            serialize(value, gen, serializers, startObject);
            serialize(value, gen, serializers, false);
        }
//        if (startObject) {
//            gen.writeEndObject();
//        }
//        if (values.size() > 1) {
//            gen.writeEndArray();
//        }
    }

    private void serialize(ModlObject.Array array, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (array == null) {
            return;
        }
        if (array.getValues() != null) {
            gen.writeStartArray();
            for (ModlObject.Value value : array.getValues()) {
                boolean startObject = false;
                if (value instanceof ModlObject.Pair) {
                    startObject = true;
                }
//                serialize(value, gen, serializers, false);
                serialize(value, gen, serializers, startObject);
            }
            gen.writeEndArray();
        } else {
            gen.writeStartArray();
            gen.writeEndArray();
        }
    }

    private void serialize(ModlObject.Number number, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (number == null) {
            return;
        }
        // TODO Will this work?
        gen.writeNumber(number.number);
    }

    private void serialize(ModlObject.True trueVal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (trueVal == null) {
            return;
        }
        gen.writeBoolean(true);
    }

    private void serialize(ModlObject.False falseVal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (falseVal == null) {
            return;
        }
        gen.writeBoolean(false);
    }

    private void serialize(ModlObject.Null nullVal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (nullVal == null) {
            return;
        }
        gen.writeNull();
    }

    private void serialize(ModlObject.String string, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (string == null) {
            return;
        }
        if (string.string != null) {
            gen.writeString(string.string);
        } else {
            gen.writeString("");
        }
    }

}