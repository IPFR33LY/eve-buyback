package de.bahr.eve.entities;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class QuantifiedItem {
    private final String typeName;
    private final Long typeId;
    private Long quantity;

    public QuantifiedItem(String typeName, Long typeId, Long quantity) {
        this.typeName = typeName;
        this.typeId = typeId;
        this.quantity = quantity;
    }

    public String getTypeName() {
        return typeName;
    }

    public Long getTypeId() {
        return typeId;
    }
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
