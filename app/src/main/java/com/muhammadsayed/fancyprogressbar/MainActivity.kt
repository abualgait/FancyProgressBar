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

@Composable
fun FancyProgressBar(
    modifier: Modifier,
    leftColor: Color = Color(0xFF212022),
    rightColor: Color = Color(0xFF4F42EF),
    indicatorColor: Color = Color.White,
    textStyle: TextStyle = TextStyle(color = Color.White),
    cornerRaduis: Dp = 10.dp,
    onDragEnd: (Float) -> Unit,
    onDrag: (Float) -> Unit,
) {

    var offsetX by remember { mutableFloatStateOf(0f) }
    var progressBarWidthInDp by remember { mutableStateOf(Dp(0f)) }

    val guidelinePercentage by remember {
        derivedStateOf {
            Dp(offsetX) / progressBarWidthInDp
        }
    }


    val isAnimatePercentageUp by remember {
        derivedStateOf {
            guidelinePercentage < 0.2f || guidelinePercentage > 0.8f
        }
    }

    val animation = animateDpAsState(
        targetValue = if (isAnimatePercentageUp) -Dp(35f) else Dp(0f),
        label = "Text Animation"
    )
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .onSizeChanged {
                progressBarWidthInDp = it.width.dp
            }
    ) {

        val constraints = ConstraintSet {
            //References of widgets in the layout
            val leftBox = createRefFor("leftBox")
            val rightBox = createRefFor("rightBox")
            val indicator = createRefFor("indicator")

            val leftPercentage = createRefFor("leftPercentage")
            val rightPercentage = createRefFor("rightPercentage")

            val guideLine = createGuidelineFromStart(guidelinePercentage)

            //set constraints to the widgets
            constrain(leftBox)
            {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(guideLine, margin = 5.dp)
                width = Dimension.fillToConstraints
                height = Dimension.preferredValue(40.dp)
            }


            constrain(leftPercentage)
            {
                top.linkTo(leftBox.top)
                bottom.linkTo(leftBox.bottom)
                start.linkTo(leftBox.start, margin = 5.dp)
            }

            constrain(rightPercentage)
            {
                top.linkTo(leftBox.top)
                bottom.linkTo(leftBox.bottom)
                end.linkTo(rightBox.end, margin = 5.dp)
            }

            constrain(rightBox)
            {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(guideLine, margin = 10.dp)
                width = Dimension.fillToConstraints
                height = Dimension.preferredValue(40.dp)
            }

            constrain(indicator)
            {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                height = Dimension.preferredValue(40.dp)
            }

        }
        ConstraintLayout(
            constraints,
            modifier = Modifier.fillMaxSize()
        ) {

            //Left progress
            Box(
                modifier = Modifier
                    .layoutId("leftBox")
                    .background(
                        shape = RoundedCornerShape(cornerRaduis),
                        color = leftColor
                    )
            )

            //Right progress
            Box(
                modifier = Modifier
                    .layoutId("rightBox")
                    .background(
                        shape = RoundedCornerShape(cornerRaduis),
                        color = rightColor
                    )
            )

            Box(modifier = Modifier
                .layoutId("indicator")
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .width(5.dp)
                .background(shape = RoundedCornerShape(5.dp), color = indicatorColor)
                .pointerInput(Unit) {
                    detectDragGestures(onDragEnd = { onDragEnd(guidelinePercentage) }) { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x)
                            .coerceIn(0f, progressBarWidthInDp.value)
                        onDrag(guidelinePercentage)
                    }

                }
            )

            Text(
                text = "${String.format("%.0f", guidelinePercentage * 100)}%",
                modifier = Modifier
                    .layoutId("leftPercentage")
                    .offset(y = animation.value),
                style = textStyle
            )

            Text(
                text = "${String.format("%.0f", (1 - guidelinePercentage) * 100)}%",
                modifier = Modifier
                    .layoutId("rightPercentage")
                    .offset(y = animation.value),
                style = textStyle
            )

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