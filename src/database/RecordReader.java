package database;

import java.io.*;

/**
 *
 * @author vassilis
 */
public class RecordReader {

    private String key;
    private byte[] data;
    private ByteArrayInputStream in;
    private ObjectInputStream objIn;

    public RecordReader(String key, byte[] data) {
        this.key = key;
        this.data = data;
        in = new ByteArrayInputStream(data);
    }

    public String getKey() {
        return key;
    }

    public byte[] getData() {
        return data;
    }

    public InputStream getInputStream() throws IOException {
        return in;
    }

    public ObjectInputStream getObjectInputStream() throws IOException {
        if (objIn == null) {
            objIn = new ObjectInputStream(in);
        }
        return objIn;
    }

    /**
     * Reads the next object in the record using an ObjectInputStream.
     *
     * @return
     * @throws java.io.IOException
     * @throws java.io.OptionalDataException
     * @throws java.lang.ClassNotFoundException
     */
    public Object readObject() throws IOException, OptionalDataException, ClassNotFoundException {
        return getObjectInputStream().readObject();
    }
}
