package net.mainsel.ipintobitwarden.objects;

import java.util.List;

public class BitwardenEntryLogin {
    private final List<BitwardenEntryLoginUri> uris;
    private final String username;
    private final String password;
    private String totp;

    public BitwardenEntryLogin(List<String> uris, String username, String password) {
        this.uris = uris.stream().map(BitwardenEntryLoginUri::new).toList();
        this.username = username;
        this.password = password;
    }

    public List<BitwardenEntryLoginUri> getUris() {
        return uris;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTotp() {
        return totp;
    }

    public void setTotp(String totp) {
        this.totp = totp;
    }
}
