package com.luxrobo.data.di

import com.luxrobo.domain.repository.BleDeviceConnectionRepository
import com.luxrobo.data.repositoryImpl.BleDeviceConnectionRepositoryImpl
import com.luxrobo.domain.repository.BleTransferRepository
import com.luxrobo.data.repositoryImpl.BleTransferRepositoryImpl
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