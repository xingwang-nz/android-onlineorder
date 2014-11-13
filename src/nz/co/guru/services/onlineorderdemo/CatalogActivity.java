package nz.co.guru.services.onlineorderdemo;

import java.lang.reflect.Method;

import nz.co.guru.services.onlineorderdemo.model.Language;
import nz.co.guru.services.onlineorderdemo.model.ProductItem;
import nz.co.guru.services.onlineorderdemo.settings.SettingsActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class CatalogActivity extends Activity {

    private ExpandableListView catalogListView;

    private CatalogListAdapter catalogListAdapter;

    private Button viewOrderButton;

    private static final int PLACE_ORDER_REQ = 0;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog);

        // action bar
        final ActionBar actionBar = getActionBar();
        // To make the application icon clickable, you need to call the setDisplayHomeAsUpEnabled()
        actionBar.setDisplayHomeAsUpEnabled(true);

        catalogListView = (ExpandableListView) findViewById(R.id.catalogListView);

        // banner
        final LayoutInflater inflater = getLayoutInflater();
        final View header = inflater.inflate(R.layout.banner, (ViewGroup) findViewById(R.id.bannerHeader));
        catalogListView.addHeaderView(header);
        // marquee text scrooling
        final TextView bannerText = (TextView) findViewById(R.id.bannerText);
        bannerText.setSelected(true);
        bannerText.setText("Atria Spare Rib $6.5/KG, Hkscan Pork R/O bellies $6.5/KG, F/M U/10 Squid Tube $3.9/KG");

        catalogListAdapter = new CatalogListAdapter(this, ProductOrderManager.getCataloggroups());
        catalogListView.setAdapter(catalogListAdapter);

        // expand each group
        for (int i = 0; i < catalogListAdapter.getGroupCount(); i++) {
            catalogListView.expandGroup(i);
        }

        catalogListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(final ExpandableListView parent, final View convertView, final int groupPosition, final long id) {
                return false;
            }
        });

        // on child click listener
        catalogListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition, final int childPosition, final long id) {

                final ProductItem productItem = ProductOrderManager.getProductByCode(id);

                final Intent productDetailsIntent = new Intent(getBaseContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ProductOrderManager.SELECTED_PRODUCT_ITEM, productItem);
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

        // setup language
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        handleLanguageChange(getLanguageFromPreferenceSettings(sharedPreferences));

        preferenceChangeListener = new OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
                if (SettingsActivity.KEY_PREF_LANGUAGE.equals(key)) {
                    handleLanguageChange(getLanguageFromPreferenceSettings(sharedPreferences));
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        notifyOrdersChange();

    }

    private Language getLanguageFromPreferenceSettings(final SharedPreferences sharedPref) {
        return Language.valueOf(sharedPref.getString(SettingsActivity.KEY_PREF_LANGUAGE, Language.ENG.toString()));
    }

    private void handleLanguageChange(final Language language) {
        ProductOrderManager.setLanguage(language);
        catalogListAdapter.notifyDataSetChanged();
    }

    private void notifyOrdersChange() {
        viewOrderButton.setEnabled(ProductOrderManager.hasOrders());
        catalogListAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        notifyOrdersChange();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ProductOrderManager.clearOrders();
        notifyOrdersChange();

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);

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

        if (ProductOrderManager.hasOrders()) {
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
                ProductOrderManager.clearOrders();
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

}
