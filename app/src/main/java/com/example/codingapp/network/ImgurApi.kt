package com.example.codingapp.network

import com.example.codingapp.model.ImageResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ImgurApi {

    @GET("/3/gallery/search/1")
    fun getImagesFromImgur(@Query("q") searchInput:String?):Single<ImageResponse>

}