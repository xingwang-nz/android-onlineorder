package nz.co.guru.services.onlineorderdemo;

import android.graphics.drawable.Drawable;

public class Product {

    private final String name;

    private final Drawable productImage;

    private final String description;

    private final String packaging;

    private final String productNo;

    // private boolean selected;

    private int quantity;

    public Product(final String productNo, final String name, final String description, final String packaging, final Drawable productImage) {
        this.productNo = productNo;
        this.name = name;
        this.description = description;
        this.packaging = packaging;
        this.productImage = productImage;

    }

    public String getName() {
        return name;
    }

    public String getReferenceNo() {
        return productNo;
    }

    public Drawable getProductImage() {
        return productImage;
    }

    public String getDescription() {
        return description;
    }

    // public boolean isSelected() {
    // return selected;
    // }
    //
    // public void setSelected(final boolean selected) {
    // this.selected = selected;
    // }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public String getPackaging() {
        return packaging;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (productNo == null ? 0 : productNo.hashCode());
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
        if (!(obj instanceof Product)) {
            return false;
        }
        final Product other = (Product) obj;
        if (productNo == null) {
            if (other.productNo != null) {
                return false;
            }
        }
        else if (!productNo.equals(other.productNo)) {
            return false;
        }
        return true;
    }

    public String getProductNo() {
        return productNo;
    }

}