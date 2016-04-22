package em.watcher.mouse;

public class MouseBufferFactory {
    static MouseBuffer produceBigEndianBuffer() {
        return new BigEndianBuffer();
    }

    static MouseBuffer produceLittlelEndianBuffer() {
        return new LittleEndianBuffer();
    }
}
