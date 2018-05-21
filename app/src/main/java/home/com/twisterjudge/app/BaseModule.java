package home.com.twisterjudge.app;

import android.content.Context;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import home.com.twisterjudge.repository.Repository;

@Module
public class BaseModule {

    private Context context;

    BaseModule(@Nonnull Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Nonnull
    @Singleton
    Repository providesSharedPreferences(Context context) {
        return new Repository(context);
    }
}
