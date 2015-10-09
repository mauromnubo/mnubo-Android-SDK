package com.mnubo.platform.android.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mnubo.platform.android.demo.intentutils.Actions;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.demo.views.AboutActivity;
import com.mnubo.platform.android.demo.views.login.CompletePasswordResetActivity;
import com.mnubo.platform.android.demo.views.login.LoginActivity;
import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.api.MnuboApi;

public abstract class MnuboAbstractActivity extends Activity {

    protected MnuboApi mnuboApi;
    private ServiceReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mnuboApi = Mnubo.getApi();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows the progressView UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show) {


        final View progressView = findViewById(R.id.progress);
        final View formView = findViewById(R.id.form);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progressView spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            formView.setVisibility(show ? View.GONE : View.VISIBLE);
            formView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            formView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    protected void logOutAndShowLogInActivity() {
        mnuboApi.getAuthenticationOperations().logOut();
        showLogInActivity();
    }

    protected void showLogInActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    protected void registerServiceReceiver() {
        //Register accel service receiver
        IntentFilter filter = new IntentFilter(Actions.ACTION_TOAST_MESSAGE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ServiceReceiver();
        registerReceiver(receiver, filter);
    }

    protected void unregisterServiceReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterServiceReceiver();
    }

    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(Extras.EXTRA_SERVICE_MESSAGE);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }
}