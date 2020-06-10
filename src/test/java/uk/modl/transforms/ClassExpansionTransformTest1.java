package uk.modl.transforms;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Parent;
import uk.modl.model.*;

public class ClassExpansionTransformTest1 {

    final Ancestry ancestry = new Ancestry();

    final Parent parent = () -> 0;

    @Test
    public void test() {
        final TransformationContext ctx = TransformationContext.emptyCtx();
        final ClassExpansionTransform transform = new ClassExpansionTransform();

        // Create the class
        final Vector<ArrayItem> assign = Vector.of(Array.of(ancestry, parent, Vector.of(StringPrimitive.of(ancestry, parent, "one"), StringPrimitive.of(ancestry, parent, "two"), StringPrimitive.of(ancestry, parent, "three"))));
        final StarClassTransform.ClassInstruction classInstruction = StarClassTransform.ClassInstruction.of("test", null, null, assign, HashMap.empty());
        ctx.addClassInstruction(classInstruction);

        // Create the Pair to be transformed by the class
        final Map pairValue = Map.of(ancestry, parent, Vector.of(Pair.of(ancestry, parent, "one", StringPrimitive.of(ancestry, parent, "1")), Pair.of(ancestry, parent, "two", StringPrimitive.of(ancestry, parent, "2")), Pair.of(ancestry, parent, "three", StringPrimitive.of(ancestry, parent, "3"))));
        final Pair testPair = Pair.of(ancestry, parent, "test", pairValue);

        // Transform the pair
        final Pair updatedPair = (Pair) transform.apply(ctx, testPair);

        // Check the result
        Assert.assertNotNull(updatedPair);
        Assert.assertEquals("test", updatedPair.getKey());
        Assert.assertTrue(updatedPair.getValue() instanceof Map);

        final Map m = (Map) updatedPair.getValue();

        Assert.assertEquals(3, m.getMapItems()
                .size());

        final List<String> expectedKeys = List.of("one", "two", "three");
        final List<String> expectedValues = List.of("1", "2", "3");

        m.getMapItems()
                .forEach(mapItem -> {
                    Assert.assertTrue(mapItem instanceof Pair);
                    final Pair p = (Pair) mapItem;
                    Assert.assertTrue(expectedKeys.contains(p.getKey()));
                    Assert.assertTrue(p.getValue() instanceof StringPrimitive);
                    Assert.assertTrue(expectedValues.contains(p.getValue()
                            .toString()));
                });
    }
}