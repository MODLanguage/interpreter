package uk.modl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import uk.modl.extractors.StarLoadExtractor;
import uk.modl.model.Array;
import uk.modl.model.PairValue;
import uk.modl.model.Primitive;
import uk.modl.model.ValueItem;
import uk.modl.parser.errors.StarLoadException;

import java.net.IDN;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Util {

    /**
     * The disallowed characters for a pair key.
     */
    public final String INVALID_CHARS = "!$-+'#^*£&";

    /**
     * Map a filename to Either an Error or the file contents as a String
     */
    public final Function1<StarLoadExtractor.FileSpec, Tuple2<StarLoadExtractor.FileSpec, String>> getFileContents = (spec) -> {
        try {
            if (spec.getFilename()
                    .startsWith("http")) {

                // Load over the net
                final URL url = new URL(spec.getFilename());
                return Tuple.of(spec, new Scanner(url.openStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A")
                        .next());
            } else if (Files.exists(Paths.get(spec.getFilename()))) {

                // Load local file
                return Tuple.of(spec, String.join("", Files.readAllLines(Paths.get(spec.getFilename()))));
            }
        } catch (final Exception e) {
            throw new StarLoadException("Could not load resource: " + e.getMessage());
        }
        throw new StarLoadException("Could not load resource: " + spec.getFilename());
    };

    /**
     * Map a PairValue to a list of Strings - for use as file names.
     * This is only applicable to *load MODL instructions
     */
    public final Function1<PairValue, Vector<String>> getFilenames = (pairValue) -> {
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
     * A pattern used for splitting method lists correctly.
     */
    private final Pattern METHODS_PATTERN = Pattern.compile("replace<[^.]*>|r<[^.]*>|t<[^<>]+>|trim<[^<>]+>|initcap|[^.]\\w+|\\w|u|e|p|s|i|d|[^%.][0-9]+");

    /**
     * A Regex to match the parameters of a MODL replace method
     */
    private final Pattern replacerPattern = Pattern.compile("^replace<(.*),(.*)>$|^r<(.*),(.*)>$");

    /**
     * A Regex to match the parameters of a MODL trim method
     */
    private final Pattern trimmerPattern = Pattern.compile("^trim<(.*)>$|^t<(.*)>$");

    /**
     * Render a JSON Node to a String.
     */
    public Function1<JsonNode, String> jsonNodeToString = (jsonNode -> {
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
    public String unquote(final String text) {
        if (text == null) {
            return null;
        }

        if (text.startsWith("`") && text.endsWith("`")) {
            return StringUtils.unwrap(text, "`");
        }
        return StringUtils.unwrap(text, "\"");
    }

    /**
     * Replace parts of a String
     *
     * @param spec a spec of the form: r&lt;this,that&gt;
     * @param s    the String to be processed
     * @return the updated String
     */
    public String replacer(final String spec, final String s) {
        final Matcher matcher = replacerPattern.matcher(spec);

        if (matcher.find()) {
            final String text = (matcher.group(1) != null) ? matcher.group(1) : matcher.group(3);
            final String rep = (matcher.group(2) != null) ? matcher.group(2) : matcher.group(4);
            final String newText = Util.unquote(rep);
            final String oldtext = Util.unquote(text);
            return s.replace(oldtext, newText);
        } else {
            throw new RuntimeException("Invalid method: " + spec);
        }
    }

    /**
     * Trim a String
     *
     * @param spec a spec of the form: t&lt;this&gt;
     * @param s    the String to be processed
     * @return the updated String
     */
    public String trimmer(final String spec, final String s) {
        final Matcher matcher = trimmerPattern.matcher(spec);

        if (matcher.find()) {
            final String text = (matcher.group(1) != null) ? matcher.group(1) : matcher.group(2);
            final int i = s.indexOf(text);
            if (i > -1) {
                return s.substring(0, i);
            }
        } else {
            throw new RuntimeException("Invalid method: " + spec);
        }
        return s;
    }

    public boolean greaterThanAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) <= v2;
        })
                .isDefined();
    }

    private double toDouble(final String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid numeric value: " + e.getMessage());
        }
    }

    public boolean greaterThanOrEqualToAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) < v2;
        })
                .isDefined();
    }

    public boolean lessThanAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) >= v2;
        })
                .isDefined();
    }

    public boolean lessThanOrEqualToAll(final ValueItem lhs, final Vector<ValueItem> values) {
        return !values.find(v -> {
            final double v2 = toDouble(v.toString());
            return toDouble(lhs.toString()) > v2;
        })
                .isDefined();
    }

    public boolean truthy(final PairValue value) {
        if (value != null) {
            final String s = value.toString();
            return !s.equalsIgnoreCase("null") && !s.equalsIgnoreCase("000") && !s.equalsIgnoreCase("00") && !s.equalsIgnoreCase("false");
        }
        return false;
    }

    public String handleMethodsAndTrailingPathComponents(final String[] refList, String
            valueStr) {
        for (final String pathComponent : refList) {
            switch (pathComponent) {
                case "p":
                case "punydecode":
                    valueStr = replacePunycode(Util.unquote(valueStr));
                    break;
                case "u":
                case "upcase":
                    valueStr = Util.unquote(valueStr)
                            .toUpperCase();
                    break;
                case "d":
                case "downcase":
                    valueStr = Util.unquote(valueStr)
                            .toLowerCase();
                    break;
                case "i":
                case "initcap":
                    valueStr = WordUtils.capitalize(Util.unquote(valueStr));
                    break;
                case "s":
                case "sentence":
                    valueStr = StringUtils.capitalize(Util.unquote(valueStr));
                    break;
                case "e":
                case "urlencode":
                    try {
                        valueStr = URLEncoder.encode(valueStr, StandardCharsets.UTF_8.toString());
                    } catch (final Exception e) {
                        throw new RuntimeException("Error processing URL encoding instruction: " + e.getMessage());
                    }
                    break;
                default:
                    if (pathComponent.startsWith("r<") || pathComponent.startsWith("replace<")) {
                        valueStr = Util.replacer(pathComponent, valueStr);
                    } else if (pathComponent.startsWith("t<") || pathComponent.startsWith("trim<")) {
                        valueStr = Util.trimmer(pathComponent, valueStr);
                    }
                    break;
            }
        }
        return valueStr;
    }

    public String replacePunycode(final String s) {
        // Prefix it with xn-- (the letters xn and two dashes) and decode using punycode / IDN library. Replace the full part (including graves) with the decoded value.
        if (s == null) {
            return null;
        }

        return IDN.toUnicode("xn--" + s);
    }


    public Vector<String> toMethodList(final String chainedMethods) {
        final Matcher matcher = METHODS_PATTERN.matcher(chainedMethods);
        Vector<String> methods = Vector.empty();
        while (matcher.find()) {
            final String match = matcher.group(0);
            methods = methods.append(match);
        }
        return methods;
    }

    /**
     * Remove graves etc.
     *
     * @param text the String to be normalized
     * @return the normalized String
     */
    public StarLoadExtractor.FileSpec normalize(@NonNull final String text) {
        String normalized = text;

        if (normalized.length() > 1 && normalized.startsWith("`") && normalized.endsWith("`")) {
            // Remove graves
            normalized = StringUtils.unwrap(normalized, "`");
        } else if (normalized.length() > 1 && normalized.startsWith("\"") && normalized.endsWith("\"")) {
            // Remove quotes
            normalized = StringUtils.unwrap(normalized, "\"");
        }

        final boolean forceLoad = (normalized.endsWith("!"));
        normalized = StringUtils.removeEnd(normalized, "!");

        if (!normalized.endsWith(".modl") && !normalized.endsWith(".txt")) {
            // Add a file extension
            normalized += ".modl";
        }

        normalized = normalized.replace("~://", "://");

        return StarLoadExtractor.FileSpec.of(normalized, forceLoad);
    }

    /**
     * Convert literals of the form %`%abc` to %abc
     * Convert literals of the form %`abc%` to abc%
     * Convert literals of the form %`%abc%` to %abc%
     *
     * @param literal String
     * @return String
     */
    public String unwrapLiteral(final String literal) {
        if (literal == null || !literal.startsWith("%`")) {
            return literal;
        }
        return StringUtils.unwrap(literal.substring(1), "`");
    }

    public void validatePairKey(final String newKey) {
        final String k = (newKey.startsWith("_") || newKey.startsWith("*")) ? newKey.substring(1) : newKey;// Strip any leading underscore or asterisk

        final int badCharIndex = StringUtils.indexOfAny(k, Util.INVALID_CHARS);
        if (badCharIndex > -1) {
            throw new RuntimeException("Invalid key - \"" +
                    k.charAt(badCharIndex) +
                    "\" character not allowed: " +
                    newKey);
        }
        if (StringUtils.isNumeric(k)) {
            throw new RuntimeException("Invalid key - \"" + k + "\" - entirely numeric keys are not allowed: " + newKey);
        }
    }

}
