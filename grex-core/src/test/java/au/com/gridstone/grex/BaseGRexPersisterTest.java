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

package au.com.gridstone.grex;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import au.com.gridstone.grex.core.TestConverter;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class BaseGRexPersisterTest {

    @Test( expected = NullPointerException.class )
    public void testConstructor1stArgNull() {
        new BaseGRexPersister(null, new TestFileFactory(), mockIOFactory);
    }

    @Test( expected = NullPointerException.class )
    public void testConstructor2ndArgNull() {
        new BaseGRexPersister(new TestConverter(), null, mockIOFactory);
    }

    @Test( expected = NullPointerException.class )
    public void testConstructor3rdArgNull() {
        new BaseGRexPersister(new TestConverter(), new TestFileFactory(), null);
    }

    private void unimplementedTest() {
        fail("Test not implemented yet");
    }




    @Test
    public void testPutList() throws Exception {
        unimplementedTest();
    }

    @Test
    public void testGetList() throws Exception {
        unimplementedTest();
    }

    @Test
    public void testAddToList() throws Exception {
        unimplementedTest();
    }

    @Test
    public void testRemoveFromList() throws Exception {
        unimplementedTest();
    }

    @Test
    public void testRemoveFromList1() throws Exception {
        unimplementedTest();
    }

    @Test
    public void testPut() throws Exception {
        unimplementedTest();
    }

    @Test
    public void testGet() throws Exception {
        unimplementedTest();
    }

    @Test
    public void testClear() throws Exception {
        unimplementedTest();
    }

    static class TestFileFactory implements FileFactory {

        private final boolean fileExists;

        TestFileFactory(final boolean fileExists) {
            this.fileExists = fileExists;
        }

        TestFileFactory() {
            this(true);
        }

        @NotNull @Override public File getFile(final String key) {
            File file = Mockito.mock(File.class);
            when(file.exists()).thenReturn(true);
            return file;
        }
    }


    ReaderWriterFactory mockIOFactory = new ReaderWriterFactory() {

        final Reader reader = new StringReader("");
        final StringWriter writer = new StringWriter();

        @NotNull @Override
        public Reader getReader(final File file) throws IOException {
            return reader;
        }

        @NotNull @Override
        public Writer getWriter(final File file) throws IOException {
            return writer;
        }
    };
}
