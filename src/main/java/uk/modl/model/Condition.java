package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.visitor.ModlVisitor;

@ToString
@EqualsAndHashCode
public class Condition implements ConditionOrConditionGroupInterface {
    public final String lhs;
    public final Operator op;
    public final Vector<ValueItem> values;

    public Condition(final String lhs, final Operator op, final Vector<ValueItem> values) {
        this.lhs = lhs;
        this.op = op;
        this.values = values;
    }

    @Override
    public void visit(final ModlVisitor visitor) {
        visitor.accept(this);
        values.forEach(s -> s.visit(visitor));
    }

}
