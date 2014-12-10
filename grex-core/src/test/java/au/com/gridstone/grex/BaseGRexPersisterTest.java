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

import java.io.File;

import au.com.gridstone.grex.core.TestConverter;

import static org.junit.Assert.fail;

public class BaseGRexPersisterTest {

    @Test( expected = NullPointerException.class )
    public void testConstructor1stArgNull() {
        new BaseGRexPersister(null, mockFileFactory);
    }

    @Test( expected = NullPointerException.class )
    public void testConstructor2ndArgNull() {
        new BaseGRexPersister(new TestConverter(), null);
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

    FileFactory mockFileFactory = new FileFactory() {
        @NotNull
        @Override
        public File getFile(final String key) {
            return null;
        }
    };
}