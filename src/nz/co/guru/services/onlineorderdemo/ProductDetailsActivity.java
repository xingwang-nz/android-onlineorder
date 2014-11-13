package nz.co.guru.services.onlineorderdemo;

import nz.co.guru.services.onlineorderdemo.model.ProductItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProductDetailsActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        final ProductItem selectedProduct = (ProductItem) getIntent().getExtras().getSerializable(ProductOrderManager.SELECTED_PRODUCT_ITEM);

        final TextView productDescriptionTextView = (TextView) findViewById(R.id.productDetailDescriptionField);
        productDescriptionTextView.setText(selectedProduct.getName());

        final TextView productDetailCodeField = (TextView) findViewById(R.id.productDetailCodeField);
        productDetailCodeField.setText(String.valueOf(selectedProduct.getCode()));

        final TextView productDetailPriceField = (TextView) findViewById(R.id.productDetailPriceField);
        productDetailPriceField.setText(selectedProduct.printPrice());

        final TextView productDetailPackagingField = (TextView) findViewById(R.id.productDetailPackagingField);
        productDetailPackagingField.setText(selectedProduct.getPackaging());

        final TextView productDetailNoteField = (TextView) findViewById(R.id.productDetailNoteField);

        if (!"".equals(selectedProduct.getNote())) {
            productDetailNoteField.setVisibility(View.VISIBLE);
            productDetailNoteField.setText(selectedProduct.getNote());
        }
        else {
            productDetailNoteField.setVisibility(View.GONE);
            productDetailNoteField.setText("");
        }

        setQuantityInField(selectedProduct.getQuantity());

        final Button addToCartButton = (Button) findViewById(R.id.addOrderButton);
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

    private void addOrder(final ProductItem selectedProduct) {
        selectedProduct.setQuantity(getInputQuantity());
        ProductOrderManager.addOrder(selectedProduct);
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
