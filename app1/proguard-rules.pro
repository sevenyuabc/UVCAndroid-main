# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontwarn
-dontpreverify
-verbose
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.graphics.BitmapFactory$Options
-keep class * extends java.lang.annotation.Annotation { *; }
-keep class android.net.http.SslError

-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class m.framework.**{*;}
-keep class rx.**{*;}
-keep class sun.misc.**{*;}
-dontwarn **.R$*

-dontwarn android.support.v4.**
-dontwarn android.support.v7.**

-dontwarn com.alibaba.fastjson.serializer.SerializeWriter
-dontwarn com.lidroid.xutils.**

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
	public <init>(org.json.JSONObject);
}

-keepclasseswithmembers class * {
	public <init> (android.content.Context, java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep class android.support.v4.** {
	*;
}

-keepclassmembers class * extends android.app.Activity {
  public void *(android.view.View);
}


#----js不进行混淆 注解不混淆
-keepattributes *Annotation*
-keepattributes *JavascriptInterface* 

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
	public <init>(org.json.JSONObject);
}

-keepclasseswithmembers class * {
	public <init> (android.content.Context, java.lang.String);
}


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class * implements java.io.Serializable {
    public *;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class android.support.v4.** {
	*;
}

-keepclassmembers class * extends android.app.Activity {
  public void *(android.view.View);
}


#----js不进行混淆 注解不混淆
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

#jar包不进行混淆
-libraryjars libs/Msc.jar
-libraryjars libs/xUtils-2.6.14.jar
-libraryjars libs/seekuApi-debug-1.18.aar

-keep class com.lidroid.xutils.** { *; }


#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}


#eventbus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}


#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#banner
-keep class androidx.recyclerview.widget.**{*;}
-keep class androidx.viewpager2.widget.**{*;}

#科大讯飞
-keep class com.iflytek.**{*;}
-keepattributes Signature


-keep class com.cicada.kidscard.**.domain.**{*;}
-keep class com.cicada.kidscard.storage.db.model.**{*;}
-keep class com.cicada.kidscard.hardware.Bluetooth.BluetoothDeviceInfo{*;}
-keep class com.cicada.kidscard.view.banner.BannerInfo{*;}

-keep class com.wits.serialport.SerialPort{*;}