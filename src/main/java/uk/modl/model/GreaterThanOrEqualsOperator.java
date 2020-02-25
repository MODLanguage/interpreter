package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class GreaterThanOrEqualsOperator implements Operator {
    public static GreaterThanOrEqualsOperator instance = new GreaterThanOrEqualsOperator();

    private GreaterThanOrEqualsOperator() {
    }
}
