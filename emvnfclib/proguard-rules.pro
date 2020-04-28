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

-dontoptimize
#-optimizationpasses 1
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
#-multidex

-keepattributes *Annotation*, Signature, Exception
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

#-keepresources string/**
#-keepresourcefiles res/drawable/**
#-keepresourcefiles res/strings.xml
#-keepresourcefiles lib/**
#-keepresourcexmlattributenames *

#-keepresources res/values/**
#-keepresourcefiles res/values/**
#-keepresourcexmlattributenames res/values/**

-keepclassmembers class **.R {
    public static <fields>;
}

-keep class **.R$*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v13.**
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v13.app.Fragment
-keep public class * extends android.support.v4.app.FragmentActivity
-keep public class * extends android.support.v13.app.FragmentActivity
-keep public class * extends android.app.Dialog.**
-keep public class * extends android.app.AlertDialog.**
-keep public class * extends android.app.ProgressDialog.**

-keep class android.support.v13.** { *; }
-keep class android.support.v4.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * extends android.support.v4.app.FragmentActivity {
   public void *(android.view.View);
}

-keepclassmembers class * extends android.support.v13.app.FragmentActivity {
   public void *(android.view.View);
}

-keepclassmembers class * extends android.support.v4.app.Fragment {
   public void *(android.view.View);
}

-keepclassmembers class * extends android.support.v13.app.Fragment {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# GOOGLE MATERIAL
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# DATA BINDING
-keep class androidx.databinding.** { *; }
-dontwarn androidx.databinding.**


# RETROFIT
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn retrofit2.**
-dontnote retrofit2.**
-keep class retrofit2.** { *; }

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# OKHTTP
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**

# OKHTTP - Okio
-keep class okio.** { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

#GSON
-keep class sun.misc.** { *; }
-keep class com.google.gson.** { *; }
-dontwarn sun.misc.**
-dontwarn com.google.gson.**

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# PARCELABLE
-keep class * implements android.os.Parcelable { *;}
-keepclassmembers class * implements android.os.Parcelable { *;}

# SERIALIZABLE
-keep class * implements java.io.Serializable { *;}
-keepclassmembers class * implements java.io.Serializable { *;}

# REACTIVE
-keep class io.reactivex.** { *; }
-keep class org.reactivestreams.** { *; }

# REALM
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

# GMS
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-dontnote com.google.android.gms.**

# CRASHLYTICS
-keep class com.crashlytics.** { *; }
-keep class io.fabric.** { *; }
-keep class com.google.firebase.crash.** { *; }
-dontwarn com.crashlytics.**
-dontwarn io.fabric.**
-dontwarn com.google.firebase.crash.**

# LEAKCANARY
-keep class com.squareup.leakcanary.** { *; }
-dontwarn com.squareup.leakcanary.**