package nz.co.guru.services.onlineorderdemo;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailsActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        final List<Product> catalog = ProductOrderHelper.getCatalog(getResources());
        final List<Product> cart = ProductOrderHelper.getOrders();

        final int productIndex = getIntent().getExtras().getInt(ProductOrderHelper.SELECTED_PRODUCT);
        // final Product selectedProduct = (Product) getIntent().getExtras().getSerializable(ProductOrderHelper.SELECTED_PRODUCT);
        final Product selectedProduct = catalog.get(productIndex);

        // Set the proper image and text
        final ImageView productImageView = (ImageView) findViewById(R.id.productDetailImageView);
        productImageView.setImageDrawable(selectedProduct.getProductImage());

        final TextView productNoTextView = (TextView) findViewById(R.id.productDetailNoField);
        productNoTextView.setText(selectedProduct.getReferenceNo());

        final TextView productTitleTextView = (TextView) findViewById(R.id.productDetailNameField);
        productTitleTextView.setText(selectedProduct.getName());

        final TextView productDetailsDescriptionView = (TextView) findViewById(R.id.productDetailDescriptionField);
        productDetailsDescriptionView.setText(selectedProduct.getDescription());

        final TextView productDetailsPackingTextView = (TextView) findViewById(R.id.productDetailPackagingField);
        productDetailsPackingTextView.setText(selectedProduct.getPackaging());

        final EditText quantityTextView = (EditText) findViewById(R.id.productDetailQuantityField);
        setQuantityInField(selectedProduct.getQuantity());
        quantityTextView.setFocusableInTouchMode(true);

        final Button addToCartButton = (Button) findViewById(R.id.buttonAddOrder);
        addToCartButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                if (getInputQuantity() <= 0) {
                    showDialog(0);
                }
                else {
                    addOrder(selectedProduct);
                }
            }
        });

        // check if selected product has quantity, if there is quantity, that means it is update
        if (selectedProduct.getQuantity() > 0) {
            addToCartButton.setText("Update Order");
        }
        else {
            addToCartButton.setText("Add Order");
        }

    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        return new AlertDialog.Builder(this).setIcon(R.drawable.dialog_alert).setTitle("Zero quantity")
                .setMessage("The product quantity is 0, this product won't be added to the orders, click OK to continue or Cancel to change the quantity")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        finish();
                    }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        // Toast.makeText(getBaseContext(), "Cancel clicked!", Toast.LENGTH_SHORT).show();
                    }
                }).create();

    }

    private void addOrder(final Product product) {
        product.setQuantity(getInputQuantity());
        ProductOrderHelper.addOrder(product);
        finish();
    }

    private int getInputQuantity() {
        final EditText quantityTextView = (EditText) findViewById(R.id.productDetailQuantityField);
        if (quantityTextView.getText() != null && !quantityTextView.getText().toString().equals("")) {
            return Integer.valueOf(quantityTextView.getText().toString());
        }
        else {
            return 0;
        }
    }

    private void setQuantityInField(final int quantity) {
        final EditText quantityTextView = (EditText) findViewById(R.id.productDetailQuantityField);
        if (quantity > 0) {
            quantityTextView.setText(String.valueOf(quantity));
        }

    }
}