package net.mainsel.ipintobitwarden.objects;

public enum IPinCsvColumns {
    Group,
    EntryName,
    FieldName,
    FieldValue,
    FieldType,
    Note,
    isSecure,
    isLink;

    private static final IPinCsvColumns[] vals = values();

    public IPinCsvColumns next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
