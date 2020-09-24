package com.example.codingapp.network

import com.example.codingapp.di.DaggerApiComponent
import com.example.codingapp.model.ImageResponse
import io.reactivex.Single
import javax.inject.Inject

/**
 * This class can also be termed as Repository in MVVM terminology
 */

class ImgurApiService {
    init {
        DaggerApiComponent.create().inject(this)
    }

    @Inject
    lateinit var api:ImgurApi

    fun getImages(searchInput: String?, pageNumber: Int = 1): Single<ImageResponse> =
        api.getImagesFromImgur(pageNumber = pageNumber, searchInput = searchInput)

}