package com.abcdandroid.monthcomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.abcdandroid.monthcomponent.ui.theme.MonthComponentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonthComponentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MonthView()
                }
            }
        }
    }
}

@Composable
fun MonthView() {
    val startFrom = 3
    val monthCount = 31 + startFrom
    val weekCount = 7
    val today = 22

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column {
            Row() {
                for (weekDay in WeekDay.values()) {
                    Text(
                        text = weekDay.title,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            for (iColumn in 1..monthCount step weekCount) {


                Row {
                    for (iRow in iColumn until iColumn + weekCount) {
                        var currentDay = if (iRow <= monthCount) (iRow - startFrom) else 0
                        if (iColumn == 1 && iRow < startFrom) currentDay = 0
                        val dayModifier = if (currentDay == today)
                            Modifier.clip(CircleShape).background(Color.Blue) else Modifier
                        Text(
                            text = if (currentDay == 0) "" else currentDay.toString(),
                            modifier = Modifier.padding(4.dp).then(dayModifier).padding(2.dp)
                                .weight(1f),
                            textAlign = TextAlign.Center,
                            color = if(currentDay == today) Color.White else Color.Blue
                        )
                    }
                }
            }
        }


    }

}

enum class WeekDay(val title: String) {
    Saturday("شنبه"),
    Sunday("یکشنبه"),
    Monday("دوشنبه"),
    Tuesday("سه شنبه"),
    Wednesday("چهارشنبه"),
    Thursday("پنجشنبه"),
    Friday("جمعه"),
}