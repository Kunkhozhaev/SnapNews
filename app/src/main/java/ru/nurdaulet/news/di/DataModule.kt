package ru.nurdaulet.news.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.nurdaulet.news.data.NewsRepositoryImpl
import ru.nurdaulet.news.data.shared_pref.SharedPref
import ru.nurdaulet.news.data.shared_pref.SharedPrefImpl
import ru.nurdaulet.news.domain.repository.NewsRepository

@Module
interface DataModule {

    @Binds
    fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    fun bindSharedPref(impl: SharedPrefImpl): SharedPref

    @Binds
    fun bindContext(application: Application): Context

    companion object {
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        @Provides
        fun provideFirebaseDatabase(): FirebaseFirestore {
            return FirebaseFirestore.getInstance()
        }

        @Provides
        fun provideFirebaseStorage(): FirebaseStorage {
            return FirebaseStorage.getInstance()
        }
    }
}