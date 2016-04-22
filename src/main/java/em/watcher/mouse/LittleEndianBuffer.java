package em.watcher.mouse;

/**
 * this buffer will convert little-endian to big-endian.
 */
public class LittleEndianBuffer extends MouseBuffer {
    @Override
    public void put(Integer i) {

    }

    @Override
    public void put(Float f) {

    }

    @Override
    public int getInt() {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            byte b = bytes.poll();
            ret += b << (i * 8);
        }
        return ret;
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
