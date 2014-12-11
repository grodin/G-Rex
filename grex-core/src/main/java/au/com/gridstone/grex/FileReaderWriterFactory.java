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

/**
 * Implementation of ReaderWriterFactory which returns {@link java.io.FileReader}
 * and {@link java.io.FileWriter}.
 */
public class FileReaderWriterFactory implements ReaderWriterFactory {

    @NotNull @Override
    public Reader getReader(final File file) throws IOException {
        return new FileReader(file);
    }

    @NotNull @Override
    public Writer getWriter(final File file) throws IOException {
        return new FileWriter(file);
    }
}
