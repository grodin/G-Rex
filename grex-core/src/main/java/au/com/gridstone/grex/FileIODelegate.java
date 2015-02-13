/*
 * Copyright 2014 Joseph Cooper
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import static com.omricat.common.base.Preconditions.checkArgument;
import static com.omricat.common.base.Preconditions.checkNotNull;

/**
 * Implementation of ReaderWriterFactory which returns {@link java.io.FileReader}
 * and {@link java.io.FileWriter}.
 */
public class FileIODelegate implements IODelegate {

    private final File directory;

    public FileIODelegate(@NotNull final File directory) {
        checkNotNull(directory);
        checkArgument(directory.isDirectory());
        this.directory = directory;
    }


    @Override
    public Reader getReader(@NotNull final String key) throws IOException {
        final File file = getFile(checkNotNull(key));
        if (!file.exists()) {
            return null;
        } else {
            return new FileReader(file);
        }
    }

    @NotNull @Override
    public Writer getWriter(@NotNull final String key) throws IOException {
        return new FileWriter(getFile(checkNotNull(key)));
    }

    @Override public boolean clear(@NotNull final String key) {
        return getFile(key).delete();
    }

    private File getFile(final String key) {
        return new File(directory, key);
    }
}
