package uk.modl.model;

import lombok.NonNull;
import lombok.Value;

/**
 * An object to match the ANTLR4 MODL grammar
 */
@Value
public class ModlString implements ModlPrimitive {
  @NonNull
  String value;
}
