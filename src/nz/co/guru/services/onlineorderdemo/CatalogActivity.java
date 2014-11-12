package nz.co.guru.services.onlineorderdemo;

import java.lang.reflect.Method;

import nz.co.guru.services.onlineorderdemo.model.ProductItem;
import nz.co.guru.services.onlineorderdemo.settings.SettingsActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class CatalogActivity extends Activity {

    private ExpandableListView catalogListView;

    private CatalogListAdapter catalogListAdapter;

    private Button viewOrderButton;

    private static final int PLACE_ORDER_REQ = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog);

        // action bar
        final ActionBar actionBar = getActionBar();
        // To make the application icon clickable, you need to call the setDisplayHomeAsUpEnabled()
        actionBar.setDisplayHomeAsUpEnabled(true);

        catalogListView = (ExpandableListView) findViewById(R.id.catalogListView);

        catalogListAdapter = new CatalogListAdapter(this, ProductOrderManager2.getCataloggroups());

        catalogListView.setAdapter(catalogListAdapter);

        for (int i = 0; i < catalogListAdapter.getGroupCount(); i++) {
            catalogListView.expandGroup(i);
        }

        // on child click listener
        catalogListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition, final int childPosition, final long id) {

                final ProductItem productItem = ProductOrderManager2.getProductByCode(id);

                final Intent productDetailsIntent = new Intent(getBaseContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ProductOrderManager2.SELECTED_PRODUCT_ITEM, productItem);
                startActivity(productDetailsIntent);

                return true;
            }
        });

        viewOrderButton = (Button) findViewById(R.id.viewOrderButton);
        viewOrderButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                viewOrders();
            }
        });

        notifyOrdersChange();

    }

    private void notifyOrdersChange() {
        viewOrderButton.setEnabled(ProductOrderManager2.hasOrders());
        catalogListAdapter.notifyDataSetChanged();

    }

    private static final int VIEW_ORDER_ACTION_ITEM_ID = 0;

    private static final int ORDER_HISTORY_ITEM_ID = 1;

    private static final int SETTINGS_ITEM_ID = 2;

    private static final int LOG_OUT_ITEM_ID = 3;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem viewOrderItem = menu.add(0, VIEW_ORDER_ACTION_ITEM_ID, VIEW_ORDER_ACTION_ITEM_ID, "View Order");
        {
            viewOrderItem.setIcon(R.drawable.view_order);
            viewOrderItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        final MenuItem orderHistoryMenuItem = menu.add(0, ORDER_HISTORY_ITEM_ID, ORDER_HISTORY_ITEM_ID, "Order History");
        {
            orderHistoryMenuItem.setIcon(R.drawable.order_history);
            orderHistoryMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        final MenuItem settingMenuItem = menu.add(0, SETTINGS_ITEM_ID, SETTINGS_ITEM_ID, "Settings");
        {
            settingMenuItem.setIcon(R.drawable.settings);
            settingMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        final MenuItem logoutItem = menu.add(0, LOG_OUT_ITEM_ID, LOG_OUT_ITEM_ID, "Logout");
        {
            logoutItem.setIcon(R.drawable.logout_middle);
            logoutItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case VIEW_ORDER_ACTION_ITEM_ID:
                viewOrders();
                return true;
            case ORDER_HISTORY_ITEM_ID:
                Toast.makeText(this, "Under construction", Toast.LENGTH_LONG).show();
                return true;
            case SETTINGS_ITEM_ID:
                // Toast.makeText(this, "Under construction", Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case LOG_OUT_ITEM_ID:
                final SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                final SharedPreferences.Editor editor = settings.edit();
                editor.remove("logged");
                editor.commit();
                finish();
                return true;
            default:
                return true;
        }
    }

    private void viewOrders() {

        if (ProductOrderManager2.hasOrders()) {
            final Intent intent = new Intent(getBaseContext(), OrderCartActivity.class);
            startActivityForResult(intent, PLACE_ORDER_REQ);
        }
        else {
            Toast.makeText(this, "Please select and add product to the order first.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == PLACE_ORDER_REQ) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), data.getData().toString(), Toast.LENGTH_SHORT).show();
                // orders have been submited, reset all products
                ProductOrderManager2.clearOrders();
                notifyOrdersChange();
            }

        }
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

    @Override
    public void onRestart() {
        super.onRestart();
        notifyOrdersChange();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ProductOrderManager2.clearOrders();
        notifyOrdersChange();

    }

}
