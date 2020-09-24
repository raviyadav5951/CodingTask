package com.example.codingapp.di


import com.example.codingapp.network.ImgurApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    //AnimalApiService determines where this will be injected
    fun inject(service:ImgurApiService)
}