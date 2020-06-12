package uk.modl.model;

import io.vavr.collection.Vector;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;


@Value(staticConstructor = "of")
public class TopLevelConditionalReturn implements Structure, Parent, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    @NonNull
    Vector<Structure> structures;

    public static TopLevelConditionalReturn of(final Ancestry ancestry, final Parent parent, final Vector<Structure> structures) {
        final TopLevelConditionalReturn child = TopLevelConditionalReturn.of(IDSource.nextId(), structures);
        ancestry.add(parent, child);
        return child;
    }

    public TopLevelConditionalReturn with(final Ancestry ancestry, final Vector<Structure> structures) {
        final TopLevelConditionalReturn child = TopLevelConditionalReturn.of(id, structures);
        ancestry.replaceChild(this, child);
        return child;
    }

}
