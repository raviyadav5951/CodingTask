package com.example.codingapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(image: ImageEntity)

    @Query("SELECT * FROM image_table WHERE image_id = :imageId")
    fun findByImageId(imageId: String): ImageEntity
}