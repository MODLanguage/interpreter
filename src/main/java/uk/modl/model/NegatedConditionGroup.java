package uk.modl.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.tuple.ImmutablePair;

@ToString
@EqualsAndHashCode
public class NegatedConditionGroup implements ConditionOrConditionGroupInterface {
    public final List<ImmutablePair<ConditionTest, String>> subConditionList;

    public NegatedConditionGroup(final List<ImmutablePair<ConditionTest, String>> subConditionList) {
        this.subConditionList = subConditionList;
    }
}
