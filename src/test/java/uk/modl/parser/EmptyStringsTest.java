/*
MIT License

Copyright (c) 2018 NUM Technology Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package uk.modl.parser;

import org.junit.Assert;
import org.junit.Test;
import uk.modl.interpreter.Interpreter;
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author twalmsley
 * <p>
 * I run the tests individually rather than in a loop to make it easier to see which ones fail.
 * This way the early failures don't mask later ones.
 */
public class EmptyStringsTest {
    private final static String[][] expected = {
        {
            "o=`ok`", "{\n" + "  \"o\" : \"ok\"" + "}"
        }, {
            "o=``", "{\n" + "  \"o\" : \"\"\n" + "}"
        }, {
            "o=\"ok\"", "{\n" + "  \"o\" : \"ok\"" + "}"
        }, {
            "o=\"\"", "{\n" + "  \"o\" : \"\"\n" + "}"
        }
    };

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_00() throws IOException {
        singleCase(expected[0]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_01() throws IOException {
        singleCase(expected[1]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_02() throws IOException {
        singleCase(expected[2]);
    }

    /**
     * @throws IOException on test failure
     */
    @Test
    public void test_03() throws IOException {
        singleCase(expected[3]);
    }

    /**
     * @param test The current test case
     * @throws IOException on test failure
     */
    private void singleCase(String[] test) throws IOException {
        String input = test[0];
        String expected = test[1];

        System.out.println("Input : " + input);
        System.out.println("Expected : " + expected);

//        RawModlObject rawModlObject = ModlObjectCreator.processModlParsed(input);
        ModlObject rawModlObject = Interpreter.interpret(input);
        String output = JsonPrinter.printModl(rawModlObject);
        System.out.println("Output : " + output);
        Assert.assertEquals(expected
                .replace(" ", "")
                .replace("\n", "")
                .replace("\r", ""),
            output
                .replace(" ", "")
                .replace("\n", "")
                .replace("\r", ""));
    }
}