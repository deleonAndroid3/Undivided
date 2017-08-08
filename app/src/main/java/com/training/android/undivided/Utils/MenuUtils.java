package com.training.android.undivided.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 8/8/2017.
 */
//used for grouped message sender - H
public class MenuUtils  {
    public static void selectDrawerItem(MenuItem menuItem, DrawerLayout mDrawer, Context context, IBaseActivity activity) {

    switch (menuItem.getItemId()) {
        case R.id.open_source_components: {
            OpenSourceItemsDialog dialog = new OpenSourceItemsDialog(context);
            activity.setCurrentDialog(dialog);
            dialog.show();
            break;
        }
        case R.id.rate_app: {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getApplicationContext().getPackageName())));
            break;
        }
        case R.id.about_app: {
            AboutDialog dialog = new AboutDialog(context);
            activity.setCurrentDialog(dialog);
            dialog.show();
            break;
        }
    }
    mDrawer.closeDrawers();
}

}
