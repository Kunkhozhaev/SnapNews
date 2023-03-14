package ru.nurdaulet.news.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.nurdaulet.news.ui.NewsActivity

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: NewsActivity)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}