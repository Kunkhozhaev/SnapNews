package ru.nurdaulet.news.app

import android.app.Application
import ru.nurdaulet.news.di.DaggerApplicationComponent

class NewsApplication : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}