package com.muhammadsayed.fancyprogressbar

import android.os.Bundle
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
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
                    val progressBarWidth = 250
                    with(LocalDensity.current) {
                        FancyProgressBar(progressBarWidth.dp.toPx())
                    }

                }
            }
        }
    }
}

@Composable
fun FancyProgressBar(progressBarWidthInPx: Float) {

    var offsetX by remember { mutableFloatStateOf(0f) }
    val guidelinePercentage by remember {
        derivedStateOf {
            offsetX / progressBarWidthInPx
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
        modifier = Modifier
            .width(250.dp)
            .height(90.dp)
            .clip(
                RoundedCornerShape(10.dp)
            )
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
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF212022)
                    )
            )

            //Right progress
            Box(
                modifier = Modifier
                    .layoutId("rightBox")
                    .background(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF4F42EF)
                    )
            )

            Box(modifier = Modifier
                .layoutId("indicator")
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .width(5.dp)
                .background(shape = RoundedCornerShape(5.dp), color = Color.White)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x)
                            .coerceIn(0f, 250.dp.toPx())
                    }
                }
            )

            Text(
                text = "${String.format("%.0f", guidelinePercentage * 100)}%",
                modifier = Modifier
                    .layoutId("leftPercentage")
                    .offset(y = animation.value),
                color = Color.White.copy(alpha = 0.7f)
            )

            Text(
                text = "${String.format("%.0f", (1 - guidelinePercentage) * 100)}%",
                modifier = Modifier
                    .layoutId("rightPercentage")
                    .offset(y = animation.value),
                color = Color.White.copy(alpha = 0.7f)
            )

        }
    }


}

@Preview
@Composable
fun FancyProgressPreview() {
    with(LocalDensity.current) {
        FancyProgressBar(250.dp.toPx())
    }
}