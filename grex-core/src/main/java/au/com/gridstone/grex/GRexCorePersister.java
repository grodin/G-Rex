package au.com.gridstone.grex;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import au.com.gridstone.grex.converter.Converter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class GRexCorePersister extends BaseGRexPersister {

    /**
     * Create a new instance using a provided {@link Converter}.
     *
     * @param converter Converter used to serialize/deserialize objects,
     *                  not null
     * @param directory Directory to write/read files,
     *                  not null. {@link File#isDirectory()} must return true
     *                  on this parameter
     */
    public GRexCorePersister(@NotNull final Converter converter,
                             @NotNull final File directory) {
        super(checkNotNull(converter), new FileFactory() {
            @NotNull
            @Override
            public File getFile(final String key) {
                checkArgument(checkNotNull(directory).isDirectory());
                return new File(directory,key);
            }
        });
    }

}
