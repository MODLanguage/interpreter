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

/**
 * @author twalmsley
 */
public class ScratchTest {
    private final static String[][] expected = {
            {
                    "*class(\n" +
                            " *id=desc;\n" +
                            " *name=description;\n" +
                            " *superclass=str\n" +
                            ");\n" +
                            "\n" +
                            "*class(\n" +
                            " *id=val;\n" +
                            " *name=value;\n" +
                            " *superclass=str\n" +
                            ");\n" +
                            "\n" +
                            "*class(\n" +
                            " *id=media1;\n" +
                            " *name=media1;\n" +
                            " *assign=[\n" +
                            "   [desc;val]\n" +
                            " ]\n" +
                            ");\n" +
                            "\n" +
                            "*class(\n" +
                            " *id=media2;\n" +
                            " *name=media2;\n" +
                            " *assign=[\n" +
                            "   [desc;val]\n" +
                            " ]\n" +
                            ");\n" +
                            "*class(\n" +
                            " *id=list;\n" +
                            " *name=list;\n" +
                            " *assign[\n" +
                            "   [media1;media2]\n" +
                            " ]\n" +
                            ");\n" +
                            "\n" +
                            "\n" +
                            "list=[tel;fb]:[yt;tw]",
                    "{\n" +
                            "  \"list\": [\n" +
                            "    {\n" +
                            "      \"description\": \"tel\",\n" +
                            "      \"value\": \"fb\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"description\": \"yt\",\n" +
                            "      \"value\": \"tw\"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}"
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
     * @param test The current test case
     * @throws IOException on test failure
     */
    private void singleCase(final String[] test) throws IOException {
        String input = test[0];
        String expected = test[1];

        System.out.println("Input : " + input);
        System.out.println("Expected : " + expected);

        ModlObject interpreted = Interpreter.interpret(input);
        String output = JsonPrinter.printModl(interpreted);
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