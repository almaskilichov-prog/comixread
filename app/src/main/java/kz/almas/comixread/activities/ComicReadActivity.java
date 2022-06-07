package kz.almas.comixread.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;

import kz.almas.comixread.R;

public class ComicReadActivity extends AppCompatActivity {

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_read);

        webView = findViewById(R.id.webView);


        // установка черного цвета фона
        webView.setBackgroundColor(getColor(R.color.black));

        // включаем поддержку масштабирования
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        // больше места для нашей картинки
        webView.setPadding(0, 0, 0, 0);

        // полосы прокрутки – внутри изображения, увеличение места для просмотра
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // webView на весь экран с нормальным размером
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        // убираем кнопки зума, но оставляем возможность масштабирования
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        String test = "https://img3.cdnlib.link//manga/kkumbakkogjil/chapters/1569837/1_Iybd.jpg";

        webView.loadUrl(test);


    }



}