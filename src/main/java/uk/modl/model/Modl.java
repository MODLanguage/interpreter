package uk.modl.model;

import io.vavr.collection.List;
import lombok.NonNull;
import lombok.Value;

@Value
public class Modl {
  @NonNull
  List<ModlStructure> structures;
}
