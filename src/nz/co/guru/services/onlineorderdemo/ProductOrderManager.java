package nz.co.guru.services.onlineorderdemo;

import static nz.co.guru.services.onlineorderdemo.model.Language.CHN;
import static nz.co.guru.services.onlineorderdemo.model.Language.ENG;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.co.guru.services.onlineorderdemo.model.CatalogGroup;
import nz.co.guru.services.onlineorderdemo.model.Language;
import nz.co.guru.services.onlineorderdemo.model.ProductItem;
import nz.co.guru.services.onlineorderdemo.model.ProductUnit;

/**
 * Temporary class to store the hard-code catalogs.
 */
public class ProductOrderManager {

    public static final String SELECTED_PRODUCT_ITEM = "SELECTED_PRODUCT_ITEM";

    private static final List<CatalogGroup> catalogGroups = new ArrayList<CatalogGroup>();

    private static final List<ProductItem> orderCart = new ArrayList<ProductItem>();

    static {
        CatalogGroup group = new CatalogGroup("Butter/Can Frunit", R.drawable.prod_butter);
        catalogGroups.add(group);

        Map<Language, String> nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Cream Soda");
        group.addProduct(new ProductItem(19007, nameMap, ProductUnit.CASE, new BigDecimal("16.99"), "375mlx24cans"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Golden Pash");
        group.addProduct(new ProductItem(19017, nameMap, ProductUnit.CASE, new BigDecimal("17.99"), "375mlx24cans"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Tropical Punch");
        group.addProduct(new ProductItem(19037, nameMap, ProductUnit.CASE, new BigDecimal("26.99"), "375mlx24cans"));

        group = new CatalogGroup("Beef", R.drawable.prod_cow);
        catalogGroups.add(group);

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Angel Bay Angus beef patties");
        group.addProduct(new ProductItem(29155, nameMap, ProductUnit.CTN, new BigDecimal("93.19"), "150mg 3x18pcs"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Angel Bay Beef patties");
        group.addProduct(new ProductItem(29365, nameMap, ProductUnit.CTN, new BigDecimal("74.45"), "120mg 3x20pcs"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "ACanterbury SPB B/L beef");
        nameMap.put(CHN, "100%无肥牛肉");
        Map<Language, String> noteMap = new HashMap<Language, String>();
        noteMap.put(CHN, "适合炒餐");
        group.addProduct(new ProductItem(29464, nameMap, ProductUnit.KG, new BigDecimal("6.95"), " FZN 27.2kg", noteMap));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Angel Bay Beef Meatball");
        group.addProduct(new ProductItem(29535, nameMap, ProductUnit.CTN, new BigDecimal("53.60"), "5x67pcs"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "EB Navel End Fresh");
        nameMap.put(CHN, "新鲜肥牛腩");
        group.addProduct(new ProductItem(29624, nameMap, ProductUnit.KG, new BigDecimal("6.50"), ""));

        group = new CatalogGroup("Pork", R.drawable.prod_pig);
        catalogGroups.add(group);

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "HKscan Collar Butt");
        nameMap.put(CHN, "欧洲冻梅头");
        noteMap = new HashMap<Language, String>();
        noteMap.put(CHN, "新货到");
        group.addProduct(new ProductItem(33224, nameMap, ProductUnit.KG, new BigDecimal("7.35"), "FZN V.W", noteMap));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Be Campbell Pk shoulder");
        nameMap.put(CHN, "冻猪膊肉");
        group.addProduct(new ProductItem(33325, nameMap, ProductUnit.KG, new BigDecimal("5.95"), "90cl 27.20kg"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Atria Spare Rib");
        nameMap.put(CHN, "冻排骨");
        noteMap = new HashMap<Language, String>();
        noteMap.put(ENG, "Low Price");
        noteMap.put(CHN, "超低价");
        group.addProduct(new ProductItem(33628, nameMap, ProductUnit.KG, new BigDecimal("4.60"), "FZN 13.60kg", noteMap));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Hkscan Pork R/O bellies");
        nameMap.put(CHN, "冻有皮猪腩");
        noteMap = new HashMap<Language, String>();
        noteMap.put(ENG, "Lowest Price");
        noteMap.put(CHN, "市场最低价");
        group.addProduct(new ProductItem(33725, nameMap, ProductUnit.KG, new BigDecimal("6.50"), "FZN V.W", noteMap));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Hkscan Pork R/L mix bellies");
        nameMap.put(CHN, "冻无皮猪腩");
        noteMap = new HashMap<Language, String>();
        noteMap.put(CHN, "回锅肉首选");
        group.addProduct(new ProductItem(33775, nameMap, ProductUnit.KG, new BigDecimal("4.95"), "FZN V.W", noteMap));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "HK S/L B/L Pork Leg");
        nameMap.put(CHN, "冻猪后腿肉");
        group.addProduct(new ProductItem(34435, nameMap, ProductUnit.KG, new BigDecimal("5.95"), "FZN V.W"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Rivalea Pork Trim");
        nameMap.put(CHN, "冻80cl猪小件");
        noteMap = new HashMap<Language, String>();
        noteMap.put(ENG, "Suitable for sweat sauce pork");
        noteMap.put(CHN, "最适合咕噜肉");
        group.addProduct(new ProductItem(34464, nameMap, ProductUnit.KG, new BigDecimal("4.95"), "80cl 27.20kg FZN", noteMap));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Hkscan Sweden Pork Mini Riblet");
        nameMap.put(CHN, "冻小排骨");
        group.addProduct(new ProductItem(34625, nameMap, ProductUnit.KG, new BigDecimal("3.65"), "FZN 10kg"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Rivalea Breast Bone");
        nameMap.put(CHN, "冻猪胸骨");
        group.addProduct(new ProductItem(34707, nameMap, ProductUnit.KG, new BigDecimal("1.50"), "FZN V.W"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "F/P NZ pork Loin");
        nameMap.put(CHN, "冻无皮猪里脊");
        group.addProduct(new ProductItem(34844, nameMap, ProductUnit.KG, new BigDecimal("6.50"), "FZN V.W"));

        group = new CatalogGroup("Bacon", R.drawable.prod_egg);
        catalogGroups.add(group);

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "THE FRESH Streaky Bacon");
        nameMap.put(CHN, "新鲜streaky培根");
        group.addProduct(new ProductItem(45812, nameMap, ProductUnit.KG, new BigDecimal("10.50"), "FSH 1kg"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "THE FRESH Middle Bacon");
        nameMap.put(CHN, "新鲜Middle培根 ");
        group.addProduct(new ProductItem(45813, nameMap, ProductUnit.KG, new BigDecimal("11.50"), "FSH 1kg"));

        group = new CatalogGroup("Lamb", R.drawable.prod_sheep);
        catalogGroups.add(group);

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Angel Bay lamb Patties");
        group.addProduct(new ProductItem(51513, nameMap, ProductUnit.CTN, new BigDecimal("81.50"), "120mg 3x20pcs"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "EB Lamb Neck Bone");
        nameMap.put(CHN, "羊蝎子");
        group.addProduct(new ProductItem(52211, nameMap, ProductUnit.KG, new BigDecimal("2.95"), ""));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Ovation Lamb Trim 80CL");
        nameMap.put(CHN, "80cl羊碎肉");
        noteMap = new HashMap<Language, String>();
        noteMap.put(CHN, "羊肉串首选");
        group.addProduct(new ProductItem(56001, nameMap, ProductUnit.KG, new BigDecimal("5.75"), "FZN 27.20kg", noteMap));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "PJ Aus Lamb Flap");
        nameMap.put(CHN, "澳洲帶骨羊腩 ");
        group.addProduct(new ProductItem(56002, nameMap, ProductUnit.KG, new BigDecimal("3.35"), "FZN V.W"));

        group = new CatalogGroup("Sea Food", R.drawable.prod_tuna);
        catalogGroups.add(group);

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Talley Crumbed Hoki Fillet");
        nameMap.put(CHN, "炸鱼块");
        noteMap = new HashMap<Language, String>();
        noteMap.put(ENG, "For buffet");
        noteMap.put(CHN, "自助餐首选");
        group.addProduct(new ProductItem(66156, nameMap, ProductUnit.CTN, new BigDecimal("14.95"), "FZN 5X750mg", noteMap));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "F/M W/T Raw Prawn Meat 71/90");
        nameMap.put(CHN, "71/90 生虾肉");
        noteMap = new HashMap<Language, String>();
        noteMap.put(ENG, "Available on 14th August");
        noteMap.put(CHN, "将在8月14日到港");
        group.addProduct(new ProductItem(66161, nameMap, ProductUnit.PK, new BigDecimal("11.95"), "10X900mg", noteMap));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "S/C All Purpose Shrimp");
        nameMap.put(CHN, "多用途熟虾仁 ");
        group.addProduct(new ProductItem(66170, nameMap, ProductUnit.BAG, new BigDecimal("8.95"), "10X900mg"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "S/M Roeless scallop 60/80");
        nameMap.put(CHN, "无边帶子");
        group.addProduct(new ProductItem(66174, nameMap, ProductUnit.KG, new BigDecimal("25.00"), "1kg 60/80"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "Sealord Dressed Snapper Fish");
        nameMap.put(CHN, "无头无尾大立鱼");
        group.addProduct(new ProductItem(66192, nameMap, ProductUnit.KG, new BigDecimal("4.50"), "FZN V.W"));

        nameMap = new HashMap<Language, String>();
        nameMap.put(ENG, "SF/M U/10 Squid Tube");
        nameMap.put(CHN, "U/10鱿鱼筒");
        noteMap = new HashMap<Language, String>();
        noteMap.put(ENG, "Lowesr price");
        noteMap.put(CHN, "市场最低价");
        group.addProduct(new ProductItem(66220, nameMap, ProductUnit.KG, new BigDecimal("3.95"), "5kg Packed", noteMap));

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

        productItem.setQuantity(selectedProduct.getQuantity());
        if (!orderCart.contains(productItem)) {
            if (selectedProduct.getQuantity() > 0) {
                orderCart.add(productItem);
            }
        }
        else {
            if (selectedProduct.getQuantity() <= 0) {
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

    public static void setLanguage(final Language language) {
        for (final CatalogGroup catalogGroup : catalogGroups) {
            for (final ProductItem productItem : catalogGroup.getProducts()) {
                productItem.setLanguage(language);
            }
        }
    }
}
