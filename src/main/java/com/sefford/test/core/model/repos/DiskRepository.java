package com.sefford.test.core.model.repos;

import com.jakewharton.disklrucache.DiskLruCache;
import com.sefford.kor.repositories.BaseRepository;
import com.sefford.kor.repositories.interfaces.RepoElement;
import com.sefford.kor.repositories.interfaces.Repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Basic DiskLRU-based disk repo. Part of this code is copied from
 * https://github.com/fhucho/simple-disk-cache/blob/master/SimpleDiskCache.java
 * in order to handle the DiskLRU interface.
 * <p/>
 * The rest is based on my own code at {@link com.sefford.kor.repositories.MemoryRepository MemoryRepository}
 */
public class DiskRepository<K, V extends RepoElement<K>> extends BaseRepository<K, V> {

    /**
     * Default Cache Size
     */
    public static final int DEFAULT_MAX_CACHE_SIZE = 2048 * 1024;
    /**
     * Log Tag
     */
    protected static final String TAG = "DiskCache";
    /**
     * Current Disk Cache Version
     */
    protected static final int VERSION = 1;
    /**
     * Actual DiskLRUCache instance
     */
    protected DiskLruCache cache;

    /**
     * Creates a new instance of the Disk Repository
     *
     * @param nextLevel  Next level if available for this repository
     * @param descriptor File descriptor where to save the information
     */
    public DiskRepository(Repository<K, V> nextLevel, File descriptor) {
        super(nextLevel);
        try {
            this.cache = DiskLruCache.open(descriptor, VERSION, 1, DEFAULT_MAX_CACHE_SIZE);
        } catch (IOException e) {
            this.cache = null;
        }
    }

    @Override
    public void clear() {
        clearCacheDirectory();
        if (hasNextLevel()) {
            nextLevel.clear();
        }
    }

    private void clearCacheDirectory() {
        try {
            deleteContents(cache.getDirectory());
        } catch (IOException e) {
        }
    }

    private void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + dir);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }

    @Override
    public boolean contains(K id) {
        boolean result;
        try {
            result = cache.get(id.toString()) != null;
        } catch (IOException e) {
            result = false;
        }
        return result ? result : hasNextLevel() ? nextLevel.contains(id) : result;
    }

    @Override
    public void delete(K id, V element) {
        try {
            cache.remove(id.toString());
        } catch (IOException e) {
        }
        if (hasNextLevel()) {
            nextLevel.delete(id, element);
        }
    }

    @Override
    public void deleteAll(List<V> elements) {
        for (final V element : elements) {
            delete(element.getId(), element);
        }
    }

    @Override
    public V get(K id) {
        V result = readToObject(id.toString());
        return result == null ? hasNextLevel() ? nextLevel.get(id) : null : result;
    }

    @Override
    public Collection<V> getAll() {
        return new ArrayList<V>();
    }

    @Override
    public V save(V element) {
        writeFromObject(element.getId().toString(), element);
        return element;
    }

    @Override
    public Collection<V> saveAll(Collection<V> elements) {
        for (final V element : elements) {
            save(element);
        }
        return elements;
    }

    /**
     * Reads the disk element to the Object
     *
     * @param id ID of the element
     * @return V element or null if there was an error
     */
    protected V readToObject(String id) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = cache.get(id);

            if (snapshot == null) {
                return null;
            }
            final InputStream in = snapshot.getInputStream(0);
            if (in != null) {
                final ObjectInputStream buffIn = new ObjectInputStream(in);
                V result = (V) buffIn.readObject();
                return result;
            }
        } catch (InvalidClassException e) {
            clearCacheDirectory();
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
        return null;
    }

    /**
     * Writes an object to a Disk Element
     *
     * @param id      ID of the element
     * @param element Element to write
     */
    protected void writeFromObject(String id, V element) {
        DiskLruCache.Editor editor = null;
        try {
            if (cache == null) {
                return;
            }
            editor = cache.edit(id);

            if (editor == null) {
                return;
            }

            if (writeObjectToFile(editor, element)) {
                cache.flush();
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Writes an object to a file
     *
     * @param editor DiskLRUCache editor instance
     * @param object Object to write
     * @return TRUE if everything went as expected
     * @throws IOException If a IO Error arises
     */
    protected boolean writeObjectToFile(DiskLruCache.Editor editor, V object)
            throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(editor.newOutputStream(0));
        out.writeObject(object);
        if (out != null) {
            out.close();
        }
        return true;
    }

    @Override
    public boolean isAvailable() {
        return cache != null && !cache.isClosed();
    }
}
