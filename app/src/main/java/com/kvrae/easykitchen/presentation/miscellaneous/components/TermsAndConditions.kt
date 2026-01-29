package com.kvrae.easykitchen.presentation.miscellaneous.components

import android.util.Log
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

private const val TAG = "TermsAndConditionsText"

/**
 * Terms and Conditions component with clickable links
 * @param modifier The modifier for the component
 * @param onPrivacyClick Callback when privacy policy link is clicked
 * @param onTermsClick Callback when terms & conditions link is clicked
 * @param contentAlpha Alpha value for fade-in animation support
 */
@Suppress("DEPRECATION")
@Composable
fun TermsAndConditionsText(
    modifier: Modifier = Modifier,
    onPrivacyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    contentAlpha: Float = 1f
) {
    val annotatedString = buildAnnotatedString {
        append("By signing up, you agree to our ")

        // Terms & Conditions link
        pushStringAnnotation(
            tag = "TERMS",
            annotation = "terms"
        )
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append("Terms & Conditions")
        }
        pop()

        append(" and ")

        // Privacy Policy link
        pushStringAnnotation(
            tag = "PRIVACY",
            annotation = "privacy"
        )
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append("Privacy Policy")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        ),
        modifier = modifier,
        onClick = { offset ->
            Log.d(TAG, "Clicked at offset: $offset")
            annotatedString.getStringAnnotations(
                start = offset,
                end = offset
            ).forEach { annotation ->
                Log.d(TAG, "Found annotation: tag=${annotation.tag}")
            }

            annotatedString.getStringAnnotations(
                start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                when (annotation.tag) {
                    "TERMS" -> {
                        Log.d(TAG, "Terms clicked")
                        onTermsClick()
                    }

                    "PRIVACY" -> {
                        Log.d(TAG, "Privacy clicked")
                        onPrivacyClick()
                    }
                }
            }
        }
    )
}
