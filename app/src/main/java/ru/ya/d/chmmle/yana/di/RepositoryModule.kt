package ru.ya.d.chmmle.yana.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ya.d.chmmle.yana.data.source.DefaultNotesRepository
import ru.ya.d.chmmle.yana.data.source.INotesRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(impl: DefaultNotesRepository) : INotesRepository
}