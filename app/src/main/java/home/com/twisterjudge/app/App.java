package home.com.twisterjudge.app;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

import home.com.twisterjudge.R;

public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = buildComponent();

        MobileAds.initialize(this, getString(R.string.ad_app_id));
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .baseModule(new BaseModule(this))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
