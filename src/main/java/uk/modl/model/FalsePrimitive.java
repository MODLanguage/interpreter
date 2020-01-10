package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class FalsePrimitive implements Primitive {
    public static final FalsePrimitive instance = new FalsePrimitive();

    private FalsePrimitive() {

    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public String text() {
        return "00";
    }
}
