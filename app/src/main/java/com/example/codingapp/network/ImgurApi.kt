package com.example.codingapp.network

import com.example.codingapp.model.ImageResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImgurApi {

    @GET("/3/gallery/search/{page_number}")
    fun getImagesFromImgur(@Path("page_number") pageNumber:Int=1, @Query("q") searchInput:String?):Single<ImageResponse>

}