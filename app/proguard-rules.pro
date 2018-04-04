# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /develope/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-libraryjars libs

-keepattributes Exceptions,SourceFile,LineNumberTable,Signature,EnclosingMethod,*Annotation*,InnerClasses,*DatabaseField*,*DatabaseTable*,*SerializedName*
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

-keepclassmembers enum * {
    public static **[] values();
    public static ** values();
    public static ** valueOf(java.lang.String);
}

## accesses a method 'value()' dynamically patch
-dontnote org.codehaus.groovy.**
-dontnote retrofit.RestMethodInfo.**

-keep class android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class org.apache.** { *; }
-keep class org.osgi.** { *; }
-keep interface org.osgi.** { *; }
-keep class org.slf4j.** { *; }
-keep class aQute.bnd.** { *; }
-keep class com.crashlytics.** { *; }
-keep class com.kakao.** { *; }
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

#naver login
-keep public class com.nhn.android.naverlogin.** {
    public protected *;
}

## Tuda Start
## Tuda App

-keep enum com.tuda.teacher.**
-keep class javax.mail.** { *; }
-keep class com.sun.mail.** { *; }
-keep class javax.activation.** { *; }
-keep class com.sun.activation.** { *; }
-keep class org.codehaus.groovy.** { *; }

-keep class * extends android.app.Application { public *; }
-keep class * extends android.app.Activity { public *; }
-keep class * extends android.support.v7.app.ActionBarActivity { public *; }
-keep class * extends android.support.v7.app.AppCompatActivity { public *; }
-keep class * extends com.tuda.teacher.ui.activity.TudaActivity { public *; }
-keep class com.tuda.teacher.listener.** { public *; }
-keep public class com.tuda.teacher.network.code.** { public *; }

-keep class com.tuda.teacher.network.entry.** { *; }
-keep class com.tuda.teacher.network.entity.** { *; }
-keep class com.tuda.teacher.talk.entity.** { *; }
-keep class com.tuda.teacher.calendar.model.entity.** { *; }
-keep class com.tuda.teacher.type.** { *; }

## Tuda End

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
    public static <fields>;
    public *;
}

-dontwarn android.support.v4.**
-dontwarn org.slf4j.**
-dontwarn com.google.android.gms.**
-dontwarn aQute.bnd.**
-dontwarn javax.activation.**
-dontwarn com.sun.mail.**
-dontwarn jp.wasabeef.glide.transformations.gpu.**

## groovy
-keep class org.codehaus.groovy.vmplugin.**
-keep class org.codehaus.groovy.runtime.dgm*

-keepclassmembers class org.codehaus.groovy.runtime.dgm* {*;}
-keepclassmembers class ** implements org.codehaus.groovy.runtime.GeneratedClosure {*;}
-keepclassmembers class org.codehaus.groovy.reflection.GroovyClassValue* {*;}
-keepclassmembers class groovyx.example.** {*;}
-keepclassmembers class com.arasthel.swissknife.utils.Finder {*;}

-dontwarn org.codehaus.groovy.**
-dontwarn groovy**

#picasso
-dontwarn com.squareup.okhttp.**

#okio
-dontwarn okio.**

#jsoup
-keep public class org.jsoup.** {
	public *;
}

# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# butterknife
-dontwarn butterknife.internal.**

-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8


## Joda Time
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

# jacson
-dontskipnonpubliclibraryclassmembers
-keep class com.fasterxml.jackson.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames interface com.fasterxml.jackson.** { *; }
-keepnames class org.codehaus.jackson.** { *; }
-dontwarn javax.xml.**
-dontwarn javax.xml.stream.events.**
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.** { *; }
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
    public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *;
}
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}

# ormlite
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keep class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
-keepclassmembers class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper {
    public <init>(android.content.Context);
}
-keep @com.j256.ormlite.table.DatabaseTable class * {
    @com.j256.ormlite.field.DatabaseField <fields>;
    @com.j256.ormlite.field.ForeignCollectionField <fields>;
    <init>();
}

#ical4j
-dontwarn groovy.**
-dontwarn org.codehaus.groovy.**
-dontwarn org.apache.commons.logging.**
-dontwarn sun.misc.Perf

-dontnote com.google.vending.**
-dontnote com.android.vending.licensing.**
-dontwarn net.fortuna.ical4j.model.**

#rxJava
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#youtubeplayer

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

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


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
