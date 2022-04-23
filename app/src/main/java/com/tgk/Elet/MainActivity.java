package com.tgk.Elet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.tgk.Elet.databinding.ActivityMainBinding;
import com.tgk.Elet.databinding.ToolBarBinding;

import java.util.List;
import java.util.Locale;

import static com.tgk.Elet.CalendarFragment.daily_events;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    BottomNavigationView navView;
    int COUNT_BACK_PRESS = 0;
    ToolBarBinding toolView;
    String[] monthsList;
    int gy,gd,gm;
    Snackbar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        // toolbar
        toolView = mainBinding.toolbar;
        monthsList = getResources().getStringArray(R.array.monthsList);
        TextView evangelist=toolView.apostle;
        evangelist.setText(getResources().getStringArray(R.array.evangelists)[new BahreHasab(gy).WENGELAWI]);

        //Test............
        setDate();
        String gap=", ";
        String dateString=monthsList[gm-1]+gap+gy;
        TextView date=toolView.datum;
        date.setText(dateString);

        // Bottom Navigation bar
        navView = mainBinding.navView;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);
    }

    // date current date.
    private void setDate() {
        CurrentDate today=new CurrentDate();
        gy=today.getCurrentGeezYear();
        gd=today.getCurrentGeezDate();
        gm=today.getCurrentGeezMonth();
    }

    // unified Toast.
    void showToast(String string){
        int length= Toast.LENGTH_SHORT;
        Toast.makeText(this,string,length)
                .show();
    }

    // handles resetting amount of back press.
    void resetBackPressCount(){
        COUNT_BACK_PRESS=0;
    }

    @Override
    public void onBackPressed(){
        if (navView.getSelectedItemId()==R.id.navigation_home){
            if (COUNT_BACK_PRESS ==0){
                COUNT_BACK_PRESS=1;
                String prompt=getString(R.string.promptExit);
                showToast(prompt);
                (new Handler()).postDelayed(this::resetBackPressCount, 5000);
            }else{
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

    // settings button click.
    public void onDoneClick(View view){
        String restarted=getResources().getString(R.string.restarted);
        Toast.makeText(getApplicationContext(),restarted,Toast.LENGTH_SHORT).show();
        Intent intent= getIntent();
        finish();
        startActivity(intent);
    }
    // SOCIAL MEDIA BUTTONS*********************

    // navigating to social media and play store
    // all are working except facebook
    // case of facebook may be because I am not logged in.
    public void onMediaClick(View view){
        // all working except facebook.
        String url;
        String app;
        String app_link=null;
        if (findViewById(R.id.facebook).equals(view)) {
            app = "com.facebook.katana";
            url = "https://www.facebook.com/tigrinya/";
            app_link = "fb://page/tigrinya";
        } else if (findViewById(R.id.play_store).equals(view)) {
            url = "https://play.google.com/store/apps/details?id=com.tgk.Elet";
            app = "market://details?id=com.tgk.Elet";
        } else if (findViewById(R.id.twitter).equals(view)) {
            url = "https://twitter.com/temahari";
            app = "com.twitter.android";
            app_link = "twitter://user?user_id=temahari";
        } else {
            url = "https://www.tiktok.com/@branatigrinya";
            app = "com.zhiliaoapp.musically";
            app_link = "tiktok://user?user_id=@branatigrinya";
        }
        navToMedia(url,app,app_link);
    }

    private void navToMedia(String url,String app,String link){
        Intent intent;
        try {

            // get the app if possible
            this.getPackageManager().getPackageInfo(app, 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {

            // no app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
        this.startActivity(intent);
    }
    // END OF Social Media Buttons**********************************************

    // snack needed to be static so that I can call it from grid adapter.
    public void showSnack(int month, int index){
        String message;
        if (month==13){
            message=index+" - "+monthsList[month-1];
        }else{
            message=index+" - "+daily_events[index];
        }

        if (bar==null||!bar.isShown()){
            bar=Snackbar.make(navView, message, 3000);
        }else {
            bar.setText(message);
        }
        View barV=bar.getView();
        barV.setBackgroundColor(getResources().getColor(R.color.Neon));
        bar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        bar.setAnchorView(navView);
        bar.show();
    }

    // get app locale from Preference
    public void attachBaseContext(Context base) {
        String lang = PreferenceManager.getDefaultSharedPreferences(base).getString("lang","ትግርኛ");
        String loc;
        switch (lang) {
            case "English":
                loc = "en";
                break;
            case "Deutsch":
                loc = "de";
                break;
            default:
                loc = "ti";
        }
        //Locales
        Resources res = base.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        Locale locale = new Locale(loc);
        conf.setLocale(locale);
        res.updateConfiguration(conf, dm);
        base = base.createConfigurationContext(conf);
        super.attachBaseContext(base);
    }
}