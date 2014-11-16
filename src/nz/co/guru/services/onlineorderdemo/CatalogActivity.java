package nz.co.guru.services.onlineorderdemo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nz.co.guru.services.onlineorderdemo.model.Language;
import nz.co.guru.services.onlineorderdemo.model.ProductItem;
import nz.co.guru.services.onlineorderdemo.settings.SettingsActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CatalogActivity extends Activity {

    private FrameLayout rootView;

    private ExpandableListView catalogListView;

    private CatalogListAdapter catalogListAdapter;

    private Button viewOrderButton;

    private ActionBar actionBar;

    private static final int PLACE_ORDER_REQ = 0;

    private boolean displayAdBanner = false;;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    private List<ProductItem> specialOfferProducts = new ArrayList<ProductItem>();

    private View adBannerView;

    final static int adAnimationStartYPoint = -200;

    private boolean adBannerIsShown;

    private static final int adBannerAnimationDuration = 1000;

    /**
     * handler to hide the adBanner in 30 seconds
     */
    private Handler hideBannerHandler;

    private Runnable hideBannerRunnable;

    private static final int ADD_BANNER_MAX_SHOW_TIME = 30000; // mili seconds

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog);

        // catalog root view
        rootView = (FrameLayout) findViewById(R.id.catalogListRootView);

        // action bar
        actionBar = getActionBar();
        // To make the application icon clickable, you need to call the setDisplayHomeAsUpEnabled()
        actionBar.setDisplayHomeAsUpEnabled(true);

        catalogListView = (ExpandableListView) findViewById(R.id.catalogListView);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean displayAdSetting = sharedPref.getBoolean(SettingsActivity.KEY_PREF_DISPLAY_ADVERTISEMENT, true);

        if (displayAdSetting) {
            // create AD banner if there are special offers
            specialOfferProducts = ProductOrderManager.getSpecialOfferProducts();
            if (specialOfferProducts.size() > 0) {
                displayAdBanner = true;
                createADBanner();
            }
        }

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

    /**
     * Create AD banner and setup touch handler
     */
    private void createADBanner() {

        final LayoutInflater inflater = getLayoutInflater();
        adBannerView = inflater.inflate(R.layout.banner, (ViewGroup) findViewById(R.id.addBanner));

        // catalogListView.addHeaderView(addBannerView);

        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        rootView.addView(adBannerView, params);
        // bring banner to front
        adBannerView.bringToFront();
        rootView.requestLayout();
        rootView.invalidate();

        setupBannerText();

        // detect swipe
        // final GestureDetector gestureDetector = new GestureDetector(this, new SimpleOnGestureListener() {
        //
        // @Override
        // public boolean onSingleTapUp(final MotionEvent e) {
        // return false;
        // }
        //
        // @Override
        // public void onShowPress(final MotionEvent e) {
        // Log.d("onShowPress", "onShowPress");
        // }
        //
        // @Override
        // public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
        // return false;
        // }
        //
        // @Override
        // public void onLongPress(final MotionEvent e) {
        // Log.d("onLongPress", "onLongPress");
        // }
        //
        // @Override
        // public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        // switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
        // case 1:
        // // Log.d(LOGTAG, "top");
        // return true;
        // case 2:
        // // Log.d(LOGTAG, "left");
        // return true;
        // case 3:
        // // Log.d(LOGTAG, "down");
        // return true;
        // case 4:
        // // Log.d(LOGTAG, "right");
        // return true;
        // }
        // return false;
        // }
        //
        // @Override
        // public boolean onDown(final MotionEvent e) {
        // return false;
        // }
        //
        // private int getSlope(final float x1, final float y1, final float x2, final float y2) {
        // final Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        // if (angle > 45 && angle <= 135) {
        // // top
        // return 1;
        // }
        // if (angle >= 135 && angle < 180 || angle < -135 && angle > -180) {
        // // left
        // return 2;
        // }
        // if (angle < -45 && angle >= -135) {
        // // down
        // return 3;
        // }
        // if (angle >= -45 && angle <= 45) {
        // // right
        // return 4;
        // }
        // return 0;
        // }
        // });

        // addBannerView.setOnTouchListener(new OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(final View view, final MotionEvent event) {
        // return gestureDetector.onTouchEvent(event);
        // }
        // });

        // final AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        // alphaAnimation.setDuration(3000);
        // alphaAnimation.setFillAfter(true);
        // alphaAnimation.start();

        animateShowAdBanner();

    }

    /**
     * Show ad banner and start timer to hide it automatically in 30 seconds
     */
    private void animateShowAdBanner() {
        final TranslateAnimation slideDownAnimation = new TranslateAnimation(0, 0, adAnimationStartYPoint, 0);
        slideDownAnimation.setDuration(adBannerAnimationDuration);
        slideDownAnimation.setFillAfter(true);
        adBannerIsShown = true;
        adBannerView.startAnimation(slideDownAnimation);

        hideBannerHandler = new Handler();
        hideBannerRunnable = new Runnable() {

            @Override
            public void run() {
                // catalogListView.removeHeaderView(header);

                // fade away header
                // final ObjectAnimator anim = ObjectAnimator.ofFloat(header, View.ALPHA, 0);
                // anim.setDuration(10000);
                // header.setHasTransientState(true);
                // anim.addListener(new AnimatorListenerAdapter() {
                //
                // @Override
                // public void onAnimationEnd(final Animator animation) {
                // addBannerView.setAlpha(1);
                // addBannerView.setHasTransientState(false);
                // catalogListView.removeHeaderView(header);
                // }
                // });
                // anim.start();

                if (adBannerIsShown) {
                    animateHideAdBanner();
                }
            }
        };
        hideBannerHandler.postDelayed(hideBannerRunnable, ADD_BANNER_MAX_SHOW_TIME);
    }

    private void animateHideAdBanner() {
        final TranslateAnimation slideUpAnimation = new TranslateAnimation(0, 0, 0, adAnimationStartYPoint);
        slideUpAnimation.setDuration(adBannerAnimationDuration);
        slideUpAnimation.setFillAfter(true);
        adBannerIsShown = false;
        adBannerView.startAnimation(slideUpAnimation);
    }

    private void setupBannerText() {
        // marquee text scrooling
        final TextView bannerTextView = (TextView) findViewById(R.id.bannerText);
        bannerTextView.setSelected(true);
        final StringBuilder textBuilder = new StringBuilder();
        int count = 1;
        for (final ProductItem item : specialOfferProducts) {
            textBuilder.append(item.getName()).append(" ").append(item.printPrice());
            if (count != specialOfferProducts.size()) {
                textBuilder.append(", ");
                count++;
            }
        }
        bannerTextView.setText(textBuilder.toString());
    }

    private Language getLanguageFromPreferenceSettings(final SharedPreferences sharedPref) {
        return Language.valueOf(sharedPref.getString(SettingsActivity.KEY_PREF_LANGUAGE, Language.ENG.toString()));
    }

    private void handleLanguageChange(final Language language) {
        ProductOrderManager.setLanguage(language);
        catalogListAdapter.notifyDataSetChanged();
        if (displayAdBanner) {
            setupBannerText();
        }
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

    private static final int SPECIAL_OFFER_ID = 0;

    private static final int ORDER_HISTORY_ITEM_ID = 1;

    private static final int SETTINGS_ITEM_ID = 2;

    private static final int LOG_OUT_ITEM_ID = 3;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (displayAdBanner) {
            final MenuItem viewOrderItem = menu.add(0, SPECIAL_OFFER_ID, SPECIAL_OFFER_ID, "View Order");
            {
                viewOrderItem.setIcon(R.drawable.special_offer);
                viewOrderItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            }
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
            case SPECIAL_OFFER_ID:
                if (hideBannerHandler != null && hideBannerRunnable != null) {
                    hideBannerHandler.removeCallbacks(hideBannerRunnable);
                    hideBannerRunnable = null;
                    hideBannerHandler = null;
                }
                if (adBannerIsShown) {
                    animateHideAdBanner();
                }
                else {
                    animateShowAdBanner();
                }
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
