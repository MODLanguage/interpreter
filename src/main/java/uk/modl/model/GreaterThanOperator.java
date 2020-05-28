package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class GreaterThanOperator implements Operator {

    public static GreaterThanOperator instance = new GreaterThanOperator();

    private GreaterThanOperator() {
    }

}
