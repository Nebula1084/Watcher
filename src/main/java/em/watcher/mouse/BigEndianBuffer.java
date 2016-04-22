package em.watcher.mouse;

/**
 * this buffer will convert big-endian to little-endian.
 */
public class BigEndianBuffer extends MouseBuffer{
    @Override
    public void put(Integer i) {

    }

    @Override
    public void put(Float f) {

    }

    @Override
    public int getInt() {
        return 0;
    }

    @Override
    public float getFloat() {
        return 0;
    }

    @Override
    public char getChar() {
        return 0;
    }
}
