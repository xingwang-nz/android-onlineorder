package nz.co.guru.services.onlineorderdemo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nz.co.guru.services.onlineorderdemo.model.CatalogGroup;
import nz.co.guru.services.onlineorderdemo.model.ProductItem;
import nz.co.guru.services.onlineorderdemo.model.ProductUnit;

/**
 * Temporary class to store the hard-code catalogs.
 */
public class ProductOrderManager2 {

    public static final String SELECTED_PRODUCT_ITEM = "SELECTED_PRODUCT_ITEM";

    private static final List<CatalogGroup> catalogGroups = new ArrayList<CatalogGroup>();

    private static final List<ProductItem> orderCart = new ArrayList<ProductItem>();

    static {
        CatalogGroup group = new CatalogGroup("Butter/Can Frunit", R.drawable.prod_butter);
        catalogGroups.add(group);
        group.addProduct(new ProductItem(19007, "Cream Soda 375mlx24cans", ProductUnit.CASE, new BigDecimal("16.99")));
        group.addProduct(new ProductItem(19017, "Golden Pash 375mlx24cans ", ProductUnit.CASE, new BigDecimal("17.99")));
        group.addProduct(new ProductItem(19037, "Tropical Punch 375mlx24cans", ProductUnit.CASE, new BigDecimal("26.99")));

        group = new CatalogGroup("Beef", R.drawable.prod_cow);
        catalogGroups.add(group);
        group.addProduct(new ProductItem(29155, "Angel Bay Angus beef patties 150mg 3x18pcs", ProductUnit.CTN, new BigDecimal("93.19")));
        group.addProduct(new ProductItem(29365, "Angel Bay Beef patties 120mg 3x20pcs", ProductUnit.CTN, new BigDecimal("74.45")));
        group.addProduct(new ProductItem(29464, "Canterbury SPB B/L beef FZN 27.2kg", ProductUnit.KG, new BigDecimal("6.95"), "适合炒餐"));
        group.addProduct(new ProductItem(29535, "Angel Bay Beef Meatball 5x67pcs", ProductUnit.CTN, new BigDecimal("53.60")));
        group.addProduct(new ProductItem(29624, "EB Navel End Fresh", ProductUnit.KG, new BigDecimal("6.50")));

        group = new CatalogGroup("Pork", R.drawable.prod_pig);
        catalogGroups.add(group);
        group.addProduct(new ProductItem(33224, "欧洲冻梅头", ProductUnit.KG, new BigDecimal("7.35"), "新货到"));
        group.addProduct(new ProductItem(33325, "冻猪膊肉", ProductUnit.KG, new BigDecimal("5.95")));
        group.addProduct(new ProductItem(33628, "冻排骨", ProductUnit.KG, new BigDecimal("4.60"), "超低价"));
        group.addProduct(new ProductItem(33725, "冻有皮猪腩", ProductUnit.KG, new BigDecimal("6.50"), "市场最低价"));
        group.addProduct(new ProductItem(33775, "冻无皮猪腩", ProductUnit.KG, new BigDecimal("4.95"), "回锅肉首选"));
        group.addProduct(new ProductItem(34435, "冻猪后腿肉", ProductUnit.KG, new BigDecimal("5.95")));
        group.addProduct(new ProductItem(34464, "冻80cl猪小件", ProductUnit.KG, new BigDecimal("4.95"), "最适合咕噜肉"));
        group.addProduct(new ProductItem(34625, "冻小排骨", ProductUnit.KG, new BigDecimal("3.65")));
        group.addProduct(new ProductItem(34707, "冻猪胸骨", ProductUnit.KG, new BigDecimal("1.50")));
        group.addProduct(new ProductItem(34844, "冻无皮猪里脊", ProductUnit.KG, new BigDecimal("6.50")));

        group = new CatalogGroup("Bacon", R.drawable.prod_egg);
        catalogGroups.add(group);
        group.addProduct(new ProductItem(45812, "THE FRESH Streaky Bacon FSH 1kg", ProductUnit.KG, new BigDecimal("10.50")));
        group.addProduct(new ProductItem(45813, "THE FRESH Middle Bacon FSH 1kg", ProductUnit.KG, new BigDecimal("11.50")));

        group = new CatalogGroup("Lamb", R.drawable.prod_sheep);
        catalogGroups.add(group);
        group.addProduct(new ProductItem(51513, "AAngel Bay lamb Patties 120mg 3x20pcs", ProductUnit.CTN, new BigDecimal("81.50")));
        group.addProduct(new ProductItem(52211, "EB Lamb Neck Bone ", ProductUnit.KG, new BigDecimal("2.95")));
        group.addProduct(new ProductItem(56001, "Ovation Lamb Trim 80CL FZN 27.20kg", ProductUnit.KG, new BigDecimal("5.75")));
        group.addProduct(new ProductItem(56002, "PJ Aus Lamb Flap FZN V.W ", ProductUnit.KG, new BigDecimal("3.35")));

        group = new CatalogGroup("Sea Food", R.drawable.prod_tuna);
        catalogGroups.add(group);
        group.addProduct(new ProductItem(66156, "Talley Crumbed Hoki Fillet FZN 5X750mg", ProductUnit.CTN, new BigDecimal("14.95")));
        group.addProduct(new ProductItem(66161, "F/M W/T Raw Prawn Meat 71/90 10X900mg", ProductUnit.PK, new BigDecimal("11.95"), "将在8月14日到港"));
        group.addProduct(new ProductItem(66170, "S/C All Purpose Shrimp 10X900mg", ProductUnit.BAG, new BigDecimal("8.95")));
        group.addProduct(new ProductItem(66174, "S/M Roeless scallop 60/80 1kg 60/80 ", ProductUnit.KG, new BigDecimal("25.00")));
        group.addProduct(new ProductItem(66192, "Sealord Dressed Snapper Fish FZN V.W", ProductUnit.KG, new BigDecimal("4.50")));
        group.addProduct(new ProductItem(66220, "F/M U/10 Squid Tube 5kg", ProductUnit.KG, new BigDecimal("3.95")));

    }

