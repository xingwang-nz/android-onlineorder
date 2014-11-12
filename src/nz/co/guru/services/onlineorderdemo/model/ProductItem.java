package nz.co.guru.services.onlineorderdemo.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductItem implements Serializable {

    private long code;

    private String description;

    private BigDecimal unitPrice;

    private ProductUnit unit;

    private String note;

    private int quantity;

    public ProductItem() {
    }

    public ProductItem(final long code, final String description, final ProductUnit unit, final BigDecimal unitPrice) {
        this(code, description, unit, unitPrice, "");
    }

    public ProductItem(final long code, final String description, final ProductUnit unit, final BigDecimal unitPrice, final String note) {
        this.code = code;
        this.description = description;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.note = note;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public void setUnit(final ProductUnit unit) {
        this.unit = unit;
    }

    public long getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (code ^ code >>> 32);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ProductItem)) {
            return false;
        }
        final ProductItem other = (ProductItem) obj;
        if (code != other.code) {
            return false;
        }
        return true;
    }

    public void setCode(final long code) {
        this.code = code;
    }

    public String getNote() {
        return note == null ? "" : note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public String printPrice() {
        return "$" + unitPrice.toString() + "/" + unit.toString();
    }

}
