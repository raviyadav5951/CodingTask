package com.example.codingapp.ui.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.codingapp.R;
import com.example.codingapp.databinding.ActivityImageDetailBinding;

public class ImageDetailActivity extends AppCompatActivity {

    private ActivityImageDetailBinding detailBinding;
    private String imageId;
    private String imageLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = ActivityImageDetailBinding.inflate(getLayoutInflater());
        View view = detailBinding.getRoot();
        setContentView(view);
        initActionBarAndReceiveArgs();

        detailBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(detailBinding.etComment.getText().toString().trim())) {
                    Toast.makeText(ImageDetailActivity.this, "Please enter text to save the comment.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ok", "ok");
                }
            }
        });

        //To Load full image
        Glide.with(this)
                .setDefaultRequestOptions(new RequestOptions()
                        .placeholder(getProgressDrawable())
                        .error(R.drawable.placeholder))
                .load(imageLink)
                .into(detailBinding.imgurDetailImage);
    }


    private void initActionBarAndReceiveArgs() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (getIntent().getExtras() != null) {
                getSupportActionBar().setTitle(getIntent().getStringExtra("image_title"));
                imageId = getIntent().getStringExtra("image_id");
                imageLink = getIntent().getStringExtra("image_link");
            }
        }
    }

    private CircularProgressDrawable getProgressDrawable(){
        CircularProgressDrawable progressDrawable=new CircularProgressDrawable(this);
        progressDrawable.setStrokeWidth(10.0f);
        progressDrawable.setCenterRadius(10.0f);
        return  progressDrawable;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
