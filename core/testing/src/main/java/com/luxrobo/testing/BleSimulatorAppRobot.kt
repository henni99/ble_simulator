package com.luxrobo.testing

import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.test.TestDispatcher
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class BleSimulatorAppRobot @Inject constructor(
    private val testDispatcher: TestDispatcher,
) {

    @Inject
    lateinit var robotTestRule: RobotTestRule

    private lateinit var composeTestRule: AndroidComposeTestRule<*, *>
    operator fun invoke(
        block: BleSimulatorAppRobot.() -> Unit,
    ) {
        runTestWithLogging(timeout = 30.seconds) {
            this@BleSimulatorAppRobot.composeTestRule = robotTestRule.composeTestRule
            waitUntilIdle()
            block()
        }
    }

    fun capture() {
        composeTestRule
            .onNode(isRoot())
            .captureRoboImage()
    }

    fun waitUntilIdle() {
        composeTestRule.waitForIdle()
        testDispatcher.scheduler.advanceUntilIdle()
    }
}
