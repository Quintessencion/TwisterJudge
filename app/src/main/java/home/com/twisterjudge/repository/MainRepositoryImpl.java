package home.com.twisterjudge.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import home.com.twisterjudge.R;
import home.com.twisterjudge.entity.Air;
import home.com.twisterjudge.entity.Body;
import home.com.twisterjudge.entity.Color;
import home.com.twisterjudge.entity.Direction;
import home.com.twisterjudge.entity.StepObject;
import io.reactivex.Observable;

public class Repository {

    private static final int UNSELECTED = -1;

    private Context context;
    private List<String> welcomePhrases;
    private List<String> farewellPhrases;
    private List<Direction> colors;
    private List<Body> partBody;
    private List<Direction> airTasks;
    private Random randomGenerator;

    private int randomIndexColor;
    private int randomIndexBody;
    private int lastColor;
    private int lastBody;
    private boolean isAddAirTasks;
    private boolean isAddExtraTasks;

    public Repository(Context context) {
        this.context = context;

        welcomePhrases = Arrays.asList(context.getResources().getStringArray(R.array.welcome_phrases));
        farewellPhrases = Arrays.asList(context.getResources().getStringArray(R.array.farewell_phrases));

        colors = new ArrayList<>();
        colors.add(new Color("на красный", context.getResources().getColor(R.color.red)));
        colors.add(new Color("на синий", context.getResources().getColor(R.color.blue)));
        colors.add(new Color("на зелёный", context.getResources().getColor(R.color.green)));
        colors.add(new Color("на жёлтый", context.getResources().getColor(R.color.yellow)));

        partBody = new ArrayList<>();
        partBody.add(new Body("Левую ногу", R.drawable.left_footprint));
        partBody.add(new Body("Правую ногу", R.drawable.right_footprint));
        partBody.add(new Body("Левую руку", R.drawable.left_handprint));
        partBody.add(new Body("Правую руку", R.drawable.right_handprint));

        airTasks = new ArrayList<>();
        airTasks.add(new Air("вверх", context.getResources().getColor(R.color.colorAccent)));
        airTasks.add(new Air("в сторону", context.getResources().getColor(R.color.colorAccent)));

        randomGenerator = new Random();

        lastColor = UNSELECTED;
        lastBody = UNSELECTED;
    }

    public String getRandomWelcomePhrase() {
        return welcomePhrases.get(getRandomIndex(welcomePhrases.size()));
    }

    public String getRandomFarewellPhrase() {
        return farewellPhrases.get(getRandomIndex(farewellPhrases.size()));
    }

    public Observable<StepObject> getRandomColorAndPartBody() {
        randomIndexColor = getRandomIndex(colors.size());
        randomIndexBody = getRandomIndex(partBody.size());

        while (lastColor == randomIndexColor && lastBody == randomIndexBody) {
            switch (randomGenerator.nextInt(2)) {
                case 0:
                    randomIndexColor = getRandomIndex(colors.size());
                    break;
                case 1:
                    randomIndexBody = getRandomIndex(partBody.size());
                    break;
            }
        }

        if (isAddAirTasks && randomIndexColor == 4 || isAddAirTasks && randomIndexColor == 5) {
            while (randomIndexBody == 0 || randomIndexBody == 1) {
                randomIndexBody = getRandomIndex(partBody.size());
            }
        }

        lastColor = randomIndexColor;
        lastBody = randomIndexBody;

        return Observable.fromCallable(() ->
                new StepObject(colors.get(randomIndexColor), partBody.get(randomIndexBody)));
    }

    private int getRandomIndex(int size) {
        return randomGenerator.nextInt(size);
    }

    public void setFlagAirTasks(boolean isAddAirTasks) {
        this.isAddAirTasks = isAddAirTasks;
        if (isAddAirTasks) {
            colors.addAll(airTasks);
        } else {
            colors.removeAll(airTasks);
        }
    }

    public void setFlagExtraTasks(boolean isAddExtraTasks) {
        this.isAddExtraTasks = isAddExtraTasks;
        if (isAddExtraTasks) {

        } else {

        }
    }
}
