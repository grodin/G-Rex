package au.com.gridstone.grex;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import au.com.gridstone.grex.converter.Converter;

import static com.google.common.base.Preconditions.checkNotNull;

public class GRexCorePersister extends BaseGRexPersister {

    /**
     * Create a new instance using a provided {@link Converter}.
     *
     * @param converter Converter used to serialize/deserialize objects.
     */
    public GRexCorePersister(final Converter converter) {
        super(checkNotNull(converter), new FileFactory() {
            @NotNull
            @Override
            public File getFile(final String key) {
                return null;
            }
        });
    }

}
