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

import au.com.gridstone.grex.BaseGRexPersister;
import au.com.gridstone.grex.FileIODelegate;
import au.com.gridstone.grex.converter.Converter;

import static com.omricat.common.base.Preconditions.checkNotNull;


public class GRexCorePersister extends BaseGRexPersister {

    /**
     * Create a new instance using a provided {@link Converter}.
     *
     * @param converter Converter used to serialize/deserialize objects, not
     *                  null
     * @param directory Directory to write/read files, not null. {@link
     *                  File#isDirectory()} must return true on this parameter
     */
    public GRexCorePersister(@NotNull final Converter converter,
                             @NotNull final File directory) {
        super(checkNotNull(converter), new FileIODelegate(checkNotNull
                (directory)) {
        });
    }

}
