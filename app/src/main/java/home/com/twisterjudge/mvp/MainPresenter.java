package home.com.twisterjudge.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import home.com.twisterjudge.repository.Repository;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private static final int START_GAME_DELAY = 2;

    private Repository repository;
    private int delay;
    private Disposable disposable;

    @Inject
    public MainPresenter(Repository repository) {
        this.repository = repository;
    }

    public void onGameStart() {
        disposable = Observable.interval(START_GAME_DELAY, delay, TimeUnit.SECONDS)
                .flatMap(aLong -> repository.getRandomColorAndPartBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::makeMove);

        getViewState().gameStart(repository.getRandomWelcomePhrase());
    }

    public void stopGame() {
        disposable.dispose();
        disposable = null;
        getViewState().gameStop(repository.getRandomFarewellPhrase());
    }

    public void setDelay(int delay) {
        this.delay = delay;
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
            onGameStart();
        }
    }

    public void setFlagAirTasks(boolean isAddAirTasks) {
        repository.setFlagAirTasks(isAddAirTasks);
    }

    public void setFlagExtraTasks(boolean isAddExtraTasks) {
        repository.setFlagExtraTasks(isAddExtraTasks);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
