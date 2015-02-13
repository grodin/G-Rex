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
import java.util.ArrayList;
import java.util.List;

import au.com.gridstone.grex.core.TestConverter;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void testPutReturnsSameObject() throws Exception {
        Persister persister = new BaseGRexPersister(new TestConverter(),
                new TestFileFactory(), mockIOFactory);

        TestData testData = new TestData("Test", 1);

        TestData putData = persister.put("TestKey", testData).toBlocking()
                .single();

        assertThat(testData).isEqualTo(putData);
    }

    @Test
    public void testPutThenGet() throws Exception {
        Persister persister = new BaseGRexPersister(new TestConverter(),
                new TestFileFactory(), mockIOFactory);

        TestData testData = new TestData("Test", 1);
        persister.put("TestKey", testData).toBlocking().single();

        TestData getData = persister.get("TestKey", TestData.class)
                .toBlocking().single();

        assertThat(testData).isEqualTo(getData);
    }

    @Test
    public void testGetWithNonexistentFile() throws Exception {
        Persister persister = new BaseGRexPersister(new TestConverter(),
                new TestFileFactory(false), mockIOFactory);

        Object ret = persister.get("TestKey", Object.class).toBlocking()
                .single();

        assertThat(ret).isNull();
    }

    @Test
    public void testPutListReturnsSameList() throws Exception {
        Persister persister = new BaseGRexPersister(new TestConverter(),
                new TestFileFactory(), mockIOFactory);

        List<TestData> inList = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            TestData data = new TestData("test" + i, +i);
            inList.add(data);
        }

        List<TestData> putList = persister.putList("inList", inList,
                TestData.class).toBlocking().single();
        assertThat(putList).containsAll(inList);
    }

    @Test
    public void testPutListThenGetList() throws Exception {
        Persister persister = new BaseGRexPersister(new TestConverter(),
                new TestFileFactory(), mockIOFactory);

        List<TestData> inList = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            TestData data = new TestData("test" + i, +i);
            inList.add(data);
        }

        persister.putList("inList", inList,
                TestData.class).toBlocking().single();

        List<TestData> getList = persister.getList("inList",
                TestData.class).toBlocking().single();
        assertThat(getList).containsAll(inList);
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
            when(file.exists()).thenReturn(fileExists);
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

    static class TestData {
        public String string;
        public int integer;

        public TestData() {
        }

        public TestData(String string, int integer) {
            this.string = string;
            this.integer = integer;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TestData)) {
                return false;
            }

            TestData otherData = (TestData) o;

            if (string != null)
                return string.equals(otherData.string) && integer ==
                        otherData.integer;

            return otherData.string == null && integer == otherData.integer;
        }
    }

}
