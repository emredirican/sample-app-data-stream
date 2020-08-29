package me.emredirican.datastream.domain

import android.content.Context
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.ObservableTransformer

@Module
@InstallIn(FragmentComponent::class)
object DataSourceFactoryModule {

  @Provides
  @FragmentScoped
  fun provideDataSourceFactory(
      @ApplicationContext context: Context,
      gson: Gson
  ): DataSourceFactory {
    return DataSourceFactory(context, gson)
  }
}

@Module
@InstallIn(FragmentComponent::class)
abstract class GetDataModule {

  @Binds
  @FragmentScoped
  abstract fun provideGetDataUseCase(
      impl: GetDataUseCase): ObservableTransformer<GetDataAction, DataResult>
}

@Module
@InstallIn(FragmentComponent::class)
abstract class FilterModule {

  @Binds
  @FragmentScoped
  abstract fun provideFilterUseCase(
      impl: FilterUseCase): ObservableTransformer<FilterAction, FilterResult>
}
