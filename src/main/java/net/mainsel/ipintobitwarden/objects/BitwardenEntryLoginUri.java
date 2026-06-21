package net.mainsel.ipintobitwarden.objects;

public class BitwardenEntryLoginUri {
    private final String match = null;
    private final String uri;

    public BitwardenEntryLoginUri(String uri) {
        this.uri = uri;
    }

    public String getMatch() {
        return match;
    }

    public String getUri() {
        return uri;
    }
}
