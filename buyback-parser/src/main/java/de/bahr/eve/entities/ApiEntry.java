package de.bahr.eve.entities;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class ApiEntry {
    private final String id;
    private final String owner;
    private final String type;
    private final String keyId;
    private final String vCode;
    private final String corpId;

    public ApiEntry(String id, String owner, String type, String keyId, String vCode, String corpId) {
        this.id = id;
        this.owner = owner;
        this.type = type;
        this.keyId = keyId;
        this.vCode = vCode;
        this.corpId = corpId;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getvCode() {
        return vCode;
    }

    public String getCorpId() {
        return corpId;
    }
}
