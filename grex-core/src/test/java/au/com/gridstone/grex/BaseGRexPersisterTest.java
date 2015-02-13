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

import org.junit.Test;
import org.mockito.Mockito;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import au.com.gridstone.grex.converter.Converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseGRexPersisterTest {

    private final IODelegate mockIODelegate = Mockito.mock(IODelegate.class);

    private final Converter converter = Mockito.mock(Converter.class);

    @Test( expected = NullPointerException.class )
    public void testConstructor1stArgNull() {
        new BaseGRexPersister(null,  mockIODelegate);
    }


    @Test( expected = NullPointerException.class )
    public void testConstructor2ndArgNull() {
        new BaseGRexPersister(converter, null);
    }

    @Test
    public void testPutReturnsSameObject() throws Exception {
        when(mockIODelegate.getWriter(anyString()))
                .thenReturn(new StringWriter());

        Persister persister = new BaseGRexPersister(converter, mockIODelegate);

        TestData testData = new TestData("Test", 1);

        TestData putData = persister.put("TestKey", testData).toBlocking()
                .single();

        assertThat(testData).isEqualTo(putData);
    }

    @Test
    public void testPutCallsIODelegateAndConverter() throws Exception {
        final StringWriter writer = new StringWriter();
        when(mockIODelegate.getWriter(anyString())).thenReturn(writer);

        Persister persister = new BaseGRexPersister(converter, mockIODelegate);

        TestData testData = new TestData("Test", 1);
        persister.put("TestKey", testData).toBlocking().single();

        verify(mockIODelegate).getWriter("TestKey");

        verify(converter).write(testData, writer);
    }

    @Test
    public void testGetCallsIODelegateAndConverter() throws Exception {
        final StringReader reader = new StringReader("");
        when(mockIODelegate.getReader(anyString())).thenReturn(reader);

        when(converter.read(any(Reader.class), eq(Object.class)))
                .thenReturn(new Object());

        Persister persister = new BaseGRexPersister(converter, mockIODelegate);

        persister.get("TestKey", Object.class).toBlocking().single();

        verify(mockIODelegate).getReader("TestKey");

        verify(converter).read(reader, Object.class);

    }

    @Test
    public void testGetWithNonexistentFile() throws Exception {
        //Simulates a file not existing
        when(mockIODelegate.getReader(anyString())).thenReturn(null);

        Persister persister = new BaseGRexPersister(converter, mockIODelegate);

        List<?> ret = persister.get("TestKey", Object.class).toList()
                .toBlocking().single();

        assertThat(ret).isEmpty();
    }

    @Test
    public void testPutListReturnsSameList() throws Exception {
        Persister persister = new BaseGRexPersister(converter, mockIODelegate);

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
    public void testPutListCallsIODelegateAndConverter() throws Exception {
        final StringWriter writer = new StringWriter();
        when(mockIODelegate.getWriter(anyString())).thenReturn(writer);

        Persister persister = new BaseGRexPersister(converter, mockIODelegate);

        List<TestData> inList = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            TestData data = new TestData("test" + i, +i);
            inList.add(data);
        }

        persister.putList("inList", inList, TestData.class)
                .toBlocking().single();

        verify(mockIODelegate).getWriter("inList");

        verify(converter).write(inList, writer);
    }

    static class TestData {
        public String string;
        public int integer;

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
