package nz.co.guru.services.onlineorderdemo;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

public class ProductOrderHelper {

    public static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";

    private static List<Product> catalog;

    private static List<Product> orders = new ArrayList<Product>();

    public static List<Product> getCatalog(final Resources res) {

        if (catalog == null) {
            catalog = new ArrayList<Product>();

            catalog.add(new Product("312PJ13H73", "Chicken", "Frozen meat, from chicken animal, barn, free ranges", "Cardboard box", res
                    .getDrawable(R.drawable.prod_chicken)));
            catalog.add(new Product("312FG159J7", "Beef", "Frozen meat, from cattle", "Cardboard box", res.getDrawable(R.drawable.prod_cow)));
            catalog.add(new Product("312XC47D15", "Pork", "Frozen meat, from pig", "Cardboard box", res.getDrawable(R.drawable.prod_pig)));

            catalog.add(new Product("193DF12L47", "Carrots", "Root vegetable", "Cardboard box", res.getDrawable(R.drawable.prod_carrot)));
            catalog.add(new Product("193HJ38O82", "Potatoes", "Starchy root", "Cardboard box", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("293KJ293OJ", "Chicken eggs", "Poultry, from chickens", "Cardboard cartons", res.getDrawable(R.drawable.ic_launcher)));

            catalog.add(new Product("193NI392L1", "Bread", "Staple baked food", "Plastic cartons", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("592HI182F8", "Lettuce", "Leaf vegetable", "Plastic container", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("184LOL482S", "Wheat flour", "Powder made from grounded wheat", "Plastic bin", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("492GH29I82", "Sugar", "Sweet powder", "Plastic bin", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("495FHJ371S", "Chocolate", "Sweet brown food made from cacao seeds", "Cardboard box", res
                    .getDrawable(R.drawable.ic_launcher)));

            catalog.add(new Product("382FH48J38", "Lemons", "Citrus fruit", "Cardboard box", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("482YO281G7", "Tuna", "Seafood, fish, meat", "Cardboard box", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("172SK39J9", "Mushrooms", "Edible fleshy fungus", "Plastic container", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("392DO39XZ6", "Apples", "Queen and Pacific rose", "Cardboard box", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("482LOTR46", "Butter", "Dairy product made by churning milk", "Plastic bag", res.getDrawable(R.drawable.ic_launcher)));
            catalog.add(new Product("482LOTR46", "Radishes", "Edible root vegetable", "Cardboard box", res.getDrawable(R.drawable.ic_launcher)));

        }

        return catalog;
    }

    public static List<Product> getOrders() {
        return orders;
    }

    public boolean hasOrder() {
        return orders.size() > 0;
    }

    public static void addOrder(final Product product) {
        if (!orders.contains(product)) {
            if (product.getQuantity() > 0) {
                orders.add(product);
            }
        }
        else {
            final Product existingOrder = getOrder(product.getReferenceNo());

            if (product.getQuantity() > 0) {
                existingOrder.setQuantity(product.getQuantity());
            }
            else {
                // the quantity has been reset to 0, remove this product from orders
                orders.remove(existingOrder);
            }
        }
    }

    private static Product getOrder(final String referenceNo) {
        for (final Product product : orders) {
            if (product.getReferenceNo().equalsIgnoreCase(referenceNo)) {
                return product;
            }
        }
        return null;
    }

    public static void clearOrders() {
        for (final Product product : orders) {
            product.setQuantity(0);
            // product.setSelected(false);
        }
        orders.clear();

    }

    public static boolean hasOrders() {
        return !orders.isEmpty();
    }

    public static int getNumberOfOrder() {
        return orders.size();
    }

    public static void deleteOrder(final Product product) {
        if (orders.contains(product)) {
            orders.remove(product);
            product.setQuantity(0);
            // product.setSelected(false);
        }
    }

    public static int getProductIndex(final Product product) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).equals(product)) {
                return i;
            }
        }
        throw new RuntimeException(String.format("The product %s is not in the catalog", product.getProductNo()));
    }

}