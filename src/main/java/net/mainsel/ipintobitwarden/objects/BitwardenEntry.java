package net.mainsel.ipintobitwarden.objects;

import java.util.ArrayList;
import java.util.List;

public class BitwardenEntry {
    private final int type;
    private final String name;
    private final String notes;
    private final List<BitwardenField> fields;
    private BitwardenEntryLogin login;

    public BitwardenEntry(int type, String name, String notes) {
        this.type = type;
        this.name = name;
        this.notes = notes;
        this.fields = new ArrayList<>();
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public List<BitwardenField> getFields() {
        return fields;
    }

    public BitwardenEntryLogin getLogin() {
        return login;
    }

    public void setLogin(BitwardenEntryLogin login) {
        this.login = login;
    }

    public void addField(BitwardenField bitwardenField) {
        this.fields.add(bitwardenField);
    }
}
