package lou.jeran.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class IO {

    private static final int BUFFER_SIZE = 1024 * 8;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static String readFromClasspath(String path) throws IOException {
        try (InputStream is = IO.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("No resource found at " + path);
            }
            try (Reader in = new InputStreamReader(is, DEFAULT_CHARSET)) {
                StringBuilder out = new StringBuilder();
                copy(in, out);
                return out.toString();
            }
        }
    }

    /**
     * Copies from a readable to a writable using default buffer size
     */
    public static void copy(Readable in, Appendable out) throws IOException {
        copy(in, out, BUFFER_SIZE);
    }

    /**
     * Copies from a readable to a writable
     */
    public static void copy(Readable in, Appendable out, int bufferSize) throws IOException {
        CharBuffer buffer = CharBuffer.allocate(bufferSize);
        while (in.read(buffer) > 0) {
            buffer.flip();
            out.append(buffer.toString());
            buffer.clear();
        }
    }

}
