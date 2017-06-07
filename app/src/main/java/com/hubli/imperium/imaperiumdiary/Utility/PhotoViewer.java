package com.hubli.imperium.imaperiumdiary.Utility;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.hubli.imperium.imaperiumdiary.R;
import com.squareup.picasso.Picasso;

public class PhotoViewer extends AppCompatActivity {

    public static final String IMG_URL = "image_url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(IMG_URL);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_photo_viewer);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        String path = getIntent().getStringExtra(IMG_URL);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        Picasso.with(getApplicationContext()).load(path).into(photoView);
    }
}
