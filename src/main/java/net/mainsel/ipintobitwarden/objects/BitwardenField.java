package net.mainsel.ipintobitwarden.objects;

public class BitwardenField {
    private final String name;
    private final String value;
    private final int type;

    public BitwardenField(String name, String value, int type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getType() {
        return type;
    }
}
