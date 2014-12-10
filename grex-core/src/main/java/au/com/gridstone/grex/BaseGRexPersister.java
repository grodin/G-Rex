package au.com.gridstone.grex;/*
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


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.com.gridstone.grex.converter.Converter;
import au.com.gridstone.grex.converter.ConverterException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Facilitates the read and write of objects to and from an application's private directory.
 *
 * @author Christopher Horner
 */
public class BaseGRexPersister implements Persister {
    private final Converter converter;

    private final FileFactory fileFactory;

    /**
     * Create a new instance using a provided {@link au.com.gridstone.grex.converter.Converter}
     * and a provided {@link FileFactory}.
     *
     * @param converter Converter used to serialize/deserialize objects.
     * @param fileFactory FileFactory used to get {@link File} to write to and
     *                    read from.
     */
    public BaseGRexPersister(@NotNull final Converter converter,
                             @NotNull final FileFactory fileFactory) {
        this.converter = checkNotNull(converter);
        this.fileFactory = checkNotNull(fileFactory);
    }

    @Override
    public final <T> Observable<List<T>> putList(final String key,
                                                 final List<T>
            list, final Class<T> type) {
        return Observable.create(new Observable.OnSubscribe<List<T>>() {
            @Override
            public void call(Subscriber<? super List<T>> subscriber) {
                Writer writer = null;

                try {
                    File outFile = getFile(key);
                    writer = new FileWriter(outFile);
                    converter.write(list, writer);

                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(list);
                        subscriber.onCompleted();
                    }
                } catch (IOException | ConverterException e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                } finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    } catch (IOException e) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(e);
                        }
                    }
                }
            }
        });
    }

    @Override
    public final <T> Observable<List<T>> getList(final String key,
                                            final Class<T> type) {
        return Observable.create(new Observable.OnSubscribe<List<T>>() {
            @Override
            public void call(Subscriber<? super List<T>> subscriber) {
                Type listType = new ListOfSomething<>(type);
                Reader reader = null;

                try {
                    File inFile = getFile(key);

                    if (!inFile.exists()) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(Collections.<T>emptyList());
                            subscriber.onCompleted();
                        }

                        return;
                    }

                    reader = new FileReader(inFile);
                    List<T> result = converter.read(reader, listType);

                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }

    @Override
    public final <T> Observable<List<T>> addToList(final String key,
                                              final T object, final Class<T> type) {
        return getList(key, type)
                .map(new Func1<List<T>, List<T>>() {
                    @Override
                    public List<T> call(List<T> list) {
                        list = new ArrayList<>(list);
                        list.add(object);
                        return list;
                    }
                })
                .flatMap(new Func1<List<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(List<T> list) {
                        return putList(key, list, type);
                    }
                });
    }

    @Override
    public <T> Observable<List<T>> removeFromList(final String key, final T object, final Class<T> type) {
        return getList(key, type)
                .map(new Func1<List<T>, List<T>>() {
                    @Override
                    public List<T> call(List<T> list) {
                        list = new ArrayList<>(list);
                        list.remove(object);
                        return list;
                    }
                })
                .flatMap(new Func1<List<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(List<T> list) {
                        return putList(key, list, type);
                    }
                });
    }

    @Override
    public final <T> Observable<List<T>> removeFromList(final String key,
                                                   final int position, final Class<T> type) {
        return getList(key, type)
                .map(new Func1<List<T>, List<T>>() {
                    @Override
                    public List<T> call(List<T> list) {
                        list = new ArrayList<>(list);
                        list.remove(position);
                        return list;
                    }
                })
                .flatMap(new Func1<List<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(List<T> list) {
                        return putList(key, list, type);
                    }
                });
    }

    @Override
    public <T> Observable<T> put(final String key, final T object) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Writer writer = null;

                try {
                    File outFile = getFile(key);
                    writer = new FileWriter(outFile);
                    converter.write(object, writer);

                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(object);
                        subscriber.onCompleted();
                    }

                } catch (Exception e) {
                    if (!subscriber.isUnsubscribed())
                        subscriber.onError(e);
                } finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }

    @Override
    public final <T> Observable<T> get(final String key, final Class<T> type) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Reader reader = null;

                try {
                    File inFile = getFile(key);

                    if (!inFile.exists()) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }

                        return;
                    }

                    reader = new FileReader(inFile);
                    T result = converter.read(reader, type);

                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }

    @Override
    public final Observable<Boolean> clear(final String key) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                File file = getFile(key);
                boolean result = file.delete();

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }
            }
        });
    }

    private File getFile(final String key) {
        return fileFactory.getFile(key);
    }

    private static final class ListOfSomething<T> implements ParameterizedType {
        private final Class<?> wrappedType;

        public ListOfSomething(Class<T> wrappedType) {
            this.wrappedType = wrappedType;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{wrappedType};
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        @Override
        public Type getRawType() {
            return List.class;
        }
    }
}
