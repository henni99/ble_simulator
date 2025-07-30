package com.luxrobo.data.di

import com.luxrobo.data.datasource.BleDeviceConnectionDataSource
import com.luxrobo.data.datasource.BleDeviceConnectionDataSourceImpl
import com.luxrobo.data.datasource.BleTransferDataSource
import com.luxrobo.data.datasource.BleTransferDataSourceImpl
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