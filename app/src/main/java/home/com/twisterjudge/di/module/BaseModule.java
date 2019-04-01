package home.com.twisterjudge.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import home.com.twisterjudge.repository.Repository;

@Module
public class BaseModule {

    private Context context;

    BaseModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    Repository providesRepository(Context context) {
        return new Repository(context);
    }
}
