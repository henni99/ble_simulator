package com.luxrobo.ble_simulator

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.luxrobo.testing.ScreenshotTests
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.luxrobo.device_list.presentation.BleDeviceListActivity
import com.luxrobo.testing.BleSimulatorAppRobot
import com.luxrobo.testing.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
@Category(ScreenshotTests::class)
class BleDeviceListActivityTest {

    @get:Rule
    @BindValue
    val robotTestRule: RobotTestRule = RobotTestRule<BleDeviceListActivity>(this)

    @Inject
    lateinit var AppRobot: BleSimulatorAppRobot

    @Test
    fun `기본 환경에서의 앱 실행 스크린샷 테스트`() {
        AppRobot {
            capture()
        }
    }

    @Test
    @Config(qualifiers = RobolectricDeviceQualifiers.MediumTablet)
    fun `중간 크기의 태블릿 앱 실행 스크린샷 테스트`() {
        AppRobot {
            capture()
        }
    }
}