package com.example.runrevolution.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.runrevolution.data.db.LocationDAO
import com.example.runrevolution.data.db.RunDatabase
import com.example.runrevolution.data.db.RunDetailDAO
import com.example.runrevolution.data.repository.LocationRepositoryImpl
import com.example.runrevolution.data.repository.RunDetailsRepositoryImpl
import com.example.runrevolution.domain.repository.LocationRepository
import com.example.runrevolution.domain.repository.RunDetailsRepository
import com.example.runrevolution.utils.other.Constant.DATABASE_NAME
import com.example.runrevolution.utils.other.Constant.KEY_FIRST_TIME
import com.example.runrevolution.utils.other.Constant.KEY_HEIGHT
import com.example.runrevolution.utils.other.Constant.KEY_NAME
import com.example.runrevolution.utils.other.Constant.KEY_WEIGHT
import com.example.runrevolution.utils.other.Constant.SHARED_PREFERENCES_NAME
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

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }


    @Singleton
    @Provides
    fun providesName(sharedPreferences: SharedPreferences) =
        sharedPreferences.getString(KEY_NAME, "") ?: ""


    @Singleton
    @Provides
    fun providesWeight(sharedPreferences: SharedPreferences) = sharedPreferences.getFloat(
        KEY_WEIGHT, 70f
    )


    @Singleton
    @Provides
    fun provideIsFirsTime(sharedPreferences: SharedPreferences) = sharedPreferences.getBoolean(
        KEY_FIRST_TIME, true
    )

}