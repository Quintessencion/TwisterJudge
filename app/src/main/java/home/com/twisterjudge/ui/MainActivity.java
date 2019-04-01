package home.com.twisterjudge.main;

import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import home.com.twisterjudge.R;
import home.com.twisterjudge.app.App;
import home.com.twisterjudge.entity.StepObject;

public class MainActivity extends MvpAppCompatActivity implements MainView, TextToSpeech.OnInitListener {

    @Inject
    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_image)
    ImageView imageView;
    @BindView(R.id.image_start_stop)
    ImageButton button;
    @BindView(R.id.ad_view)
    AdView adView;

    private Spinner secondsSpinner;
    private CheckBox airTasks;
    private CheckBox extraTasks;

    private TextToSpeech TTL;
    private HashMap<String, String> map = new HashMap<>();
    private boolean isGame;

    @ProvidePresenter
    MainPresenter provideFilterPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prepareNavigationMenu();
        prepareActionBar();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        TTL = new TextToSpeech(this, this);
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
    }

    private void prepareNavigationMenu() {
        secondsSpinner = navigationView.getMenu().findItem(R.id.nav_timer).getActionView().findViewById(R.id.seconds_spinner);
        airTasks = navigationView.getMenu().findItem(R.id.nav_air_hands).getActionView().findViewById(R.id.checkBox_extra_air);
        extraTasks = navigationView.getMenu().findItem(R.id.nav_extra_tasks).getActionView().findViewById(R.id.checkBox_extra_tasks);

        secondsSpinner.setSelection(2);
        secondsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setDelay(Integer.parseInt((String) parent.getItemAtPosition(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        airTasks.setOnCheckedChangeListener((buttonView, isChecked) -> {
            presenter.setFlagAirTasks(isChecked);
            if (isChecked) {
                TTL.speak(getString(R.string.air_mode_on), TextToSpeech.QUEUE_ADD, map);
            } else {
                TTL.speak(getString(R.string.air_mode_off), TextToSpeech.QUEUE_ADD, map);
            }
        });

        extraTasks.setOnCheckedChangeListener((buttonView, isChecked) -> {
            presenter.setFlagExtraTasks(isChecked);
            extraTasks.setChecked(false);
            TTL.speak(MainActivity.this.getString(R.string.additional_assignments_have_not_delivered), TextToSpeech.QUEUE_ADD, map);
        });
    }

    private void prepareActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView.loadAd(new AdRequest.Builder().addTestDevice("1C0FDF73ED17134A1DD86BB6793131FE").build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.image_start_stop)
    public void startStopButton() {
        if (!isGame) {
            presenter.onGameStart();
        } else {
            presenter.stopGame();
        }
    }

    @Override
    public void gameStart(String text) {
        button.setImageResource(R.drawable.start);
        if (!isGame) {
            TTL.speak(text, TextToSpeech.QUEUE_ADD, map);
        }
        isGame = true;
    }

    @Override
    public void gameStop(String text) {
        if (isGame) {
            TTL.speak(text, TextToSpeech.QUEUE_ADD, map);
            button.setImageResource(R.drawable.stop);
        }
        isGame = false;
    }

    @Override
    public void makeMove(StepObject step) {
        TTL.speak(getString(R.string.mask_phrase, step.getBody().getTextBody(), step.getDirection().getTextColor()), TextToSpeech.QUEUE_ADD, map);

        imageView.setImageResource(step.getBody().getIdBody());
        imageView.setColorFilter(step.getDirection().getColor());
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            TTL.setLanguage(new Locale("ru"));
        } else {
            Toast.makeText(this, "Speech init error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adView.destroy();
        TTL.shutdown();
    }
}
