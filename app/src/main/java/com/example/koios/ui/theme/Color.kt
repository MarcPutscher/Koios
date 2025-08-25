package com.example.koios.ui.theme

import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color


fun makeColorDarker(color: Color, darknessFactor: Double = 0.8): Color {
    val alpha = android.graphics.Color.alpha(color.toArgb())
    val red = (android.graphics.Color.red(color.toArgb()) * darknessFactor).toInt()
    val green = (android.graphics.Color.green(color.toArgb()) * darknessFactor).toInt()
    val blue = (android.graphics.Color.blue(color.toArgb()) * darknessFactor).toInt()
    return Color(android.graphics.Color.argb(alpha, red, green, blue))
}
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Eucalyptus = Color(android.graphics.Color.rgb(114, 132, 106))
val Charcoal = Color(android.graphics.Color.rgb(0, 71, 96))
val Teal = Color(android.graphics.Color.rgb(127, 140, 170))
val CadetBlue = Color(android.graphics.Color.rgb(85, 168, 170))
val Glaucous = Color(android.graphics.Color.rgb(98, 138, 186))
val DarkBlue = Color(android.graphics.Color.rgb(34, 40, 49))
val LightBlue = Color(android.graphics.Color.rgb(172, 224, 236))
val DarkGrey = Color(android.graphics.Color.rgb(57, 62, 70))
val LightGrey = Color(android.graphics.Color.rgb(199, 204, 211))
val RobinEggBlue = Color(android.graphics.Color.rgb(174, 225, 217))
val DarkGreen = Color(android.graphics.Color.rgb(0, 67, 61))


val TextColor1 = DarkGrey
val TextColor2 = Charcoal

val TaskBarBackgroundColor = Teal
val TextFieldBackgroundColor = LightGrey

val Card1BackgroundColor = CadetBlue
val Card2BackgroundColor = Glaucous
val Card3BackgroundColor = Eucalyptus

val HighLightBackground = DarkGreen
val HighLightText = RobinEggBlue