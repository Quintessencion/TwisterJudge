package home.com.twisterjudge.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

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

    public MainPresenter(Repository repository) {
        this.repository = repository;
    }

    public void startGame() {
        disposable = Observable.interval(START_GAME_DELAY, delay, TimeUnit.SECONDS)
                .flatMap(aLong -> repository.getRandomColorAndPartBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::makeMove);
    }

    public void stopGame() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
        if (disposable != null) {
            stopGame();
            startGame();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
