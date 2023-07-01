package com.example.runrevolution.di

import android.content.Context
import androidx.room.Room
import com.example.runrevolution.data.db.LocationDAO
import com.example.runrevolution.data.db.RunDatabase
import com.example.runrevolution.data.db.RunDetailDAO
import com.example.runrevolution.data.repository.LocationRepositoryImpl
import com.example.runrevolution.data.repository.RunDetailsRepositoryImpl
import com.example.runrevolution.domain.repository.LocationRepository
import com.example.runrevolution.domain.repository.RunDetailsRepository
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
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }


    @Provides
    @Singleton
    fun provideLocationRepository(locationDAO: LocationDAO): LocationRepository {
        return LocationRepositoryImpl(locationDAO)
    }

    @Provides
    @Singleton
    fun provideRunDetailsRepository(runDetailDAO: RunDetailDAO): RunDetailsRepository {
        return RunDetailsRepositoryImpl(runDetailDAO)
    }


    @Provides
    fun provideLocationDao(database: RunDatabase): LocationDAO {
        return database.getLocationDao()
    }

    @Provides
    fun provideRunDetailsDao(database: RunDatabase): RunDetailDAO {
        return database.getRunDetailsDao()
    }

}