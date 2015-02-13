/*
 * Copyright 2014 Omricat Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.com.gridstone.grex.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

import au.com.gridstone.grex.converter.Converter;
import au.com.gridstone.grex.converter.ConverterException;

public class TestConverter implements Converter {

    TestData testData = null;


    @Override
    public <T> void write(final T data, final Writer writer) throws ConverterException {
        testData = new TestData(data);
        try {
            writer.write(data.toString());
        } catch (IOException e) {
            throw new ConverterException(e);
        }
    }


    @Override
    public <T> T read(final Reader reader, final Type type) throws
            ConverterException {
        if (testData == null) {
            throw new ConverterException(new FileNotFoundException());
        }
        try {
            return (T) testData.data;
        } catch (ClassCastException e) {
            throw new ConverterException(e);
        }
    }

    private static class TestData {
        final Type type;
        final Object data;

        TestData(final Object data) {
            this.type = data.getClass();
            this.data = data;
        }
    }
}
