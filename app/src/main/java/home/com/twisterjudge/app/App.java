package home.com.twisterjudge.app;

import android.app.Application;

public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = buildComponent();
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
