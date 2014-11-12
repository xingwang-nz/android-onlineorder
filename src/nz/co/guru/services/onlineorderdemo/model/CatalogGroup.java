package nz.co.guru.services.onlineorderdemo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CatalogGroup {

    private final String name;

    private final int imageRsourceId;

    private final List<ProductItem> products = new ArrayList<ProductItem>();

    public CatalogGroup(final String name, final int imageRsourceId) {
        this.name = name;
        this.imageRsourceId = imageRsourceId;
    }

    public String getName() {
        return name;
    }

    public void addProduct(final ProductItem item) {
        this.products.add(item);
    }

    public List<ProductItem> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public int getImageRsourceId() {
        return imageRsourceId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
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
        if (!(obj instanceof CatalogGroup)) {
            return false;
        }
        final CatalogGroup other = (CatalogGroup) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
