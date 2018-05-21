package home.com.twisterjudge.main;

import android.app.ActionBar;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import home.com.twisterjudge.R;
import home.com.twisterjudge.app.App;
import home.com.twisterjudge.entity.StepObject;
import home.com.twisterjudge.repository.Repository;

public class MainActivity extends MvpAppCompatActivity implements MainView,
        TextToSpeech.OnInitListener,
        AdapterView.OnItemSelectedListener {

    @Inject Repository repository;
    @InjectPresenter MainPresenter presenter;

    @BindView(R.id.main_image) ImageView imageView;
    @BindView(R.id.seconds_spinner) Spinner secondsSpinner;
    @BindView(R.id.image_start_stop) ImageButton button;

    private TextToSpeech TTL;
    private HashMap<String, String> map;

    @ProvidePresenter
    MainPresenter provideFilterPresenter() {
        return new MainPresenter(repository);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        secondsSpinner.setOnItemSelectedListener(this);
        secondsSpinner.setSelection(2);

        TTL = new TextToSpeech(this, this);
        map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }

    @OnClick(R.id.image_start_stop)
    public void startStopButton() {
        button.setSelected(!button.isSelected());
        if (button.isSelected()) {
            button.setImageResource(R.drawable.start);
            TTL.speak(getString(R.string.start_game), TextToSpeech.QUEUE_FLUSH, map);
            presenter.startGame();
        } else {
            button.setImageResource(R.drawable.stop);
            TTL.speak(getString(R.string.end_game), TextToSpeech.QUEUE_FLUSH, map);
            presenter.stopGame();
        }
    }

    @Override
    public void makeMove(StepObject step) {
        TTL.speak(getString(R.string.mask_phrase, step.getBody().getTextBody(), step.getColor().getTextColor()), TextToSpeech.QUEUE_FLUSH, map);

        imageView.setImageResource(step.getBody().getIdBody());
        imageView.setColorFilter(step.getColor().getColor());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.setDelay(Integer.parseInt((String) parent.getItemAtPosition(position)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            TTL.setLanguage(new Locale("ru"));
        } else {
            Toast.makeText(this, "Speech init error", Toast.LENGTH_SHORT).show();
        }
    }
}
