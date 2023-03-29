package com.example.tic_tac_toe;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    String StartGame = "X";
    int[] arr = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int i;
    int countX, countO, countD;

    TextView txt1, txt2, txt3;
    Button btnreset, btnGameMode;
    ImageView playerTurn;
    ImageView[] mCases;
    private boolean botEnabled = false;
    private boolean botLevelHard = false;
    private SeekBar mVolumeSeekBar;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private ToggleButton mSoundToggle;

//x=1
//y=2

    public static Bitmap createXBitmap(int width, int height, int startColor, int endColor) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Créer le dégradé linéaire
        LinearGradient gradient = new LinearGradient(0, 0, width, height, startColor, endColor, Shader.TileMode.CLAMP);

        // Définir la peinture (paint) avec le dégradé et le style Material Design
        Paint paint = new Paint();
        paint.setShader(gradient);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setShadowLayer(5.0f, 0.0f, 2.0f, Color.BLACK);

        // Dessiner la première ligne diagonale
        canvas.drawLine(0, 0, width, height, paint);

        // Dessiner la deuxième ligne diagonale
        canvas.drawLine(width, 0, 0, height, paint);

        // Renvoyer le bitmap
        return bitmap;
    }

    public static Bitmap createOBitmap(int width, int height, int radius, int color) {
        // Créer un Bitmap de taille width x height
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Créer un Canvas à partir du Bitmap
        Canvas canvas = new Canvas(bitmap);

        // Dessiner un cercle blanc rempli au centre du Canvas
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        // Dessiner un cercle noir autour du cercle blanc pour créer le contour
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);

        // Ajouter l'effet Material Design au cercle noir
        paint.setShadowLayer(5.0f, 0.0f, 2.0f, Color.BLACK);

        // Créer le dégradé linéaire pour le cercle noir
        int endColor = Color.argb(255, 33, 33, 33);
        LinearGradient gradient = new LinearGradient(0, 0, width, height, color, endColor, Shader.TileMode.CLAMP);

        // Appliquer le dégradé à la peinture
        paint.setShader(gradient);

        canvas.drawCircle(width / 2, height / 2, radius, paint);

        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GrilleJeuCustomView grilleJeuCustomView = findViewById(R.id.grilleJeu);
        mCases = grilleJeuCustomView.getmCases();

        for (ImageView mCase : mCases) {
            mCase.setOnClickListener(this);
        }


        LinearLayout linearLayoutScore = findViewById(R.id.LLScore);


        btnreset = findViewById(R.id.btnreset);
        btnGameMode = findViewById(R.id.btnGameMode);
        txt1 = findViewById(R.id.txtXWins);
        txt2 = findViewById(R.id.txtOWins);
        txt3 = findViewById(R.id.txtDraws);
        playerTurn = findViewById(R.id.playerTurn);
        //  bot = new Bot(grilleJeuCustomView, linearLayoutScore, txt1, txt2);

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Appliquer l'animation au bouton
                Animation animation = getAnimation(3);
                animation.setRepeatCount(Animation.ABSOLUTE);
                grilleJeuCustomView.startAnimation(animation);

                Animation bounceAnimation = getAnimation(2);
                bounceAnimation.setRepeatCount(Animation.ABSOLUTE);
                linearLayoutScore.startAnimation(bounceAnimation);

                resetGame();
                countX = 0;
                countO = 0;
                countD = 0;
                txt1.setText(String.valueOf(countX));
                txt2.setText(String.valueOf(countO));
                txt3.setText(String.valueOf(countD));

            }
        });
        btnGameMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!botEnabled) {
                    botEnabled = true;
                    botLevelHard=false;
                    btnGameMode.setText("Easy Bot");
                    resetGame();
                    StartGame = "X";
                    playerTurn.setImageResource(R.drawable.x);
                } else {
                    if(!botLevelHard){
                        btnGameMode.setText("Hard Bot");
                        botLevelHard=true;
                        resetGame();
                        StartGame = "X";
                        playerTurn.setImageResource(R.drawable.x);
                    }
                    else{
                        btnGameMode.setText("2 Players");
                        botLevelHard=false;
                        botEnabled = false;
                        resetGame();
                    }

                }
            }
        });

        mVolumeSeekBar = findViewById(R.id.volumeSeekBar);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMediaPlayer = MediaPlayer.create(this, R.raw.mario);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volume = (float) currentVolume / (float) maxVolume;
        mMediaPlayer.setVolume(volume, volume);

        mVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = (float) progress / 100.0f;
                mMediaPlayer.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
