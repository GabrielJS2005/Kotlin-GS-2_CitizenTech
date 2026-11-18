package gabrieljs2005.com.github.caminholivreapp.di

import android.content.Context
import gabrieljs2005.com.github.caminholivreapp.data.local.CaminhoLivreDatabase
import gabrieljs2005.com.github.caminholivreapp.data.preferences.SessionPreferences
import gabrieljs2005.com.github.caminholivreapp.data.remote.BarrierApi
import gabrieljs2005.com.github.caminholivreapp.data.remote.RetrofitFactory
import gabrieljs2005.com.github.caminholivreapp.data.repository.BarrierRepository
import gabrieljs2005.com.github.caminholivreapp.data.repository.GuardianRepository

class AppContainer(context: Context) {

    private val database: CaminhoLivreDatabase by lazy {
        CaminhoLivreDatabase.getInstance(context)
    }

    private val barrierApi: BarrierApi by lazy {
        RetrofitFactory.createBarrierApi()
    }

    val sessionPreferences: SessionPreferences by lazy {
        SessionPreferences(context)
    }

    val barrierRepository: BarrierRepository by lazy {
        BarrierRepository(
            dao = database.barrierDao(),
            api = barrierApi,
        )
    }

    val guardianRepository: GuardianRepository by lazy {
        GuardianRepository(
            dao = database.guardianProgressDao(),
        )
    }
}

