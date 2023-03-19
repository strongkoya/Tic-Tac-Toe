package com.example.tic_tac_toe;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    String StartGame = "X";
    int arr[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int i;
    int countX,countO;

    TextView txt1,txt2;
    Button btnreset;
    ImageView[] mCases;

//x=1
//y=2

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



        btnreset=(Button)findViewById(R.id.btnreset);
        txt1=(TextView)findViewById(R.id.txt1);
        txt2=(TextView)findViewById(R.id.txt2);


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
                countX=0;
                countO=0;
                txt1.setText("Player X: "+String.valueOf(countX));
                txt2.setText("Player O: "+String.valueOf(countO));

            }
        });


    }



    public void choosePalyer() {
        if (StartGame.equals("X")) {
            StartGame = "O";
        } else {
            StartGame = "X";
        }
    }

    public void winningGame() {
        if ((arr[0] == 1 && arr[1] == 1 && arr[2] == 1) || (arr[3] == 1 && arr[4] == 1 && arr[5] == 1) || (arr[6] == 1 && arr[7] == 1 && arr[8] == 1) || (arr[0] == 1 && arr[3] == 1 && arr[6] == 1) || (arr[1] == 1 && arr[4] == 1 && arr[7] == 1) || (arr[2] == 1 && arr[5] == 1 && arr[8] == 1) || (arr[0] == 1 && arr[4] == 1 && arr[8] == 1) || (arr[2] == 1 && arr[4] == 1 && arr[6] == 1)) {

            AlertFragment alertFragment = new AlertFragment("Player X Wins", 1);
            alertFragment.setPositiveButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Code à exécuter lorsque le bouton positif est cliqué
                    
                    resetGame();
                }
            });
            // Affichage de la fenêtre de dialogue
            FragmentManager fragmentManager = getSupportFragmentManager();

            alertFragment.setCancelable(false);
            alertFragment.show(fragmentManager, "AlertFragment");

            countX++;
            txt1.setText("Player X: "+String.valueOf(countX));

        } else if ((arr[0] == 2 && arr[1] == 2 && arr[2] == 2) || (arr[3] == 2 && arr[4] == 2 && arr[5] == 2) || (arr[6] == 2 && arr[7] == 2 && arr[8] == 2) || (arr[0] == 2 && arr[3] == 2 && arr[6] == 2) || (arr[1] == 2 && arr[4] == 2 && arr[7] == 2) || (arr[2] == 2 && arr[5] == 2 && arr[8] == 2) || (arr[0] == 2 && arr[4] == 2 && arr[8] == 2) || (arr[2] == 2 && arr[4] == 2 && arr[6] == 2)) {

            AlertFragment alertFragment = new AlertFragment("Player O Wins", 2);
            alertFragment.setPositiveButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Code à exécuter lorsque le bouton positif est cliqué
                    
                    resetGame();
                }
            });
            // Affichage de la fenêtre de dialogue
            FragmentManager fragmentManager = getSupportFragmentManager();
            alertFragment.setCancelable(false);
            alertFragment.show(fragmentManager, "AlertFragment");

            countO++;
            txt2.setText("Player O: "+String.valueOf(countO));
        }
        else if ((arr[0] != 0 && arr[1] != 0 && arr[2] != 0 && arr[3] != 0 && arr[4] != 0 && arr[5] != 0 && arr[6] != 0 && arr[7] != 0 && arr[8] != 0)){
            AlertFragment alertFragment = new AlertFragment("The Game is Draw !!!", 1);
            alertFragment.setPositiveButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Code à exécuter lorsque le bouton positif est cliqué
                    
                    resetGame();
                }
            });
            // Affichage de la fenêtre de dialogue
            FragmentManager fragmentManager = getSupportFragmentManager();
            alertFragment.setCancelable(false);
            alertFragment.show(fragmentManager, "AlertFragment");
        }
    }

    public void resetGame(){
        for(int i=0;i<arr.length;i++){
            arr[i]=0;
        }
        for (int j = 0; j < 9; j++) {

            mCases[j].setImageDrawable(null);
            mCases[j].setClickable(true);

        }
    }

    @Override
    public void onClick(View view) {
      /*  ImageView caseCliquee = (ImageView) view;
        int caseCliqueeIndex = -1;
        for (int i = 0; i < 9; i++) {
            if (mCases[i] == caseCliquee) {
                Toast.makeText(getApplicationContext(),"helllo"+i,Toast.LENGTH_LONG).show();
                caseCliqueeIndex = i;
                break;
            }
        }

        if (caseCliqueeIndex != -1) {
            if (mCurrentPlayer == 1) {
                caseCliquee.setImageResource(mImages[1]);
                mCurrentPlayer = 2;
            } else {
                caseCliquee.setImageResource(mImages[2]);
                mCurrentPlayer = 1;
            }
        }*/
        for (int i = 0; i < 9; i++) {
            mCases[i].clearAnimation(); // Stopper l'animation en cours sur chaque ImageView
        }
        for (int i = 0; i < 9; i++) {
            if(mCases[i]==view){
                if (StartGame.equals("X")) {
                    Toast.makeText(getApplicationContext(),"helllo"+i,Toast.LENGTH_LONG).show();

                    Bitmap xBitmap = createXBitmap(100, 100, Color.WHITE);
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
                    Bitmap oBitmap = createOBitmap(100, 100,40, Color.WHITE);
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

            }}
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
    public static Bitmap createXBitmap(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(10);

        // Dessiner la première ligne diagonale
        canvas.drawLine(0, 0, width, height, paint);

        // Dessiner la deuxième ligne diagonale
        canvas.drawLine(width, 0, 0, height, paint);

        return bitmap;
    }
    public static Bitmap createOBitmap(int width, int height,int radius,  int color) {
        // Créer un Bitmap de taille 100x100
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
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        return bitmap;
    }

}