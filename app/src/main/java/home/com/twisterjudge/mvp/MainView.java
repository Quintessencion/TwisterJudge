package home.com.twisterjudge.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import home.com.twisterjudge.entity.StepObject;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {

    void gameStart(String text);

    void gameStop(String text);

    void makeMove(StepObject stepObject);
}
