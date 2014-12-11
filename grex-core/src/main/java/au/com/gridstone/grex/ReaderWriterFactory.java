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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;


/**
 * Factory which can provide {@link java.io.Reader} and {@link java.io.Writer}
 * instances.
 * <p/>
 * Most uses of this interface will probably use the {@link
 * FileReaderWriterFactory} implementation.
 *
 * @author Joseph Cooper
 */
public interface ReaderWriterFactory {

    /**
     * Returns a {@link java.io.Reader} for a given {@link java.io.File}.
     * <p/>
     * Must not return null.
     */
    @NotNull public Reader getReader(final File file) throws IOException;

    /**
     * Returns a {@link java.io.Writer} for a given {@link java.io.File}.
     * <p/>
     * Must not return null.
     */
    @NotNull public Writer getWriter(final File file) throws IOException;

}
