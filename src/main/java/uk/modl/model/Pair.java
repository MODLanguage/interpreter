package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Pair implements Structure, MapItem, ValueItem, ArrayItem {
    public final String key;
    public final PairValue value;

    public Pair(final String key, final PairValue value) {
        this.key = key;
        this.value = value;
    }
}
