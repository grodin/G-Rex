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
import java.util.Arrays;
import java.util.List;

import au.com.gridstone.grex.converter.Converter;
import rx.observers.TestSubscriber;

import static au.com.gridstone.grex.BaseGRexPersister.ListOfSomething;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class BaseGRexPersisterTest {

    private final IODelegate mockIODelegate = Mockito.mock(IODelegate.class);

    private final Converter converter = Mockito.mock(Converter.class);


    @Test
    public void testPutReturnsSameObject() throws Exception {
        when(mockIODelegate.getWriter(anyString()))
                .thenReturn(new StringWriter());

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final TestData testData = new TestData("Test", 1);

        final TestData putData = persister.put("TestKey", testData).toBlocking()
                .single();

        assertThat(testData).isEqualTo(putData);
    }

    @Test
    public void testPutCallsIODelegateAndConverter() throws Exception {
        final StringWriter writer = new StringWriter();
        when(mockIODelegate.getWriter(anyString())).thenReturn(writer);

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final TestData testData = new TestData("Test", 1);
        persister.put("TestKey", testData).toBlocking().single();

        verify(mockIODelegate).getWriter("TestKey");
        verifyNoMoreInteractions(mockIODelegate);

        verify(converter).write(testData, writer);
        verifyNoMoreInteractions(converter);
    }

    @Test
    public void testGetCallsIODelegateAndConverter() throws Exception {
        final StringReader reader = new StringReader("");
        when(mockIODelegate.getReader(anyString())).thenReturn(reader);

        when(converter.read(any(Reader.class), eq(Object.class)))
                .thenReturn(new Object());

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        persister.get("TestKey", Object.class).toBlocking().single();

        verify(mockIODelegate).getReader("TestKey");
        verifyNoMoreInteractions(mockIODelegate);

        verify(converter).read(reader, Object.class);
        verifyNoMoreInteractions(converter);
    }

    @Test
    public void testGetWithNonexistentFile() throws Exception {
        //Simulates a file not existing
        when(mockIODelegate.getReader(anyString())).thenReturn(null);

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final List<?> ret = persister.get("TestKey", Object.class).toList()
                .toBlocking().single();

        assertThat(ret).isEmpty();
    }

    @Test
    public void testPutListReturnsSameList() throws Exception {
        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final List<TestData> inList = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            TestData data = new TestData("test" + i, +i);
            inList.add(data);
        }

        final List<TestData> putList = persister.putList("inList", inList,
                TestData.class).toBlocking().single();
        assertThat(putList).containsAll(inList);
    }


    @Test
    public void testPutListCallsIODelegateAndConverter() throws Exception {
        final StringWriter writer = new StringWriter();
        when(mockIODelegate.getWriter(anyString())).thenReturn(writer);

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final List<TestData> inList = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            TestData data = new TestData("test" + i, +i);
            inList.add(data);
        }

        persister.putList("inList", inList, TestData.class)
                .toBlocking().single();

        verify(mockIODelegate).getWriter("inList");
        verifyNoMoreInteractions(mockIODelegate);

        verify(converter).write(inList, writer);
        verifyNoMoreInteractions(converter);
    }

    @Test
    public void testGetListCallsToIODelegateAndConverter() throws Exception {
        final StringReader reader = new StringReader("");
        when(mockIODelegate.getReader(anyString())).thenReturn(reader);

        final List<Object> objects = Arrays.asList(new Object(),
                new Object(), new Object());
        final ListOfSomething<Object> listType =
                ListOfSomething.wrap(Object.class);
        when(converter.read(any(Reader.class), eq(listType)))
                .thenReturn(objects);

        Persister persister = new BaseGRexPersister(converter, mockIODelegate);

        persister.getList("test",Object.class).toBlocking().single();

        verify(mockIODelegate).getReader("test");
        verifyNoMoreInteractions(mockIODelegate);

        verify(converter).read(eq(reader), any(ListOfSomething.class));
        verifyNoMoreInteractions(converter);
    }

    @Test
    public void testAddToList() throws Exception {
        when(mockIODelegate.getReader(eq("Test")))
                .thenReturn(new StringReader(""));
        when(converter.read(any(Reader.class), any(ListOfSomething.class)))
                .thenReturn(Arrays.asList(1,2,3));

        when(mockIODelegate.getWriter(eq("Test")))
                .thenReturn(new StringWriter());

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final List<Integer> ret = persister.addToList("Test",4,Integer.class)
                .toBlocking().single();

        assertThat(ret).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void testAddToList_NoExistingData() throws Exception {
        when(mockIODelegate.getReader(eq("Test")))
                .thenReturn(null); //signal that there is no data for this key

        when(mockIODelegate.getWriter(eq("Test")))
                .thenReturn(new StringWriter());

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final List<Integer> ret = persister.addToList("Test",4,Integer.class)
                .toBlocking().single();

        assertThat(ret).containsExactly(4);
    }

    @Test
    public void testRemoveFromListByObject() throws Exception {
        when(mockIODelegate.getReader(eq("Test")))
                .thenReturn(new StringReader(""));
        when(converter.read(any(Reader.class), any(ListOfSomething.class)))
                .thenReturn(Arrays.asList("1", "2", "3"));

        when(mockIODelegate.getWriter(eq("Test")))
                .thenReturn(new StringWriter());

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final List<String> ret = persister
                .removeFromList("Test","2" , String.class)
                .toBlocking().single();

        assertThat(ret).containsExactly("1","3");
    }

    @Test
    public void testRemoveFromListByIndex() throws Exception {
        when(mockIODelegate.getReader(eq("Test")))
                .thenReturn(new StringReader(""));
        when(converter.read(any(Reader.class), any(ListOfSomething.class)))
                .thenReturn(Arrays.asList(1, 2, 3));

        when(mockIODelegate.getWriter(eq("Test")))
                .thenReturn(new StringWriter());

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final List<Integer> ret = persister
                .removeFromList("Test", 1, Integer.class)
                .toBlocking().single();

        assertThat(ret).containsExactly(1,3);
    }

    @Test
    public void testRemoveFromListByIndex_IndexOutOfBounds() throws Exception {
        when(mockIODelegate.getReader(eq("Test")))
                .thenReturn(new StringReader(""));
        when(converter.read(any(Reader.class), any(ListOfSomething.class)))
                .thenReturn(Arrays.asList(1, 2, 3));

        when(mockIODelegate.getWriter(eq("Test")))
                .thenReturn(new StringWriter());

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        final TestSubscriber<List<Integer>> testSubscriber = new TestSubscriber<>();

        persister.removeFromList("Test", Integer.MAX_VALUE, Integer.class)
                    .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertTerminalEvent();

        final List<Throwable> errors = testSubscriber.getOnErrorEvents();

        assertThat(errors).hasSize(1);

        assertThat(errors.get(0))
                .isExactlyInstanceOf(IndexOutOfBoundsException.class);

    }

    @Test
    public void testClearCallsToDelegate() throws Exception {

        when(mockIODelegate.clear(anyString())).thenReturn(true);

        final Persister persister = new BaseGRexPersister(converter,
                mockIODelegate);

        persister.clear("test").toBlocking().single();

        verify(mockIODelegate).clear(eq("test"));
        verifyNoMoreInteractions(mockIODelegate);

        verifyZeroInteractions(converter);

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
