package nz.co.guru.services.onlineorderdemo;

import java.lang.reflect.Method;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OrderCartActivity extends Activity {

    // private List<Product> ;

    private ProductAdapter productAdapter;

    private Button proceeddOrderButton;

    private ListView listViewCatalog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_cart);

        // orders = ProductOrderHelper.getOrders();

        // Make sure to clear the selections
        // for (int i = 0; i < orders.size(); i++) {
        // orders.get(i).setSelected(false);
        // }

        // action bar
        final ActionBar actionBar = getActionBar();
        // To make the application icon clickable, you need to call the setDisplayHomeAsUpEnabled()
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create the list
        listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);
        // productAdapter = new ProductAdapter(getApplicationContext(), ProductOrderHelper.getOrders(), getLayoutInflater(), true);
        productAdapter = new ProductAdapter(this, ProductOrderManager.getOrderCart(), getLayoutInflater());

        listViewCatalog.setAdapter(productAdapter);
        listViewCatalog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

                // final Product selectedProduct = orders.get(position);
                // if (selectedProduct.isSelected()) {
                // selectedProduct.setSelected(false);
                // }
                // else {
                // selectedProduct.setSelected(true);
                // }
                //
                // productAdapter.notifyDataSetInvalidated();

            }
        });

        // register ListView for context menu in ListActivity class
        registerForContextMenu(listViewCatalog);

        // final Button removeButton = (Button) findViewById(R.id.removeOrderButton);
        // removeButton.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(final View v) {
        // // Loop through and remove all the products that are selected
        // // Loop backwards so that the remove works correctly
        // for (int i = orders.size() - 1; i >= 0; i--) {
        //
        // if (orders.get(i).isSelected()) {
        // orders.remove(i);
        // }
        // }
        // productAdapter.notifyDataSetChanged();
        //
        // setProceeddOrderButtonStatus();
        // }
        // });

        proceeddOrderButton = (Button) findViewById(R.id.proceedOrderButton);
        // simulate calling send orders external services
        proceeddOrderButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                placeOrders();
            }

        });

        setProceeddOrderButtonStatus();

    }

    private void placeOrders() {
        if (!ProductOrderManager.hasOrders()) {
            Toast.makeText(this, "No products are selected in this order.", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog dialog = ProgressDialog.show(OrderCartActivity.this, "Placing Orders", "Please wait...", true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // ---simulate doing something lengthy---
                    Thread.sleep(3500);
                    // ---dismiss the dialog---
                    dialog.dismiss();

                    final Intent intent = new Intent();
                    intent.setData(Uri.parse(String.format("%s order(s) have been sent successfully.", ProductOrderManager.getNumberOfOrder())));
                    setResult(RESULT_OK, intent);
                    finish();

                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static final int DELETE_ORDER_MENU_ITEM_ID = 1;

    private static final int EDIT_ORDER_MENU_ITEM_ID = 0;

    // long press listview context menu
    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenu.ContextMenuInfo menuInfo) {

        if (view.getId() == R.id.ListViewCatalog) {
            final ListView lv = (ListView) view;
            final AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
            final Product product = (Product) lv.getItemAtPosition(acmi.position);

            menu.setQwertyMode(true);
            menu.setHeaderTitle(product.getName());
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
        final Product product = (Product) listViewCatalog.getItemAtPosition(info.position);

        switch (item.getItemId()) {
            case EDIT_ORDER_MENU_ITEM_ID:

                final int productIndex = ProductOrderManager.getProductIndex(product);
                final Intent productDetailsIntent = new Intent(getBaseContext(), ProductDetailsActivity.class);

                productDetailsIntent.putExtra(ProductOrderManager.SELECTED_PRODUCT, productIndex);
                startActivity(productDetailsIntent);

                // productAdapter.notifyDataSetChanged();
                // setProceeddOrderButtonStatus();

                return true;

            case DELETE_ORDER_MENU_ITEM_ID:
                ProductOrderManager.deleteOrder(product);
                productAdapter.notifyDataSetChanged();
                setProceeddOrderButtonStatus();
            default:
                return super.onContextItemSelected(item);

        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        productAdapter.notifyDataSetChanged();
        setProceeddOrderButtonStatus();
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

    private void setProceeddOrderButtonStatus() {
        proceeddOrderButton.setEnabled(ProductOrderManager.hasOrders());
    }

}