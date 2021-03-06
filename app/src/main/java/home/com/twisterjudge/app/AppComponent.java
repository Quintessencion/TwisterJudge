package home.com.twisterjudge.app;

import javax.inject.Singleton;

import dagger.Component;
import home.com.twisterjudge.main.MainActivity;

@Singleton
@Component(modules = BaseModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
}
