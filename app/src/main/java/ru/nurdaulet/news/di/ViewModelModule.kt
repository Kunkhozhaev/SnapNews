package ru.nurdaulet.news.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nurdaulet.news.ui.fragments.article.ArticleViewModel
import ru.nurdaulet.news.ui.fragments.auth.login.LoginViewModel
import ru.nurdaulet.news.ui.fragments.auth.signup.SignUpViewModel
import ru.nurdaulet.news.ui.fragments.breaking.BreakingNewsViewModel
import ru.nurdaulet.news.ui.fragments.profile.EditProfileViewModel
import ru.nurdaulet.news.ui.fragments.profile.ProfileViewModel
import ru.nurdaulet.news.ui.fragments.saved_articles.SavedNewsViewModel
import ru.nurdaulet.news.ui.fragments.search.SearchNewsViewModel

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(ArticleViewModel::class)
    @Binds
    fun bindArticleViewModel(impl: ArticleViewModel): ViewModel

    @IntoMap
    @ViewModelKey(BreakingNewsViewModel::class)
    @Binds
    fun bindBreakingNewsViewModel(impl: BreakingNewsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SavedNewsViewModel::class)
    @Binds
    fun bindSavedNewsViewModel(impl: SavedNewsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SearchNewsViewModel::class)
    @Binds
    fun bindSearchNewsViewModel(impl: SearchNewsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    @Binds
    fun bindLoginViewModel(impl: LoginViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    @Binds
    fun bindSignUpViewModel(impl: SignUpViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    @Binds
    fun bindProfileViewModel(impl: ProfileViewModel): ViewModel

    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    @Binds
    fun bindEditProfileViewModel(impl: EditProfileViewModel): ViewModel
}