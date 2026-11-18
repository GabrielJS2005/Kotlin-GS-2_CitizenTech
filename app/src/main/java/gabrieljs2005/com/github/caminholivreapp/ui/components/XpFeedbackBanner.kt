package gabrieljs2005.com.github.caminholivreapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gabrieljs2005.com.github.caminholivreapp.ui.theme.HighContrastText
import gabrieljs2005.com.github.caminholivreapp.ui.theme.SecondaryTeal
import gabrieljs2005.com.github.caminholivreapp.ui.theme.SecondaryText

@Composable
fun XpFeedbackBanner(
    xpGained: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SecondaryTeal.copy(alpha = 0.12f),
                shape = RoundedCornerShape(14.dp),
            )
            .border(
                width = 1.8.dp,
                color = SecondaryTeal,
                shape = RoundedCornerShape(14.dp),
            )
            .padding(14.dp),
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = SecondaryTeal,
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(
                text = "+$xpGained XP",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HighContrastText,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.5.sp,
                color = SecondaryText,
            )
        }
    }
}

