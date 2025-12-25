package com.qrcode.generator.models

sealed class ShapePreset {
    data object Business : ShapePreset()
    data object Artistic : ShapePreset()
    data object Elegant : ShapePreset()
    data object Vibrant : ShapePreset()
    data object Minimal : ShapePreset()
    data class Custom(val config: QRGeneratorConfig) : ShapePreset()
}
