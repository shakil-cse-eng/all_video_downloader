plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.onesignal.androidsdk.onesignal-gradle-plugin")
}

android {
    namespace = "com.project.allvideodownloader"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.project.allvideodownloader"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    android {
        packagingOptions {
            resources.excludes.add("META-INF/*")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.1.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation(project(":youtubeExtractor"))
    //exoplayer
    implementation("com.google.android.exoplayer:exoplayer:2.18.7")
    //ads
    implementation("com.google.android.gms:play-services-ads:22.2.0")
    //zoom_img
    implementation("com.jsibbold:zoomage:1.3.1")
    //country_picker
    implementation("com.hbb20:ccp:2.6.0")
    // Multidex
    implementation("androidx.multidex:multidex:2.0.1")
    // Glide image loader
    implementation("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2")
    //Gson
    implementation("com.google.code.gson:gson:2.9.1")
    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.8.1")
    implementation("com.squareup.okhttp:okhttp:2.7.5")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    //twitter
    implementation("com.twitter.sdk.android:twitter-core:3.3.0")
    //html parser
    implementation("org.jsoup:jsoup:1.12.1")

    implementation("com.karumi:dexter:6.2.2")

    implementation("commons-io:commons-io:2.8.0")

    //def lifecycle_version = '2.3.1'
    implementation("androidx.lifecycle:lifecycle-process:2.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime:2.3.1")
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:2.3.1")
    //appUpdate
    implementation("com.google.android.play:core:1.10.3")
    //refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    //OneSignal
    implementation("com.onesignal:OneSignal:4.7.0")

    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    //def lifecycle_version = '2.2.0'
    //implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}