package nz.co.guru.services.onlineorderdemo;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter {

    private List<Product> productList;

    private LayoutInflater inflater;

    private static final int MAX_PORTAIT_DESCRIPTION_LENGTH = 35;

    private static final int MAX_LANDSCAPE_DESCRIPTION_LENGTH = 70;

    // private boolean showCheckbox;

    private Activity parentActivity;

    public ProductAdapter(final Activity parentActivity, final List<Product> list, final LayoutInflater inflater) {
        this.parentActivity = parentActivity;
        productList = list;
        this.inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(final int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewItem item;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_row, null);
            // convertView.setLongClickable(true);

            item = new ViewItem();

            item.productImageView = (ImageView) convertView.findViewById(R.id.productItemImage);

            item.productTitleField = (TextView) convertView.findViewById(R.id.productItemName);

            item.productDescriptionField = (TextView) convertView.findViewById(R.id.productItemDescription);

            item.productCountsField = (TextView) convertView.findViewById(R.id.productCounts);

            // item.productCheckbox = (CheckBox) convertView.findViewById(R.id.CheckBoxSelected);

            convertView.setTag(item);
        }
        else {
            item = (ViewItem) convertView.getTag();
        }

        final Product currentProduct = productList.get(position);

        item.productImageView.setImageDrawable(currentProduct.getProductImage());
        item.productTitleField.setText(currentProduct.getName());

        String description = currentProduct.getReferenceNo() + ": " + currentProduct.getDescription();

        int maxDescriptionLength = MAX_PORTAIT_DESCRIPTION_LENGTH;

        final WindowManager wm = parentActivity.getWindowManager();
        final Display display = wm.getDefaultDisplay();
        if (display.getWidth() > display.getHeight()) {
            // ---landscape mode---
            maxDescriptionLength = MAX_LANDSCAPE_DESCRIPTION_LENGTH;
        }

        if (description.length() > maxDescriptionLength) {
            description = description.substring(0, maxDescriptionLength) + "...";
        }

        item.productDescriptionField.setText(description);

        if (currentProduct.getQuantity() > 0) {
            item.productCountsField.setText(String.valueOf(currentProduct.getQuantity()));
        }
        else {
            item.productCountsField.setText("");
        }

        // if (!showCheckbox) {
        // item.productCheckbox.setVisibility(View.GONE);
        // }
        // else {
        // if (currentProduct.isSelected() == true) {
        // item.productCheckbox.setChecked(true);
        // }
        // else {
        // item.productCheckbox.setChecked(false);
        // }
        // }

        return convertView;
    }

    public class ViewItem {

        ImageView productImageView;

        TextView productTitleField;

        TextView productDescriptionField;

        TextView productCountsField;

        CheckBox productCheckbox;
    }

}
