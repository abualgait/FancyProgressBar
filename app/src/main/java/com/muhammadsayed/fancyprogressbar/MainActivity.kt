package com.muhammadsayed.fancyprogressbar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.muhammadsayed.fancyprogressbar.ui.theme.FancyProgressBarTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FancyProgressBarTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF121113)),
                    contentAlignment = Alignment.Center
                ) {
                    FancyProgressBar(
                        Modifier
                            .height(90.dp)
                            .width(250.dp),
                        onDragEnd = { finalProgress ->
                            Log.e(
                                "finalProgress: ",
                                "${String.format("%.0f", (1 - finalProgress) * 100)}%"
                            )


                        }, onDrag = { progress ->
                            Log.d("progress: ", "${String.format("%.0f", (1 - progress) * 100)}%")
                        })
                }
            }
        }
    }
}



@Preview
@Composable
fun FancyProgressPreview() {
    FancyProgressBar(
        Modifier
            .height(90.dp)
            .width(250.dp),
        onDragEnd = { finalProgress ->
            print("${String.format("%.0f", (1 - finalProgress) * 100)}%")


        }, onDrag = { progress ->
            print("${String.format("%.0f", (1 - progress) * 100)}%")
        })

}