package me.emredirican.datastream

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SchedulersModule {

  @Provides
  @Main
  fun provideMainScheduler(): Scheduler {
    return AndroidSchedulers.mainThread()
  }

  @Provides
  @IO
  fun provideIOScheduler(): Scheduler {
    return Schedulers.io()
  }
}

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {

  @Provides
  @Singleton
  fun provideGson(): Gson {
    return Gson()
  }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Main

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IO

