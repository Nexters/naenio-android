package com.nexters.teamvs.naenio.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nexters.teamvs.naenio.R

//TODO 피그마에 텍스트 스타일 네임 표시되도록 요청

val montserratBold = FontFamily(
    Font(R.font.montserrat_bold)
)

val montserratMedium = FontFamily(
    Font(R.font.montserrat_medium)
)

val montserratSemiBold = FontFamily(
    Font(R.font.montserrat_semibold)
)

val NaenioTypography = Typography(
    h1 = TextStyle(
        fontFamily = montserratBold,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp
    ),
    h2 = TextStyle(
        fontFamily = montserratBold,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp
    ),
    h3 = TextStyle(
        fontFamily = montserratBold,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp
    ),
    h4 = TextStyle(
        fontFamily = montserratMedium,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    ),
    h5 = TextStyle(
        fontFamily = montserratSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
        fontWeight = FontWeight.W600,
        fontSize = 12.sp
    ),
)

object Font {
    val pretendardSemiBold22 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
        fontWeight = FontWeight.W600,
        fontSize = 22.sp
    )
    val pretendardSemiBold20 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    )
    val pretendardSemiBold18 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
        fontWeight = FontWeight.W600,
        fontSize = 18.sp
    )
    val pretendardMedium18 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.W600,
        fontSize = 18.sp
    )
    val pretendardSemiBold16 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    )
    val pretendardMedium16 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    )
    val pretendardRegular16 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    )
    val pretendardSemiBold14 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    )
    val body2 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    )
    val pretendardRegular14 = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    )
    val caption = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    )

    val montserratBold18 = TextStyle(
        fontFamily = montserratBold,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp
    )

    val montserratMedium12 = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
        fontWeight = FontWeight.W600,
        fontSize = 12.sp
    )
    val montserratSemiBold16 = TextStyle(
        fontFamily = montserratSemiBold,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    )
    val montserratSemiBold12 = TextStyle(
        fontFamily = montserratSemiBold,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    )
}