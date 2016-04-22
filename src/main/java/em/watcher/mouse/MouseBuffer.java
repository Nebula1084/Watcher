package em.watcher.mouse;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * we use this class to convert the little-endian buffer to big-endian buffer.
 */
abstract public class MouseBuffer {
    protected Queue<Byte> bytes;

    public MouseBuffer() {
        bytes = new ArrayDeque<>();
    }

    public void put(Byte b) {
        bytes.add(b);
    }

    abstract public void put(Integer i);

    abstract public void put(Float f);

    abstract public int getInt();

    abstract public float getFloat();

    abstract public char getChar();

}