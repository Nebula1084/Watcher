package em.watcher;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@MappedSuperclass
public abstract class WatcherPacket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ElementCollection
    private Map<String, String> types;
    @ElementCollection
    private Map<String, Integer> lengths;

    public final static String TYPE_INT = "int";
    public final static String TYPE_FLOAT = "float";
    public final static String TYPE_CHAR = "char";
    public final static String TYPE_STRING = "string";

    public WatcherPacket() {
        this.types = new HashMap<>();
        this.lengths = new HashMap<>();
    }

    public WatcherPacket(String name) {
        this.types = new HashMap<>();
        this.lengths = new HashMap<>();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getTypes() {
        return types;
    }

    public String getType(String name) {
        return types.get(name);
    }

    public Map<String, Integer> getLengths() {
        return lengths;
    }

    public int getLength(String name) {
        return lengths.get(name);
    }

    public void addField(String name, String type, Integer length) throws Exception {
        if (!isType(type)) throw new Exception("type is invalid.");
        if ((1 > length) || (length > 1000)) throw new Exception("length is invalid.");
        if (types.containsKey(name)) throw new Exception("this field already exists");
        types.put(name, type);
        lengths.put(name, length);
    }

    public Set<String> getField() {
        return types.keySet();
    }

    public Boolean isType(String type) {
        if (type.equals(TYPE_INT))
            return true;
        else if (type.equals(TYPE_FLOAT))
            return true;
        else if (type.equals(TYPE_CHAR))
            return true;
        else if (type.equals(TYPE_STRING))
            return true;
        else return false;
    }

}