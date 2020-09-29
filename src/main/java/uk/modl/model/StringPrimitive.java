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

package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.val;
import org.apache.commons.lang3.math.NumberUtils;
import uk.modl.ancestry.Ancestry;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;
import uk.modl.utils.IDSource;

@Value(staticConstructor = "of")
public class StringPrimitive implements Primitive, Child {

    @ToString.Exclude
    long id;

    @EqualsAndHashCode.Exclude
    String value;

    public static StringPrimitive of(final Ancestry ancestry, final Parent parent, final String value) {
        val child = StringPrimitive.of(IDSource.nextId(), value);
        ancestry.add(parent, child);
        return child;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Number numericValue() {
        return NumberUtils.createNumber(value);
    }

    public StringPrimitive with(final Ancestry ancestry, final String value) {
        val child = StringPrimitive.of(id, value);
        ancestry.replaceChild(this, child);
        return child;
    }

}
