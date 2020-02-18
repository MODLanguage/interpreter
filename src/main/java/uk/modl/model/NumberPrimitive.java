package uk.modl.model;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.visitor.ModlVisitor;

@EqualsAndHashCode
public class NumberPrimitive implements Primitive {
    public final String value;

    public NumberPrimitive(final String value) {
        this.value = value;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public String toString() {
        return value;
    }

    public Number numericValue() {
        return NumberUtils.createNumber(value);
    }
}
