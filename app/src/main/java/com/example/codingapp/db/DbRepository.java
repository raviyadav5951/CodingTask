package com.example.codingapp.db;

import android.app.Application;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

public class DbRepository {

    private ImageDao imageDao;

    public DbRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        imageDao = db.imageRecordDao();
    }

    //Method to insert in the database
    public void insert(ImageEntity imageRecord) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            imageDao.insertRecord(imageRecord);
        });
    }

    //Method to fetch the comment if it exist
    public ImageEntity getCommentOfImage(String imageId) throws ExecutionException, InterruptedException{
        Callable<ImageEntity> callable = () -> imageDao.findByImageId(imageId);

        Future<ImageEntity> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }
}
