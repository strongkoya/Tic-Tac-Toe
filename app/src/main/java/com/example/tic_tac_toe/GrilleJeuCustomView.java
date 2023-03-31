package com.example.tic_tac_toe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


        import android.content.Context;
        import android.util.AttributeSet;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.Toast;

public class GrilleJeuCustomView extends RelativeLayout  {

    private ImageView[] mCases;
    private RelativeLayout[] winnerCrossedLines;

    public GrilleJeuCustomView(Context context) {
        super(context);
        init();
    }

    public GrilleJeuCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GrilleJeuCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.grille_jeu, this);

        mCases = new ImageView[9];
        mCases[0] = findViewById(R.id.btnn1);
        mCases[1] = findViewById(R.id.btn2);
        mCases[2] = findViewById(R.id.btn3);
        mCases[3] = findViewById(R.id.btn4);
        mCases[4] = findViewById(R.id.btn5);
        mCases[5] = findViewById(R.id.btn6);
        mCases[6] = findViewById(R.id.btn7);
        mCases[7] = findViewById(R.id.btn8);
        mCases[8] = findViewById(R.id.btn9);

        winnerCrossedLines = new RelativeLayout[9];

        winnerCrossedLines[0] = findViewById(R.id.winnerLine0);
        winnerCrossedLines[1] = findViewById(R.id.winnerLine1);
        winnerCrossedLines[2] = findViewById(R.id.winnerLine2);
        winnerCrossedLines[3] = findViewById(R.id.winnerLine3);
        winnerCrossedLines[4] = findViewById(R.id.winnerLine4);
        winnerCrossedLines[5] = findViewById(R.id.winnerLine5);
        winnerCrossedLines[6] = findViewById(R.id.winnerLine6);
        winnerCrossedLines[7] = findViewById(R.id.winnerLine7);


    }



    public ImageView[] getmCases() {
        return mCases;
    }
    
    public RelativeLayout[] getWinningLine() {return winnerCrossedLines;}
}

