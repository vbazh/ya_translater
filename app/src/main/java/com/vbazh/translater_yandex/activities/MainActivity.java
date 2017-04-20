package com.vbazh.translater_yandex.activities;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.vbazh.translater_yandex.Constants;
import com.vbazh.translater_yandex.R;
import com.vbazh.translater_yandex.fragments.FavoritesFragment;
import com.vbazh.translater_yandex.fragments.HistoryFragment;
import com.vbazh.translater_yandex.fragments.TranslateFragment;
import com.vbazh.translater_yandex.model.Language;
import com.vbazh.translater_yandex.utils.InternetConnection;
import com.vbazh.translater_yandex.utils.OpenTranslateEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    private InternetConnection internetConnection;
    private int prevNavItem = 1;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        internetConnection = new InternetConnection();
        registerReceiver(internetConnection, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetConnection);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    void init() {

        if (getSupportFragmentManager().findFragmentById(R.id.frame_main)==null){
            switchFragment(new TranslateFragment(), 1);
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
                switch (item.getItemId()) {
                    case R.id.action_translate:

                        //чтобы добавить кастомную анимацию вынужден передавать номер фрагмента...(
                        if (!(currentFragment instanceof TranslateFragment)) {
                            switchFragment(new TranslateFragment(), 1);
                        }
                        break;

                    case R.id.action_favorites:
                        if (!(currentFragment instanceof FavoritesFragment)) {
                            switchFragment(new FavoritesFragment(), 2);
                        }
                        break;

                    case R.id.action_history:
                        if (!(currentFragment instanceof HistoryFragment)) {
                            switchFragment(new HistoryFragment(), 3);
                        }
                        break;
                }
                return true;
            }
        });
    }


    private void switchFragment(Fragment fragment, int fragmentNumber) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (prevNavItem > fragmentNumber) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_out_appear, R.anim.slide_out_disappear);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_appear, R.anim.slide_in_disappear);
        }

        prevNavItem = fragmentNumber;

        fragmentTransaction.setAllowOptimization(true);
        fragmentTransaction.replace(R.id.frame_main, fragment).commit();
        fragmentManager.executePendingTransactions();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OpenTranslateEvent event) {
        SharedPreferences settingsPreferences = this.getSharedPreferences(Constants.SHARED_SETTINGS_PREFERENCES, MODE_PRIVATE);
        settingsPreferences.edit().putString(Constants.SHARED_SOURCE_TEXT, event.translation.getSourceText()).apply();
        settingsPreferences.edit().putString(Constants.SHARED_TRANSLATED_TEXT, event.translation.getTranslatedText()).apply();
        int pos = 0;
        for (Language language:Constants.languages){
            if (language.getCode().equals(event.translation.getSourceLang())){
                settingsPreferences.edit().putInt(Constants.SHARED_SOURCE_LANG_POSITION, pos ).apply();

            } else if (language.getCode().equals(event.translation.getTargetLang())){
                settingsPreferences.edit().putInt(Constants.SHARED_TARGET_LANG_POSITION, pos).apply();
            }
            pos++;
        }

        bottomNavigationView.setSelectedItemId(R.id.action_translate);
    }
}
