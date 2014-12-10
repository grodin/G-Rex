/*
 * Copyright (C) GRIDSTONE 2014
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

import android.content.Context;

import java.io.File;

import au.com.gridstone.grex.converter.Converter;
import au.com.gridstone.grex.core.BaseGRexPersister;

import static android.content.Context.MODE_PRIVATE;

/**
 * Facilitates the read and write of objects to and from an application's private directory.
 *
 * @author Christopher Horner
 */
public class GRexPersister extends BaseGRexPersister {
    private final Context context;
    private final String dirName;

    /**
     * Create a new instances using a provided au.com.gridstone.grex.converter.
     *
     * @param context   Context used to determine file directory.
     * @param dirName   The sub directory name to perform all read/write operations to.
     * @param converter Converter used to serialize/deserialize objects.
     */
    public GRexPersister(Context context, String dirName, Converter converter) {
        super(converter);
        this.context = context.getApplicationContext();
        this.dirName = dirName;
    }


    @Override
    protected File getFile(String key) {
        return new File(context.getDir(dirName, MODE_PRIVATE), key);
    }

}
