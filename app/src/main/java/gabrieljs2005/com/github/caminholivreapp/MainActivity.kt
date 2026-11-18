package gabrieljs2005.com.github.caminholivreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import gabrieljs2005.com.github.caminholivreapp.ui.navigation.CaminhoLivreNavHost
import gabrieljs2005.com.github.caminholivreapp.ui.theme.CaminhoLivreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = (application as CaminhoLivreApplication).container
        setContent {
            CaminhoLivreTheme {
                CaminhoLivreNavHost(appContainer = appContainer)
            }
        }
    }
}