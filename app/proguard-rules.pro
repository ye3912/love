# Add project specific ProGuard rules here.

# Keep data models
-keep class com.night.memo.data.model.** { *; }

# Keep Compose runtime
-keep class androidx.compose.** { *; }

# Keep Navigation
-keep class androidx.navigation.** { *; }

# Keep Kotlin metadata for serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep enum classes
-keepclassmembers enum * { *; }
