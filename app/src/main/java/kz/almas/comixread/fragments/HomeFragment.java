package kz.almas.comixread.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import kz.almas.comixread.R;
import kz.almas.comixread.activities.ComicInfoActivity;
import kz.almas.comixread.classes.ComicRecommendations;
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

    AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // присваивание бд (Firebase) к переменной
        DB = FirebaseDatabase.getInstance();
        comicRecommendations = DB.getReference().child("comicRecommendations");

        startLoadingDialog();

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



        LinearLayout linearPoster = view.findViewById((R.id.linearPoster));

        ArrayList<ImageView> imageViewPosters = new ArrayList<>();
        ArrayList<String> posterBase64 = new ArrayList<>();
        Context context = getContext();



        Query query = comicRecommendations.orderByChild("posterBase64");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    ComicRecommendations comicRecommendations = data.getValue(ComicRecommendations.class);

                    posterBase64.add(comicRecommendations.posterBase64);
                    // постеры
                        ImageView imageViewPoster = new ImageView(context);
                        imageViewPoster.setImageBitmap(getImage(comicRecommendations.posterBase64));
                        imageViewPosters.add(imageViewPoster);
                        imageViewPoster.setId(View.generateViewId());

                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(getPX(10),0,getPX(5),0);
                        layoutParams.width = getPX(99);

                        imageViewPoster.setLayoutParams(layoutParams);
                        imageViewPoster.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        linearPoster.addView(imageViewPoster);


                    // заголовки

                        TextView textViewTitle = new TextView(context);

                        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        layoutParams1.setMarginStart(getPX(13));
                        layoutParams1.width = getPX(100);

                        textViewTitle.setLayoutParams(layoutParams1);
                        textViewTitle.setTextSize(15);
                        textViewTitle.setText(comicRecommendations.title);
                        textViewTitle.setId(View.generateViewId());

                        textViewTitles.add(textViewTitle);
                        linearTitles.addView(textViewTitle);



                }
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Thread runOnClickTitles = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
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
                        Thread.sleep(1);

                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        runOnClickTitles.start();
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


    public Bitmap getImage(String poster64) {
        byte[] decodedString = Base64.decode(poster64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
}