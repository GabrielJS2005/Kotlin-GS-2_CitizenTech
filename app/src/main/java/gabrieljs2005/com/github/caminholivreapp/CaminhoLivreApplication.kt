package gabrieljs2005.com.github.caminholivreapp

import android.app.Application
import gabrieljs2005.com.github.caminholivreapp.di.AppContainer

class CaminhoLivreApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

