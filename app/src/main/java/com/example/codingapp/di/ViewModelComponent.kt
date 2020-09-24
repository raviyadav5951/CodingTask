package com.example.codingapp.di

import com.example.codingapp.ui.main.MainViewModel
import dagger.Component

@Component(modules = [ApiModule::class,AppModule::class])
interface ViewModelComponent {
    //location where we will inject this
    fun inject(viewModel:MainViewModel)
}