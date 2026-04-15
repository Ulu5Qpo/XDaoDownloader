package com.example.xddemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class XDaoApplicationInstrumentedTest {

    @Test
    fun application_initializesDependencies() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val application = appContext.applicationContext as XDaoApplication

        assertEquals("com.c137.chaDao", appContext.packageName)
        assertNotNull(application.userPreferencesRepository)
        assertNotNull(application.container)
        assertNotNull(application.container.cookieInterceptor)
        assertNotNull(application.container.threadRepository)
    }
}
