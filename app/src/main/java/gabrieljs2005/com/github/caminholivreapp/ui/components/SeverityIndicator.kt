package gabrieljs2005.com.github.caminholivreapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gabrieljs2005.com.github.caminholivreapp.ui.theme.AccentAmber
import gabrieljs2005.com.github.caminholivreapp.ui.theme.BorderGray
import gabrieljs2005.com.github.caminholivreapp.ui.theme.CriticalRed
import gabrieljs2005.com.github.caminholivreapp.ui.theme.PrimaryNavy

@Composable
fun SeverityIndicator(
    severity: Int,
    modifier: Modifier = Modifier,
) {
    val clamped = severity.coerceIn(1, 5)
    val activeColor = when {
        clamped >= 5 -> CriticalRed
        clamped == 4 -> AccentAmber
        else -> PrimaryNavy
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.semantics {
            contentDescription = "Gravidade nível $clamped de 5"
        },
    ) {
        Text(
            text = "Gravidade $clamped/5",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = activeColor,
        )
        Spacer(modifier = Modifier.width(6.dp))
        repeat(5) { index ->
            val filled = index < clamped
            Box(
                modifier = Modifier
                    .padding(end = 3.dp)
                    .width(8.dp)
                    .height(14.dp)
                    .background(
                        color = if (filled) activeColor else BorderGray,
                        shape = RoundedCornerShape(3.dp),
                    ),
            )
        }
    }
}