    public static List<CatalogGroup> getCataloggroups() {
        return Collections.unmodifiableList(catalogGroups);
    }

    public static ProductItem getProductByCode(final long code) {
        for (final CatalogGroup catalogGroup : catalogGroups) {
            for (final ProductItem productItem : catalogGroup.getProducts()) {
                if (productItem.getCode() == code) {
                    return productItem;
                }
            }
        }
        return null;
    }

    public static List<ProductItem> getOrderCart() {
        return Collections.unmodifiableList(orderCart);
    }

    public boolean hasOrder() {
        return orderCart.size() > 0;
    }

    public static void addOrder(final ProductItem selectedProduct) {
        final ProductItem productItem = getProductByCode(selectedProduct.getCode());
        if (productItem == null) {
            return;
        }

        if (!orderCart.contains(productItem)) {
            if (selectedProduct.getQuantity() > 0) {
                productItem.setQuantity(selectedProduct.getQuantity());
                orderCart.add(productItem);
            }
        }
        else {
            if (selectedProduct.getQuantity() > 0) {
                productItem.setQuantity(selectedProduct.getQuantity());
            }
            else {
                // the quantity has been reset to 0, remove this product from orders
                orderCart.remove(productItem);
            }
        }
    }

    public static void clearOrders() {
        for (final ProductItem product : orderCart) {
            product.setQuantity(0);
        }
        orderCart.clear();
    }

    public static boolean hasOrders() {
        return !orderCart.isEmpty();
    }

    public static int getNumberOfOrder() {
        return orderCart.size();
    }

    public static void deleteOrder(final ProductItem productItem) {
        if (orderCart.contains(productItem)) {
            orderCart.remove(productItem);
            productItem.setQuantity(0);
        }
    }
}
