package gabrieljs2005.com.github.caminholivreapp.data.remote

import retrofit2.http.GET

interface BarrierApi {

    @GET("GabrielJS2005/1299682cf15857deae0ebcb58494988f/raw/barriers_sp.json")
    suspend fun fetchBarriers(): List<BarrierDto>

    companion object {
        const val BASE_URL = "https://gist.githubusercontent.com/"
    }
}

