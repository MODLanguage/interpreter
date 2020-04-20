package uk.modl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import org.apache.commons.lang3.StringUtils;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.model.Array;
import uk.modl.model.PairValue;
import uk.modl.model.Primitive;
import uk.modl.model.ValueItem;
import uk.modl.parser.errors.InterpreterError;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static final String GRAVE = "`";
    public static final String DOUBLEQUOTE = "\"";
    /**
     * A Regex to match the parameters of a MODL replace method
     */
    private static final Pattern replacerPattern = Pattern.compile("^r<(.*),(.*)>$");
    /**
     * A Regex to match the parameters of a MODL trim method
     */
    private static final Pattern trimmerPattern = Pattern.compile("^t<(.*)>$");
    /**
     * Map a filename to Either an Error or the file contents as a String
     */
    public static Function1<StarLoadExtractor.FileSpec, Tuple2<StarLoadExtractor.FileSpec, String>> getFileContents = (spec) -> {
        try {
            if (spec.filename.startsWith("http")) {
                final URL url = new URL(spec.filename);
                return Tuple.of(spec, new Scanner(url.openStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A")
                        .next());
            } else if (Files.exists(Paths.get(spec.filename))) {
                return Tuple.of(spec, String.join("", Files.readAllLines(Paths.get(spec.filename))));
            }
            return null;
        } catch (final Exception e) {
            throw new RuntimeException("Could not interpret " + e.getMessage());
        }
    };
    /**
     * Map a PairValue to a list of Strings - for use as file names.
     * This is only applicable to *load MODL instructions
     */
    public static Function1<PairValue, Vector<String>> getFilenames = (pairValue) -> {
        if (pairValue instanceof Primitive) {
            final Primitive pv = (Primitive) pairValue;
            return Vector.of(pv.toString());
        }
        if (pairValue instanceof Array) {
            final Array a = (Array) pairValue;
            return Vector.ofAll(a.getArrayItems()
                    .map(Objects::toString));
        }
        return Vector.empty();
    };
    /**
     * Render a JSON Node to a String.
     */
    public static Function1<JsonNode, String> jsonNodeToString = (jsonNode -> {
        try {
            return new ObjectMapper().writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    });

    /**
     * Remove single quotes from a String
     *
     * @param text the possibly quoted String
     * @return an unquoted String
     */
    public static String unquote(final String text) {
        return StringUtils.removeEnd(StringUtils.removeStart(StringUtils.removeEnd(StringUtils.removeStart(text, GRAVE), GRAVE), DOUBLEQUOTE), DOUBLEQUOTE);
    }

    /**
     * Replace parts of a String
     *
     * @param spec a spec of the form: r&lt;this,that&gt;
     * @param s    the String to be processed
     * @return the updated String
     */
    public static String replacer(final String spec, final String s) {
        final Matcher matcher = replacerPattern.matcher(spec);

        if (matcher.find()) {
            final String text = matcher.group(1);
            final String newText = Util.unquote(matcher.group(2));

            return s.replace(text, newText);
        } else {
            throw new InterpreterError("Invalid method: " + spec);
        }
    }

    /**
     * Trim a String
     *
     * @param spec a spec of the form: t&lt;this&gt;
     * @param s    the String to be processed
     * @return the updated String
     */
    public static String trimmer(final String spec, final String s) {
        final Matcher matcher = trimmerPattern.matcher(spec);

        if (matcher.find()) {
            final String text = matcher.group(1);
            final int i = s.indexOf(text);
            if (i > -1) {
                return s.substring(0, i);
            }
        } else {
            throw new InterpreterError("Invalid method: " + spec);
        }
        return s;
    }

    public static boolean greaterThanAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) <= v2;
        })
                .isDefined();
    }

    private static double toDouble(final String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new InterpreterError("Invalid numeric value: " + e.getMessage());
        }
    }

    public static boolean greaterThanOrEqualToAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) < v2;
        })
                .isDefined();
    }

    public static boolean lessThanAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) >= v2;
        })
                .isDefined();
    }

    public static boolean lessThanOrEqualToAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) > v2;
        })
                .isDefined();
    }

}
