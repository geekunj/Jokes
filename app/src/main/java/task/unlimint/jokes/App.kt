package task.unlimint.jokes

import android.app.Application
import android.content.ContextWrapper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import task.unlimint.jokes.core.util.Prefs
import task.unlimint.jokes.di.appModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule))
        }

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(BuildConfig.APPLICATION_ID)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}