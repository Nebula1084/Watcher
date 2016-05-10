package em.watcher;

import em.watcher.user.User;

import javax.persistence.*;
import java.util.*;

@MappedSuperclass
public abstract class WatcherPacketDef {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> types;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> lengths;
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private User user;

    public final static String TYPE_INT = "int";
    public final static String TYPE_FLOAT = "float";
    public final static String TYPE_CHAR = "char";
    public final static String TYPE_STRING = "string";

    public WatcherPacketDef() {
        this.types = new LinkedHashMap<>();
        this.lengths = new LinkedHashMap<>();
    }

    public WatcherPacketDef(String name) {
        this();
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (!isType(type, length)) throw new Exception("type is invalid.");
        if ((1 > length) || (length > 1000)) throw new Exception("length is invalid.");
        if (types.containsKey(name)) throw new Exception("this field already exists");
        types.put(name, type);
        lengths.put(name, length);
    }

    public Set<String> getField() {
        return types.keySet();
    }

    public Boolean isType(String type, Integer length) {
        switch (type) {
            case TYPE_INT:
                return length == 4;
            case TYPE_FLOAT:
                return length == 4;
            case TYPE_CHAR:
                return length == 1;
            case TYPE_STRING:
                return length > 0;
            default:
                return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WatcherPacketDef)) return false;
        WatcherPacketDef packet = (WatcherPacketDef) obj;
        return Objects.equals(this.id, packet.id);
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(this.id);
    }
}