/*
//Récupérer le bouton bascule pour le son
        mSoundToggle = findViewById(R.id.sound_toggle);

        // Créer un objet MediaPlayer pour jouer l'audio
        mMediaPlayer = MediaPlayer.create(this, R.raw.mario);
        // Désactiver la boucle (par défaut, MediaPlayer boucle l'audio)
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);
// Ajouter un écouteur de clic au bouton bascule pour le son
        mSoundToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSoundToggle.isChecked()) {
                        mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.mario);

                    // Activer le son
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.start();
                } else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                    }
                }*//*
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                }*//*
            }
        });*/


    }

    // Arrêter l'audio lors de la fermeture de l'activité
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        } else {
            mMediaPlayer = MediaPlayer.create(this, R.raw.mario);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        } else {
            mMediaPlayer = MediaPlayer.create(this, R.raw.mario);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }
    }

    public void playSound(View view) {
        mMediaPlayer.start();
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volume = (float) currentVolume / (float) maxVolume;
        mMediaPlayer.setVolume(volume, volume);
    }

    public void choosePalyer() {
        if (StartGame.equals("X")) {
            StartGame = "O";
            playerTurn.setImageResource(R.drawable.o);
        } else {
            StartGame = "X";
            playerTurn.setImageResource(R.drawable.x);
        }
    }

    public Boolean winningGame() {
        if ((arr[0] == 1 && arr[1] == 1 && arr[2] == 1) || (arr[3] == 1 && arr[4] == 1 && arr[5] == 1) || (arr[6] == 1 && arr[7] == 1 && arr[8] == 1) || (arr[0] == 1 && arr[3] == 1 && arr[6] == 1) || (arr[1] == 1 && arr[4] == 1 && arr[7] == 1) || (arr[2] == 1 && arr[5] == 1 && arr[8] == 1) || (arr[0] == 1 && arr[4] == 1 && arr[8] == 1) || (arr[2] == 1 && arr[4] == 1 && arr[6] == 1)) {

            AlertFragment alertFragment = new AlertFragment("Player X Wins", 1);
            alertFragment.setPositiveButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Code à exécuter lorsque le bouton positif est cliqué

                    resetGame();
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Affichage de la fenêtre de dialogue
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    alertFragment.setCancelable(false);
                    alertFragment.show(fragmentManager, "AlertFragment");
                }
            }, 3000);

            endGame();
            countX++;
            txt1.setText(String.valueOf(countX));

            return true;
        } else if ((arr[0] == 2 && arr[1] == 2 && arr[2] == 2) || (arr[3] == 2 && arr[4] == 2 && arr[5] == 2) || (arr[6] == 2 && arr[7] == 2 && arr[8] == 2) || (arr[0] == 2 && arr[3] == 2 && arr[6] == 2) || (arr[1] == 2 && arr[4] == 2 && arr[7] == 2) || (arr[2] == 2 && arr[5] == 2 && arr[8] == 2) || (arr[0] == 2 && arr[4] == 2 && arr[8] == 2) || (arr[2] == 2 && arr[4] == 2 && arr[6] == 2)) {

            AlertFragment alertFragment = new AlertFragment("Player O Wins", 2);
            alertFragment.setPositiveButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Code à exécuter lorsque le bouton positif est cliqué

                    resetGame();
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Affichage de la fenêtre de dialogue
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    alertFragment.setCancelable(false);
                    alertFragment.show(fragmentManager, "AlertFragment");
                }
            }, 3000);

            endGame();
            countO++;
            txt2.setText(String.valueOf(countO));
            return true;
        } else if ((arr[0] != 0 && arr[1] != 0 && arr[2] != 0 && arr[3] != 0 && arr[4] != 0 && arr[5] != 0 && arr[6] != 0 && arr[7] != 0 && arr[8] != 0)) {
            AlertFragment alertFragment = new AlertFragment("The Game is Draw !!!", 1);
            alertFragment.setPositiveButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Code à exécuter lorsque le bouton positif est cliqué

                    resetGame();
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Affichage de la fenêtre de dialogue
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    alertFragment.setCancelable(false);
                    alertFragment.show(fragmentManager, "AlertFragment");
                }
            }, 3000);

            endGame();
            countD++;
            txt3.setText(String.valueOf(countD));
            return true;
        }
        return false;
    }

    public void resetGame() {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
        for (int j = 0; j < 9; j++) {

            mCases[j].setImageDrawable(null);
            mCases[j].setClickable(true);

        }
        btnreset.setClickable(true);
        btnGameMode.setClickable(true);
    }

    public void endGame() {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
        for (int j = 0; j < 9; j++) {
            mCases[j].setClickable(false);

        }
        btnreset.setClickable(false);
        btnGameMode.setClickable(false);
    }

    @Override
    public void onClick(View view) {

        for (int i = 0; i < 9; i++) {
            mCases[i].clearAnimation(); // Stopper l'animation en cours sur chaque ImageView
        }
        if (!botEnabled) {
            for (int i = 0; i < 9; i++) {
                if (mCases[i] == view) {
                    if (StartGame.equals("X")) {
                        // Toast.makeText(getApplicationContext(),"helllo"+i,Toast.LENGTH_LONG).show();

                        //  Bitmap xBitmap = createXBitmap(100, 100, Color.BLUE);
                        Bitmap xBitmap = createXBitmap(100, 100, Color.BLUE, Color.CYAN);
                        mCases[i].setImageBitmap(xBitmap);
                        //ajouter une animation
                        Animation animation = getAnimation(i % 3);
                        animation.setRepeatCount(Animation.INFINITE);
                        mCases[i].startAnimation(animation);
                        mCases[i].setClickable(false);
                        // mCases[i].setImageResource(R.drawable.picx);
                        arr[i] = 1;
                        i++;

                    } else {
                        Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                        mCases[i].setImageBitmap(oBitmap);

                        //ajouter une animation
                        Animation animation = getAnimation(i % 3);
                        animation.setRepeatCount(Animation.INFINITE);
                        mCases[i].startAnimation(animation);
                        mCases[i].setClickable(false);
                        // mCases[i].setImageResource(R.drawable.circle);
                        arr[i] = 2;
                        i++;
                    }
                    choosePalyer();
                    winningGame();
                }

            }
        } //botenabled
         else if(botEnabled && !botLevelHard){
            Toast.makeText(getApplicationContext(),"(botEnabled && !botLevelHard) : "+(botEnabled && !botLevelHard),Toast.LENGTH_LONG).show();
            if (!StartGame.equals("X")) {

                StartGame = "X";
                playerTurn.setImageResource(R.drawable.x);
            }

            for (int i = 0; i < 9; i++) {
                if (mCases[i] == view) {
                    // Toast.makeText(getApplicationContext(),"helllo"+i,Toast.LENGTH_LONG).show();

                    //  Bitmap xBitmap = createXBitmap(100, 100, Color.BLUE);
                    Bitmap xBitmap = createXBitmap(100, 100, Color.BLUE, Color.CYAN);
                    mCases[i].setImageBitmap(xBitmap);
                    //ajouter une animation
                    Animation animation = getAnimation(i % 3);
                    animation.setRepeatCount(Animation.INFINITE);
                    mCases[i].startAnimation(animation);
                    mCases[i].setClickable(false);
                    // mCases[i].setImageResource(R.drawable.picx);
                    arr[i] = 1;
                    i++;
                    if(!winningGame()){


                        for (int j = 0; j < 9; j++) {
                            mCases[j].clearAnimation(); // Stopper l'animation en cours sur chaque ImageView
                        }
                        // Le bot est activé, donc il joue automatiquement un coup aléatoire
                        int index = new Random().nextInt(9);
                        while (arr[index] != 0) {
                            index = new Random().nextInt(9);
                        }
                        Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                        mCases[index].setImageBitmap(oBitmap);

                        //ajouter une animation
                        animation = getAnimation(index % 3);
                        animation.setRepeatCount(Animation.INFINITE);
                        mCases[index].startAnimation(animation);
                        mCases[index].setClickable(false);
                        // mCases[i].setImageResource(R.drawable.circle);
                        arr[index] = 2;
        }
                }
                winningGame();
            }
         }
        else
        {

            if (!StartGame.equals("X")) {

                StartGame = "X";
                playerTurn.setImageResource(R.drawable.x);
            }

            for (int i = 0; i < 9; i++) {
                if (mCases[i] == view) {
                    // Toast.makeText(getApplicationContext(),"helllo"+i,Toast.LENGTH_LONG).show();

                    //  Bitmap xBitmap = createXBitmap(100, 100, Color.BLUE);
                    Bitmap xBitmap = createXBitmap(100, 100, Color.BLUE, Color.CYAN);
                    mCases[i].setImageBitmap(xBitmap);
                    //ajouter une animation
                    Animation animation = getAnimation(i % 3);
                    animation.setRepeatCount(Animation.INFINITE);
                    mCases[i].startAnimation(animation);
                    mCases[i].setClickable(false);
                    // mCases[i].setImageResource(R.drawable.picx);
                    arr[i] = 1;
                    i++;
                    if (!winningGame()) {


                        for (int j = 0; j < 9; j++) {
                            mCases[j].clearAnimation(); // Stopper l'animation en cours sur chaque ImageView
                        }
                         /*// Le bot est activé, donc il joue automatiquement un coup aléatoire
                         int index = new Random().nextInt(9);
                         while (arr[index] != 0) {
                             index = new Random().nextInt(9);
                         }
                         Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                         mCases[index].setImageBitmap(oBitmap);

                         //ajouter une animation
                         animation = getAnimation(index % 3);
                         animation.setRepeatCount(Animation.INFINITE);
                         mCases[index].startAnimation(animation);
                         mCases[index].setClickable(false);
                         // mCases[i].setImageResource(R.drawable.circle);
                         arr[index] = 2;*/
                        // Trouver les coups possibles pour le bot
                        List<Integer> coupsPossibles = new ArrayList<>();
                        for (int x = 0; x < 9; x++) {
                            if (arr[x] == 0) {
                                coupsPossibles.add(x);
                            }
                        }

// Parcourir chaque ligne, colonne et diagonale pour trouver les coups du joueur
                        int[][] lignes = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
                        int[][] colonnes = {{0, 3, 6}, {1, 4, 7}, {2, 5, 8}};
                        int[][] diagonales = {{0, 4, 8}, {2, 4, 6}};

                        for (int[] ligne : lignes) {
                            int coupsJoueur = 0;
                            int coupsBot = 0;
                            int indexRestant = -1;
                            for (int e : ligne) {
                                if (arr[e] == 1) {
                                    coupsJoueur++;
                                } else if (arr[e] == 2) {
                                    coupsBot++;
                                } else {
                                    indexRestant = e;
                                }
                            }
                            if (coupsJoueur == 2 && coupsBot == 0) {
                                // Le joueur a deux coups alignés, le bot doit bloquer l'adversaire
                                Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                                mCases[indexRestant].setImageBitmap(oBitmap);
                                arr[indexRestant] = 2;
                                animation = getAnimation(indexRestant % 3);
                                animation.setRepeatCount(Animation.INFINITE);
                                mCases[indexRestant].startAnimation(animation);
                                mCases[indexRestant].setClickable(false);
                                winningGame();
                                return;
                            }
                            if (coupsBot == 2 && coupsJoueur == 0) {
                                // Le bot a deux coups alignés, le bot doit gagner
                                Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                                mCases[indexRestant].setImageBitmap(oBitmap);
                                arr[indexRestant] = 2;
                                animation = getAnimation(indexRestant % 3);
                                animation.setRepeatCount(Animation.INFINITE);
                                mCases[indexRestant].startAnimation(animation);
                                mCases[indexRestant].setClickable(false);;
                                winningGame();
                                return;
                            }
                            coupsPossibles.remove(Integer.valueOf(indexRestant));
                        }

                        for (int[] colonne : colonnes) {
                            // Même code que pour les lignes
                            int coupsJoueur = 0;
                            int coupsBot = 0;
                            int indexRestant = -1;
                            for (int e : colonne) {
                                if (arr[e] == 1) {
                                    coupsJoueur++;
                                } else if (arr[e] == 2) {
                                    coupsBot++;
                                } else {
                                    indexRestant = e;
                                }
                            }
                            if (coupsJoueur == 2 && coupsBot == 0) {
                                // Le joueur a deux coups alignés, le bot doit bloquer l'adversaire
                                Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                                mCases[indexRestant].setImageBitmap(oBitmap);
                                arr[indexRestant] = 2;
                                animation = getAnimation(indexRestant % 3);
                                animation.setRepeatCount(Animation.INFINITE);
                                mCases[indexRestant].startAnimation(animation);
                                mCases[indexRestant].setClickable(false);
                                winningGame();
                                return;
                            }
                            if (coupsBot == 2 && coupsJoueur == 0) {
                                // Le bot a deux coups alignés, le bot doit gagner
                                Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                                mCases[indexRestant].setImageBitmap(oBitmap);
                                arr[indexRestant] = 2;
                                animation = getAnimation(indexRestant % 3);
                                animation.setRepeatCount(Animation.INFINITE);
                                mCases[indexRestant].startAnimation(animation);
                                mCases[indexRestant].setClickable(false);
                                winningGame();
                                return;
                            }
                            coupsPossibles.remove(Integer.valueOf(indexRestant));
                        }

                        for (int[] diagonale : diagonales) {
                            // Même code que pour les lignes
                            int coupsJoueur = 0;
                            int coupsBot = 0;
                            int indexRestant = -1;
                            for (int e : diagonale) {
                                if (arr[e] == 1) {
                                    coupsJoueur++;
                                } else if (arr[e] == 2) {
                                    coupsBot++;
                                } else {
                                    indexRestant = e;
                                }
                            }
                            if (coupsJoueur == 2 && coupsBot == 0) {
                                // Le joueur a deux coups alignés, le bot doit bloquer l'adversaire
                                Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                                mCases[indexRestant].setImageBitmap(oBitmap);
                                arr[indexRestant] = 2;
                                animation = getAnimation(indexRestant % 3);
                                animation.setRepeatCount(Animation.INFINITE);
                                mCases[indexRestant].startAnimation(animation);
                                mCases[indexRestant].setClickable(false);
                                winningGame();
                                return;
                            }
                            if (coupsBot == 2 && coupsJoueur == 0) {
                                // Le bot a deux coups alignés, le bot doit gagner
                                Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                                mCases[indexRestant].setImageBitmap(oBitmap);
                                arr[indexRestant] = 2;
                                animation = getAnimation(indexRestant % 3);
                                animation.setRepeatCount(Animation.INFINITE);
                                mCases[indexRestant].startAnimation(animation);
                                mCases[indexRestant].setClickable(false);
                                winningGame();
                                return;
                            }
                            coupsPossibles.remove(Integer.valueOf(indexRestant));
                        }

// Si le joueur n'a pas deux coups alignés, le bot joue un coup aléatoire parmi les coups possibles
                        if (!coupsPossibles.isEmpty()) {
                            int index = new Random().nextInt(coupsPossibles.size());
                            int coup = coupsPossibles.get(index);
                            Bitmap oBitmap = createOBitmap(100, 100, 40, Color.RED);
                            // Vérifier si le bot peut bloquer l'adversaire
                            for (int[] ligne : lignes) {
                                int count = 0;
                                int caseIndex = -1;
                                for (int k = 0; k < 3; k++) {
                                    int indexLigne = ligne[k];
                                    if (arr[indexLigne] == 1) {
                                        count++;
                                    } else if (arr[indexLigne] == 0) {
                                        caseIndex = indexLigne;
                                    }
                                }
                                if (count == 2 && caseIndex != -1) {
                                    arr[caseIndex] = 2;
                                    mCases[caseIndex].setImageBitmap(oBitmap);
                                    animation = getAnimation(caseIndex % 3);
                                    animation.setRepeatCount(Animation.INFINITE);
                                    mCases[caseIndex].startAnimation(animation);
                                    mCases[caseIndex].setClickable(false);
                                    winningGame();
                                    return;
                                }
                            }

// Jouer un coup aléatoire
                            mCases[coup].setImageBitmap(oBitmap);
                            arr[coup] = 2;
                            animation = getAnimation(coup % 3);
                            animation.setRepeatCount(Animation.INFINITE);
                            mCases[coup].startAnimation(animation);
                            mCases[coup].setClickable(false);
                            winningGame();
                        }

                    }

                }

                winningGame();
            }
        }
    }

    private Animation getAnimation(int animationType) {
        switch (animationType) {
            case 0:
                return AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
            case 1:
                return AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
            case 2:
                return AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            case 3:
                return AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
            default:
                return null;
        }
    }


}