# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep generic signatures and annotations used by Retrofit and Gson.
# Retrofit also needs InnerClasses and EnclosingMethod so generic return types
# on suspend API methods are still visible after R8 optimization.
-keepattributes Signature,InnerClasses,EnclosingMethod,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault

# Retrofit service interfaces are created reflectively.
-keep interface com.example.xddemo.network.** { *; }

# Keep Retrofit HTTP service definitions compatible with R8 full mode.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# Suspend functions depend on Continuation generic signatures at runtime.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Keep response models used by Gson deserialization.
-keep class com.example.xddemo.data.model.** { *; }

# Keep fields that Gson reads reflectively.
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Room database, DAO interfaces, and entity models.
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Dao class *
-keep @androidx.room.Entity class *
-keep class androidx.room.RoomDatabase_Impl
-dontwarn androidx.room.paging.**

# Keep Kotlin metadata used by reflection-based libraries.
-keep class kotlin.Metadata { *; }

# Silence optional annotations referenced by OkHttp/Retrofit.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn kotlin.KotlinNothingValueException
-dontwarn kotlinx.coroutines.**
-dontwarn retrofit2.**
