package home.com.twisterjudge.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import home.com.twisterjudge.R;
import home.com.twisterjudge.entity.Body;
import home.com.twisterjudge.entity.Color;
import home.com.twisterjudge.entity.StepObject;
import io.reactivex.Observable;

public class Repository {

    private Context context;
    private List<Color> colors;
    private List<Body> partBody;
    private Random randomGenerator;

    public Repository(Context context) {
        this.context = context;

        colors = new ArrayList<>();
        colors.add(new Color("красный", context.getResources().getColor(R.color.red)));
        colors.add(new Color("синий", context.getResources().getColor(R.color.blue)));
        colors.add(new Color("зелёный", context.getResources().getColor(R.color.green)));
        colors.add(new Color("жёлтый", context.getResources().getColor(R.color.yellow)));

        partBody = new ArrayList<>();
        partBody.add(new Body("Левую ногу", R.drawable.left_footprint));
        partBody.add(new Body("Правую ногу", R.drawable.right_footprint));
        partBody.add(new Body("Левую руку", R.drawable.left_handprint));
        partBody.add(new Body("Правую руку", R.drawable.right_handprint));

        randomGenerator = new Random();
    }

    public Observable<StepObject> getRandomColorAndPartBody() {
        final int randomIndexColor = randomGenerator.nextInt(colors.size());
        final int randomIndexBody = randomGenerator.nextInt(partBody.size());

        return Observable.fromCallable(() ->
                new StepObject(colors.get(randomIndexColor), partBody.get(randomIndexBody)));
    }
}
