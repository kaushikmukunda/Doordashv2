package km.com.doordash

import android.app.Application
import com.bumptech.glide.GlideBuilder
import km.com.doordash.common.di.AppComponent
import km.com.doordash.common.di.DaggerAppComponent

class MyApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        appComponent = DaggerAppComponent.builder().build()
    }

    companion object {
        lateinit var INSTANCE: MyApplication
    }
}