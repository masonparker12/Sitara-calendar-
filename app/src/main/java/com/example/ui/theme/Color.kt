package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Professional Polish Brand Colors
val PolishIndigo = Color(0xFF4F46E5)      // Indigo 600
val PolishIndigoLight = Color(0xFFEEF2FF) // Indigo 50
val PolishIndigoBorder = Color(0xFFC7D2FE)// Indigo 200
val PolishPurple = Color(0xFF9333EA)      // Purple 600
val PolishRose = Color(0xFFF43F5E)        // Rose 500
val PolishRoseLight = Color(0xFFFFF1F2)   // Rose 50
val PolishRoseText = Color(0xFFFB7185)    // Rose 400
val PolishAmber = Color(0xFFF59E0B)       // Amber 500
val PolishAmber600 = Color(0xFFD97706)    // Amber 600
val PolishAmber50 = Color(0xFFFFFBEB)     // Amber 50
val PolishOrange50 = Color(0xFFFFF7ED)    // Orange 50
val PolishAmberBorder = Color(0xFFFEF3C7) // Amber 100

// Slate Scale
val Slate50 = Color(0xFFF8FAFC)
val Slate100 = Color(0xFFF1F5F9)
val Slate200 = Color(0xFFE2E8F0)
val Slate300 = Color(0xFFCBD5E1)
val Slate400 = Color(0xFF94A3B8)
val Slate500 = Color(0xFF64748B)
val Slate600 = Color(0xFF475569)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val Slate900 = Color(0xFF0F172A)
val Slate950 = Color(0xFF020617)

// Backward compatibility references for existing components
val SitaraPurple = PolishIndigo
val SitaraBlue = Color(0xFF2575FC)
val SitaraCyan = Color(0xFF38BDF8)
val SitaraPink = PolishRose
val SitaraOrange = Color(0xFFFF7844)
val SitaraGreen = Color(0xFF10B981)
val SitaraAmber = PolishAmber

// Sunday Accent Color
val SundayRedLight = PolishRose
val SundayRedDark = PolishRoseText

// Theme Surfaces
val LightBg = Slate50
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Slate100
val LightBorder = Slate200
val LightTextPrimary = Slate900
val LightTextSecondary = Slate500

val DarkBg = Slate950
val DarkSurface = Slate900
val DarkSurfaceVariant = Slate800
val DarkBorder = Slate700
val DarkTextPrimary = Slate50
val DarkTextSecondary = Slate400

// Gradients
object SitaraGradients {
    // Exact Professional Polish Header gradient: from indigo-600 via purple-600 to rose-500
    val HeaderGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF4F46E5),
            Color(0xFF9333EA),
            Color(0xFFF43F5E)
        )
    )

    val HeaderGradientDark = Brush.linearGradient(
        colors = listOf(
            Color(0xFF312E81),
            Color(0xFF581C87),
            Color(0xFF881337)
        )
    )

    val SelectedDateGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6366F1),
            Color(0xFF9333EA)
        )
    )

    val CurrentDateGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF43F5E),
            Color(0xFFFB7185)
        )
    )

    val AmberBannerGradient = Brush.horizontalGradient(
        colors = listOf(
            PolishAmber50,
            PolishOrange50
        )
    )

    val AmberBannerGradientDark = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF451A03).copy(alpha = 0.6f),
            Color(0xFF78350F).copy(alpha = 0.5f)
        )
    )
}

// Event Color Options
data class EventColorItem(
    val id: String,
    val name: String,
    val color: Color,
    val hexString: String
)

val AvailableEventColors = listOf(
    EventColorItem("indigo", "Indigo", Color(0xFF4F46E5), "#4F46E5"),
    EventColorItem("purple", "Purple", Color(0xFF9333EA), "#9333EA"),
    EventColorItem("rose", "Rose", Color(0xFFF43F5E), "#F43F5E"),
    EventColorItem("amber", "Amber", Color(0xFFF59E0B), "#F59E0B"),
    EventColorItem("blue", "Blue", Color(0xFF2575FC), "#2575FC"),
    EventColorItem("green", "Green", Color(0xFF10B981), "#10B981")
)
