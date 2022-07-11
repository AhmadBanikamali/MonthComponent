@file:OptIn(ExperimentalPagerApi::class)

package com.abcdandroid.monthcomponent

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.abcdandroid.monthcomponent.ui.theme.MonthComponentTheme
import com.abcdandroid.monthcomponent.utils.PersianDate
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonthComponentTheme {
                var a: Modifier by remember {
                    mutableStateOf(Modifier)
                }

                val context = LocalContext.current
                var height by remember {
                    mutableStateOf(0f)
                }

                Box(Modifier.then(a)) {
                    YearView{
                        if(it!=0f) {
                            height = it
                            a = Modifier.height(it.toDp(context).dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YearView(onSizeChange: (Float) -> Unit) {
    var maxHeight: Int by remember {
        mutableStateOf(0)
    }

    val currentMonth = PersianDate().shMonth
    val currentYear = PersianDate().shYear

    val firstYear = 1390
    val lastYear = 1404

    val state = rememberPagerState(/*initIndex*/)

    /*LazyColumn( modifier = Modifier , reverseLayout = false, state = state ) {
        for (year in firstYear.. lastYear)
        items(12) { i ->
           val a = i+1
            MonthView(
                modifier = Modifier.onSizeChanged {
                    if(it.height>maxHeight) maxHeight = it.height
                },
                startFrom = PersianDate().setShYear(year).setShMonth(a).setShDay(1).dayOfWeek(),
                monthLength = PersianDate().setShYear(year).setShMonth(a).monthDays,
                monthName = PersianDate().setShYear(year).setShMonth(a).monthName(),
                yearName = year.toString()
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
    }*/

    val totalMonthCount = (lastYear - firstYear) * 12



    VerticalPager(
        count = totalMonthCount,
        reverseLayout = true,
        state = state,
        modifier = Modifier.background(Color.Green)
    ) { monthIndex ->
        val year = lastYear - monthIndex / 12
        val month = 12 - (monthIndex % 12)

        MonthView(
            modifier = Modifier ,
            startFrom = PersianDate().setShYear(year).setShMonth(month).setShDay(1).dayOfWeek(),
            monthLength = PersianDate().setShYear(year).setShMonth(month).monthDays,
            monthName = PersianDate().setShYear(year).setShMonth(month).monthName(),
            yearName = year.toString(),
            onSizeChange = {
                println("bb"+it)
                onSizeChange(it)
            }
        )

    }

}


@Composable
fun MonthView(
    modifier: Modifier,
    startFrom: Int,
    monthLength: Int,
    monthName: String,
    yearName: String,
    onSizeChange: (Float) -> Unit
) {
    val monthCount = monthLength + startFrom
    val weekCount = 7
    val today = 22

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

            Column(modifier = modifier.onGloballyPositioned {
                println("aa"+it.size.height.toFloat())
                onSizeChange(it.size.height.toFloat())
            }) {
                Row {
                    Text(text = yearName)
                    Text(text = monthName)
                }

                Row {
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
                                Modifier
                                    .clip(CircleShape)
                                    .background(Color.Blue) else Modifier
                            Text(
                                text = if (currentDay == 0) "" else currentDay.toString(),
                                modifier = Modifier
                                    .padding(4.dp)
                                    .then(dayModifier)
                                    .padding(2.dp)
                                    .weight(1f),
                                textAlign = TextAlign.Center,
                                color = if (currentDay == today) Color.White else Color.Blue
                            )
                        }
                    }
                }
            }



    }

}


fun Float.toDp(context:Context) = this / context.resources.displayMetrics.density



enum class WeekDay(val title: String, val dayNumber: Int) {
    Saturday("شنبه", 0),
    Sunday("یکشنبه", 1),
    Monday("دوشنبه", 2),
    Tuesday("سه شنبه", 3),
    Wednesday("چهارشنبه", 4),
    Thursday("پنجشنبه", 5),
    Friday("جمعه", 6),
}