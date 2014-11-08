package nz.co.guru.services.onlineorderdemo;

import java.lang.reflect.Method;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static final String PREFS_NAME = "LoginPrefs";

    private List<Product> mProductList;

    private ProductAdapter productAdapter;

    private static final int PLACE_ORDER_REQ = 0;

    private Button viewOrdersCartButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog);

        // action bar
        final ActionBar actionBar = getActionBar();
        // To make the application icon clickable, you need to call the setDisplayHomeAsUpEnabled()
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Obtain a reference to the product catalog
        mProductList = ProductOrderHelper.getCatalog(getResources());

        // Create the list
        final ListView listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);

        // productAdapter = new ProductAdapter(getApplicationContext(), mProductList, getLayoutInflater(), false);
        productAdapter = new ProductAdapter(this, mProductList, getLayoutInflater());
        listViewCatalog.setAdapter(productAdapter);

        listViewCatalog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Intent productDetailsIntent = new Intent(getBaseContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ProductOrderHelper.SELECTED_PRODUCT, position);
                startActivity(productDetailsIntent);
            }
        });

        viewOrdersCartButton = (Button) findViewById(R.id.buttonViewOrders);
        viewOrdersCartButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                // final Intent viewShoppingCartIntent = new Intent(getBaseContext(), OrderCartActivity.class);
                // startActivityForResult(viewShoppingCartIntent, PLACE_ORDER_REQ);
                viewOrders();
            }
        });

        notifyOrdersChange();

    }

    private void viewOrders() {
        if (ProductOrderHelper.hasOrders()) {
            final Intent viewShoppingCartIntent = new Intent(getBaseContext(), OrderCartActivity.class);
            startActivityForResult(viewShoppingCartIntent, PLACE_ORDER_REQ);
        }
        else {
            Toast.makeText(this, "Please select and add product to order first.", Toast.LENGTH_LONG).show();
        }
    }

    private static final int VIEW_ORDER_ACTION_ITEM_ID = 0;

    private static final int SETTINGS_ITEM_ID = 1;

    private static final int LOG_OUT_ITEM_ID = 2;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem mnu1 = menu.add(0, VIEW_ORDER_ACTION_ITEM_ID, VIEW_ORDER_ACTION_ITEM_ID, "View Order");
        {
            mnu1.setIcon(R.drawable.view_order);
            mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        final MenuItem mnu2 = menu.add(0, SETTINGS_ITEM_ID, SETTINGS_ITEM_ID, "Settings");
        {
            mnu2.setIcon(R.drawable.settings);
            mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        final MenuItem mnu3 = menu.add(0, LOG_OUT_ITEM_ID, LOG_OUT_ITEM_ID, "Logout");
        {
            mnu3.setIcon(R.drawable.logout_middle);
            mnu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
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
            case SETTINGS_ITEM_ID:
                Toast.makeText(this, "Settings under construction", Toast.LENGTH_LONG).show();
                return true;
            case LOG_OUT_ITEM_ID:
                final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                final SharedPreferences.Editor editor = settings.edit();
                editor.remove("logged");
                editor.commit();
                finish();
                return true;
            default:
                return true;
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

    private void notifyOrdersChange() {
        viewOrdersCartButton.setEnabled(ProductOrderHelper.hasOrders());
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == PLACE_ORDER_REQ) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), data.getData().toString(), Toast.LENGTH_SHORT).show();
                // orders have been submited, reset all products
                ProductOrderHelper.clearOrders();
                notifyOrdersChange();
            }

        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        notifyOrdersChange();
    }
}