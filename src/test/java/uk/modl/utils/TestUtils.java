/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package uk.modl.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.Tuple2;
import lombok.experimental.UtilityClass;
import org.junit.Assert;
import uk.modl.interpreter.Interpreter;
import uk.modl.model.Modl;
import uk.modl.transforms.JacksonJsonNodeTransform;
import uk.modl.transforms.TransformationContext;

import java.net.MalformedURLException;

@UtilityClass
public class TestUtils {

    public void runTest(final String input, final String expected) throws MalformedURLException {
        final Interpreter interpreter = new Interpreter();
        final TransformationContext ctx = TransformationContext.baseCtx(null);
        final Tuple2<TransformationContext, Modl> interpreted = interpreter.apply(ctx, input);
        final JsonNode json = new JacksonJsonNodeTransform(ctx).apply(interpreted._2);
        final String result = Util.jsonNodeToString(json);

        Assert.assertEquals(expected, result);
    }

}
