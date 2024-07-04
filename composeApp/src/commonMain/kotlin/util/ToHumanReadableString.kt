package util

import com.ionspin.kotlin.bignum.integer.toBigInteger

fun Gelds.toHumanReadableString(): String = when (exponent) {
    in 0..2 -> "${(significand)}"
    in 3..5 -> "${(significand / 1000)},${significand % 1000}k"
    in 6..8 -> "${(significand / 1000000)},${significand % 1000}M"
    in 9..11 -> "${(significand / 1000000000)},${significand % 1000}B"
    in 12..14 -> "${(significand / 1000000000000)},${significand % 1000}T"
    in 15..17 -> "${(significand / 1000000000000000)},${significand % 1000}Q"
    in 15..17 -> "${(significand / 1000000000000000000)},${significand % 1000}Qa"
    in 18..20 -> "${(significand / 10.toBigInteger().pow(18))},${significand % 1000}Qt"
    in 21..23 -> "${(significand / 10.toBigInteger().pow(21))},${significand % 1000}Qa"

    else -> "You Are Rich" }
