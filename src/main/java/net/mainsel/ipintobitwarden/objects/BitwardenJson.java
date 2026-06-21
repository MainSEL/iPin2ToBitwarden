package net.mainsel.ipintobitwarden.objects;

import java.util.List;

public class BitwardenJson {
    private final List<BitwardenEntry> items;

    public BitwardenJson(List<BitwardenEntry> items) {
        this.items = items;
    }

    public List<BitwardenEntry> getItems() {
        return items;
    }
}
