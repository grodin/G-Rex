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

import org.jetbrains.annotations.NotNull;

import java.io.File;

import au.com.gridstone.grex.FileFactory;

import static com.omricat.common.base.Preconditions.checkArgument;

public class JavaFileFactory implements FileFactory {

    private final File directory;

    public JavaFileFactory(final File directory) {
        checkArgument(directory.isDirectory());
        this.directory = directory;
    }

    @NotNull
    @Override
    public File getFile(final String key) {
        return new File(directory, key);
    }
}
