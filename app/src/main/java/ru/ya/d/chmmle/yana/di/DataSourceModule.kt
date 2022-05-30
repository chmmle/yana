@file:Suppress("unused")

package ru.ya.d.chmmle.yana.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ya.d.chmmle.yana.data.source.INotesDataSource
import ru.ya.d.chmmle.yana.data.source.local.NotesLocalDataSource
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(impl: NotesLocalDataSource): INotesDataSource
}