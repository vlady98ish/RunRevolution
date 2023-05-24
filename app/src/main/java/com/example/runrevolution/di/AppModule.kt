package com.example.runrevolution.di

import android.content.Context
import androidx.room.Room
import com.example.runrevolution.data.db.LocationDAO
import com.example.runrevolution.data.db.RunDatabase
import com.example.runrevolution.data.repository.LocationRepositoryImpl
import com.example.runrevolution.domain.repository.LocationRepository
import com.example.runrevolution.utils.other.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideRoomDatabase(
        @ApplicationContext app: Context,

        ) = Room.databaseBuilder(
        app,
        RunDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }


    @Provides
    @Singleton
    fun provideContactRepository(locationDAO: LocationDAO): LocationRepository {
        return LocationRepositoryImpl(locationDAO)
    }


    @Provides
    fun provideLocationDao(database: RunDatabase): LocationDAO {
        return database.getLocationDao()
    }

}