package uk.modl.interpreter;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.transforms.JacksonJsonNodeTransformer;
import uk.modl.utils.Util;

public class ComplexReferenceTest3 {

    public static final String EXPECTED = "{\"x\":true}";
    public static final String INPUT = "_x=[1;2;3;4];_y=[1;2;3;4];x={%x.2=%y.2?}";

    @Test
    public void parseOk() {
        val f = new Interpreter().andThen(new JacksonJsonNodeTransformer())
                .andThen(Util.jsonNodeToString);

        Assert.assertEquals(EXPECTED, f.apply(INPUT));
    }

}