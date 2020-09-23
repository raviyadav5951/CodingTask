package com.example.codingapp.ui.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.codingapp.R;
import com.example.codingapp.databinding.ActivityImageDetailBinding;
import com.example.codingapp.db.DbRepository;
import com.example.codingapp.db.ImageEntity;

import java.util.concurrent.ExecutionException;

public class ImageDetailActivity extends AppCompatActivity {

    private ActivityImageDetailBinding detailBinding;
    private String imageId;
    private String imageLink;
    private String imageTitle;

    //Database Operations
    private DbRepository dbRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = ActivityImageDetailBinding.inflate(getLayoutInflater());
        View view = detailBinding.getRoot();
        setContentView(view);

        initActionBarAndReceiveArgs();
        loadDetailImage();
        initDatabase();
        fetchComment();
        detailBinding.btnSubmit.setOnClickListener(submitClickListener);
    }

    /**
     * Fetch comment if it exist in local db
     */
    private void fetchComment() {
        try {
            ImageEntity imageEntity=dbRepository.getCommentOfImage(imageId);
            if(imageEntity!=null && !TextUtils.isEmpty(imageEntity.getComment())){
                detailBinding.etComment.setText(imageEntity.getComment());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Init local database
     */

    private void initDatabase() {
        dbRepository=new DbRepository(getApplication());
    }

    /**
     * Load Full image based on the url received from {@link com.example.codingapp.ui.main.MainActivity}
     */
    private void loadDetailImage() {
        //To Load full image
        Glide.with(this)
                .setDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder))
                .load(imageLink)
                .into(detailBinding.imgurDetailImage);
    }

    /**
     * Receive the bundle arguments from {@link com.example.codingapp.ui.main.MainActivity}
     */
    private void initActionBarAndReceiveArgs() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (getIntent().getExtras() != null) {
                imageTitle = getIntent().getStringExtra("image_title");
                imageId = getIntent().getStringExtra("image_id");
                imageLink = getIntent().getStringExtra("image_link");
                getSupportActionBar().setTitle(imageTitle);
            }
        }
    }

    /**
     * Custom click listener for submit button
     */
    private View.OnClickListener submitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(detailBinding.etComment.getText().toString().trim())) {
                Toast.makeText(ImageDetailActivity.this,
                        getString(R.string.empty_comment_message), Toast.LENGTH_SHORT).show();
            } else {
                ImageEntity imageEntity=new ImageEntity(imageId,detailBinding.etComment.getText().toString().trim());
                dbRepository.insert(imageEntity);
                Toast.makeText(ImageDetailActivity.this, getString(R.string.record_insert_message), Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Custom progress drawable to show loader.
     */
    private CircularProgressDrawable getProgressDrawable() {
        CircularProgressDrawable progressDrawable = new CircularProgressDrawable(this);
        progressDrawable.setStrokeWidth(10.0f);
        progressDrawable.setCenterRadius(10.0f);
        return progressDrawable;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
