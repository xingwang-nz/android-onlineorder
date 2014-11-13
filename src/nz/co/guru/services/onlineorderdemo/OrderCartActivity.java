package nz.co.guru.services.onlineorderdemo;

import java.lang.reflect.Method;
import java.util.List;

import nz.co.guru.services.onlineorderdemo.model.ProductItem;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderCartActivity extends Activity {

    private OrderCartListAdapter listAdapter;

    private Button sendOrderButton;

    private ListView orderCartListView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_cart);

        // action bar
        final ActionBar actionBar = getActionBar();
        // To make the application icon clickable, you need to call the setDisplayHomeAsUpEnabled()
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create the list
        orderCartListView = (ListView) findViewById(R.id.orderCartListView);

        // productAdapter = new ProductAdapter(getApplicationContext(), ProductOrderHelper.getOrders(), getLayoutInflater(), true);
        listAdapter = new OrderCartListAdapter(this, ProductOrderManager.getOrderCart(), getLayoutInflater());

        orderCartListView.setAdapter(listAdapter);
        orderCartListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            }
        });

        // register ListView for context menu in ListActivity class
        registerForContextMenu(orderCartListView);

        sendOrderButton = (Button) findViewById(R.id.sendOrderButton);
        // simulate calling send orders external services
        sendOrderButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                placeOrders();
            }

        });

        setSendOrderButtonStatus();

    }

    private void placeOrders() {
        if (!ProductOrderManager.hasOrders()) {
            Toast.makeText(this, "No products are selected in this order.", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog dialog = ProgressDialog.show(OrderCartActivity.this, "Sending Order", "Please wait...", true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // ---simulate doing something lengthy---
                    Thread.sleep(3500);
                    // ---dismiss the dialog---
                    dialog.dismiss();

                    final Intent intent = new Intent();
                    intent.setData(Uri.parse(String.format("Order has been sent successfully.")));
                    setResult(RESULT_OK, intent);
                    finish();

                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static final int EDIT_ORDER_MENU_ITEM_ID = 0;

    private static final int DELETE_ORDER_MENU_ITEM_ID = 1;

    // long press listview context menu
    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenu.ContextMenuInfo menuInfo) {

        if (view.getId() == R.id.orderCartListView) {
            final ListView lv = (ListView) view;
            final AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
            final ProductItem productItem = (ProductItem) lv.getItemAtPosition(acmi.position);

            menu.setQwertyMode(true);
            menu.setHeaderTitle(productItem.getName());
            final MenuItem menuItem1 = menu.add(0, EDIT_ORDER_MENU_ITEM_ID, 0, "Edit");
            {
                // menuItem1.setAlphabeticShortcut('a');
                menuItem1.setIcon(R.drawable.edit_notes);
            }
            final MenuItem menuItem2 = menu.add(0, DELETE_ORDER_MENU_ITEM_ID, 1, "Delete");
            {
                // menuItem2.setAlphabeticShortcut('b');
                menuItem2.setIcon(R.drawable.delete_middle);
            }

        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final ProductItem productItem = (ProductItem) orderCartListView.getItemAtPosition(info.position);

        switch (item.getItemId()) {
            case EDIT_ORDER_MENU_ITEM_ID:

                final Intent productDetailsIntent = new Intent(getBaseContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ProductOrderManager.SELECTED_PRODUCT_ITEM, productItem);
                startActivity(productDetailsIntent);

                return true;

            case DELETE_ORDER_MENU_ITEM_ID:
                ProductOrderManager.deleteOrder(productItem);
                listAdapter.notifyDataSetChanged();
                setSendOrderButtonStatus();
            default:
                return super.onContextItemSelected(item);

        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        listAdapter.notifyDataSetChanged();
        setSendOrderButtonStatus();
    }

    /**
     * on Android 3.0+, the icons in the menu are not shown by design. This is a design decision by Google.<br/>
     * The following is workaround telling the overflow menu to display the icons directly.
     */
    @Override
    public boolean onMenuOpened(final int featureId, final Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    final Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch (final NoSuchMethodException e) {
                    Log.e("onMenuOpened", "onMenuOpened", e);
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private static final int SEND_ORDER_ACTION_ITEM_ID = 3;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem mnu1 = menu.add(0, SEND_ORDER_ACTION_ITEM_ID, 0, "Send Order");
        {
            mnu1.setIcon(R.drawable.place_order);
            mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case SEND_ORDER_ACTION_ITEM_ID:
                placeOrders();
                return true;
            default:
                return true;
        }
    }

    private void setSendOrderButtonStatus() {
        sendOrderButton.setEnabled(ProductOrderManager.hasOrders());
    }

    private class OrderCartListAdapter extends BaseAdapter {

        private final Activity parentActivity;

        private final List<ProductItem> orderCart;

        private final LayoutInflater inflater;

        public OrderCartListAdapter(final Activity parentActivity, final List<ProductItem> orderCart, final LayoutInflater inflater) {
            this.parentActivity = parentActivity;
            this.orderCart = orderCart;
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return orderCart == null ? 0 : orderCart.size();
        }

        @Override
        public Object getItem(final int position) {
            return orderCart.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_catalog_item, null);
            }

            final ProductItem productItem = (ProductItem) getItem(position);

            final TextView productDescriptionView = (TextView) convertView.findViewById(R.id.catalogItemName);
            productDescriptionView.setText(productItem.getName());

            final TextView catalogItemOtherInfo = (TextView) convertView.findViewById(R.id.catalogItemOtherInfo);
            catalogItemOtherInfo.setText(productItem.printFullDescription());

            final TextView catalogItemCounts = (TextView) convertView.findViewById(R.id.catalogItemCounts);

            if (productItem.getQuantity() > 0) {
                catalogItemCounts.setText(String.valueOf(productItem.getQuantity()));
            }
            else {
                catalogItemCounts.setText("");
            }

            return convertView;
        }
    }

}