package com.example.codingapp

import com.example.codingapp.di.ApiModule
import com.example.codingapp.network.ImgurApiService

class ApiModuleTest (val mockApiModule:ImgurApiService) : ApiModule(){
    override fun provideImgurApiService(): ImgurApiService {
        return mockApiModule
    }
}