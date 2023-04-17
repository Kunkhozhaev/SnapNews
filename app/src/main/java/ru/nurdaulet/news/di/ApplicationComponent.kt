package ru.nurdaulet.news.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.nurdaulet.news.ui.fragments.article.ArticleFragment
import ru.nurdaulet.news.ui.fragments.auth.login.LoginFragment
import ru.nurdaulet.news.ui.fragments.auth.signup.SignUpFragment
import ru.nurdaulet.news.ui.fragments.breaking.BreakingNewsFragment
import ru.nurdaulet.news.ui.fragments.profile.EditProfileFragment
import ru.nurdaulet.news.ui.fragments.profile.ProfileFragment
import ru.nurdaulet.news.ui.fragments.saved_articles.SavedNewsFragment
import ru.nurdaulet.news.ui.fragments.search.SearchNewsFragment
import ru.nurdaulet.news.ui.splash_screen.SplashScreen

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(fragment: BreakingNewsFragment)
    fun inject(fragment: ArticleFragment)
    fun inject(fragment: SavedNewsFragment)
    fun inject(fragment: SearchNewsFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: SignUpFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: EditProfileFragment)

    fun inject(fragment: SplashScreen)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}