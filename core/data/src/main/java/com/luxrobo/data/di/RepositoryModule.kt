package com.luxrobo.data.di

import com.luxrobo.data.datasource.BleDeviceConnectionDataSource
import com.luxrobo.data.datasource.BleDeviceConnectionDataSourceImpl
import com.luxrobo.data.repository.BleDeviceConnectionRepository
import com.luxrobo.data.repository.BleDeviceConnectionRepositoryImpl
import com.luxrobo.data.repository.BleTransferRepository
import com.luxrobo.data.repository.BleTransferRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideBleDeviceConnectionRepository(
        dataSource: BleDeviceConnectionRepositoryImpl,
    ): BleDeviceConnectionRepository

    @Binds
    @Singleton
    abstract fun provideBleTransferRepository(
        dataSource: BleTransferRepositoryImpl,
    ): BleTransferRepository
}