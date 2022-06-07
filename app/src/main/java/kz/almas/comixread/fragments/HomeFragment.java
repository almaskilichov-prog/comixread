package kz.almas.comixread.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

import kz.almas.comixread.R;
import kz.almas.comixread.activities.ComicInfoActivity;
import kz.almas.comixread.classes.DataConverter;
import kz.almas.comixread.classes.ObservableScrollView;
import kz.almas.comixread.interfaces.ScrollViewListener;

public class HomeFragment extends Fragment {


    // HorizontalScrollView
    private ObservableScrollView updatedPopularMangaPoster = null;
    private ObservableScrollView updatedPopularMangaTitles = null;

    View view;

    // Firebase
    private FirebaseDatabase DB;
    private DatabaseReference comicRecommendations;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // присваивание бд (Firebase) к переменной
        DB = FirebaseDatabase.getInstance();
        comicRecommendations = DB.getReference().child("comicRecommendations");

        // синхронизация двух HorizontalScrollView
        updatedPopularMangaPoster = view.findViewById(R.id.updatedPopularMangaPoster);
        updatedPopularMangaPoster.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                updatedPopularMangaTitles.scrollTo(x, y);
            }
        });
        updatedPopularMangaTitles = view.findViewById(R.id.updatedPopularMangaTitles);
        updatedPopularMangaTitles.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                updatedPopularMangaPoster.scrollTo(x, y);
            }
        });


        LinearLayout linearTitles = view.findViewById(R.id.linearTitles);
        ArrayList<TextView> textViewTitles = new ArrayList<>();

        for (int i = 0; i < 6; i++) {

            TextView textViewTitle = new TextView(getContext());

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMarginStart(getPX(13));
            layoutParams.width = getPX(100);

            textViewTitle.setLayoutParams(layoutParams);
            textViewTitle.setTextSize(15);
            textViewTitle.setText("Тройной брак");
            textViewTitle.setId(View.generateViewId());

            textViewTitles.add(textViewTitle);
            linearTitles.addView(textViewTitle);
        }

        LinearLayout linearPoster = view.findViewById((R.id.linearPoster));

        ArrayList<ImageView> imageViewPosters = new ArrayList<>();

        for (int i = 0; i < 6; i++) {

            ImageView imageViewPoster = new ImageView(getContext());
            imageViewPoster.setImageResource(R.drawable.test);
            imageViewPosters.add(imageViewPoster);
            imageViewPoster.setId(View.generateViewId());

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(getPX(10),0,getPX(5),0);
            layoutParams.width = getPX(99);

            imageViewPoster.setLayoutParams(layoutParams);

            linearPoster.addView(imageViewPoster);
        }


        TextView textView9 = view.findViewById(R.id.textView9);
        Thread runOnClickTitles = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < textViewTitles.size(); i++) {
                    int finalI = i;
                    textViewTitles.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), ComicInfoActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                while(true){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        runOnClickTitles.start();
//            for (int i = 0; i < textViewTitles.size(); i++) {
//                int finalI = i;
//                textViewTitles.get(i).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(getContext(), finalI, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }


        return view;





    }

    public int getPX(int dp) {
        Resources r =  view.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }


}