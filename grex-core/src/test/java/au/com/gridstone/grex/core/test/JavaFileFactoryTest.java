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

package au.com.gridstone.grex.core.test;

import org.junit.Test;

import java.io.File;

import au.com.gridstone.grex.FileFactory;
import au.com.gridstone.grex.core.JavaFileFactory;

import static org.junit.Assert.assertTrue;

public class JavaFileFactoryTest {

    @Test
    public void testGetFile() throws Exception {
        final File directory = new File(System.getProperty("java.io.tmpdir"));
        FileFactory fileFactory = new JavaFileFactory(directory);

        File test = new File(directory,"test");

        assertTrue(fileFactory.getFile("test").equals(test));
    }

    @Test( expected = IllegalArgumentException.class)
    public void ConstructorTestArgNotDirectory() {
        final File file = new File(
                new File(System.getProperty("java.io.tmpdir"))
                ,"some-filename-we-dont-expect-to-exist");
        new JavaFileFactory(file);
    }

}
