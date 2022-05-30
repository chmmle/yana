package ru.ya.d.chmmle.yana.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ya.d.chmmle.yana.data.source.local.NotesDao
import ru.ya.d.chmmle.yana.data.source.local.YanaDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideNotesDao(database: YanaDatabase): NotesDao {
        return database.notesDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): YanaDatabase {
        return Room.databaseBuilder(
            appContext,
            YanaDatabase::class.java,
            "NotesDb"
        )
            .build()
    }
}