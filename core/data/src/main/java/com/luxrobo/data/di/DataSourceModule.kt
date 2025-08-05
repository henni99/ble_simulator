package com.luxrobo.data.di

import com.luxrobo.data.datasourceImpl.BleDeviceConnectionDataSource
import com.luxrobo.data.datasourceImpl.BleDeviceConnectionDataSourceImpl
import com.luxrobo.data.datasourceImpl.BleTransferDataSource
import com.luxrobo.data.datasourceImpl.BleTransferDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun provideBleConnectionDataSource(
        dataSource: BleDeviceConnectionDataSourceImpl,
    ): BleDeviceConnectionDataSource

    @Binds
    @Singleton
    abstract fun provideBleTransferDataSource(
        dataSource: BleTransferDataSourceImpl,
    ): BleTransferDataSource

}