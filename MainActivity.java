package in.iejaz.guessthenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.muddzdev.styleabletoast.StyleableToast;

import org.w3c.dom.Text;

import java.util.Random;

class game{
    int randNum;
    int enteredNum;
//    int guessed = 0;
    public int count = 0;

    game(){
        Random rand = new Random();
        randNum = rand.nextInt(100);
    }
}

public class MainActivity extends AppCompatActivity {

    EditText inputNum;
    TextView nofTrials, showHint, wonTxtShow, trialsShow;
    boolean win = false;
    LottieAnimationView my_animationView;
    String error01 = "you did't enter any number !";
    String trials = "Trials Only";

    private SoundPool soundPool;
    int clap, click, laser, error_sound;
    Button b;
    game gameObj = new game(); //to use variables of game class



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // to remove top bar

        inputNum = (EditText) findViewById(R.id.enterNum);
        nofTrials = (TextView) findViewById(R.id.noTrialsTxt);
        showHint = (TextView) findViewById(R.id.hintTxt);
        wonTxtShow = (TextView) findViewById(R.id.wonTxt);
        trialsShow = (TextView) findViewById(R.id.noTrialsShowNum);
        my_animationView = (LottieAnimationView) findViewById(R.id.animationView);
        showHint.setText("Enter Your Guess");


//------for SoundPool-------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);

        }
        laser = soundPool.load(this, R.raw.lasersound, 1);
        clap = soundPool.load(this, R.raw.clap, 1);
        click = soundPool.load(this, R.raw.pcmouseclick1, 1);
        error_sound = soundPool.load(this, R.raw.error_audio3, 1);


        my_animationView.setVisibility(View.INVISIBLE);
//        my_animationView.setAnimation(fileName);
//        my_animationView.loop(true);
//        my_animationView.playAnimation();


    }




    public void btn_enter(View view) {
//        showHint.startAnimation(my_animation);
        start_my_animation();

        soundPool.play(click, 1, 1, 0, 0, 1);

        if (inputNum.getText().toString().length() == 0){
            showToast(error01);
        }else{
            gameObj.enteredNum = Integer.parseInt(inputNum.getText().toString());
            isCorrect();
        }
}



    private void showToast(String text) {
        soundPool.play(error_sound, 1, 1, 0, 0, 1);
//
//        Toast.makeText(MainActivity.this,text, Toast.LENGTH_SHORT).show();
        StyleableToast.makeText(MainActivity.this,text, Toast.LENGTH_SHORT,R.style.toaststyle).show();


    }

    public void btn_reset(View view) {

        soundPool.play(laser, 1, 1, 0, 0, 1);
        start_my_animation_fade();

        game gameObj2 = new game();
        int randNum2 = gameObj2.randNum;
//        System.out.println(randNum2);
        gameObj.randNum = randNum2;
        showHint.setText("Reset Successfully");
        wonTxtShow.setText("");
        gameObj.count = 0;
        trialsShow.setText("");
        nofTrials.setText("");
        inputNum.setText("");
        win = false;
        my_animationView.setVisibility(View.INVISIBLE);


    }


    public void isCorrect(){
        gameObj.count++;

        if (gameObj.enteredNum> gameObj.randNum){
            showHint.setText("make it Lower");

        }else if(gameObj.enteredNum< gameObj.randNum){
            showHint.setText("make it Higher");
        }else{
            wonTxtShow.setText("You did it in");
            trialsShow.setText(String.valueOf(gameObj.count));
            nofTrials.setText(trials);
            showHint.setText("WINNER");
            win = true;
            soundPool.play(clap, 1, 1, 0, 0, 1);

            Animation my_animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate);
            showHint.startAnimation(my_animation);

            my_animationView.setVisibility(View.VISIBLE);
            my_animationView.playAnimation();

//
        }
    }

    public void btn_share(View view) {

        if (win){ // share
//
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi\nI Guessed the number in\n"+gameObj.count+"Steps \nWhats yours");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        }else{

            showToast("Play the game first");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.release();
    }

    private void start_my_animation() {
         Animation my_animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.bounce);
        showHint.startAnimation(my_animation);
    }

    private void start_my_animation_fade() {
        Animation my_animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.blink_anim);
        showHint.startAnimation(my_animation);
    }


}
