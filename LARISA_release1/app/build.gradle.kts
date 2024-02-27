plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
        versionCode = 1
        versionName = "1.02"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation ("com.github.TutorialsAndroid:GButton:v1.0.19")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("androidx.appcompat:appcompat:1.4.0")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation ("androidx.preference:preference:1.1.1")
    implementation ("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("org.tensorflow:tensorflow-lite:2.2.0")
    testImplementation ("junit:junit:4.13.1")
    implementation ("com.mikepenz:aboutlibraries:8.5.0")
    implementation ("com.github.nekocode:Badge:2.1")
    implementation ("com.github.angads25:filepicker:1.1.1")
    implementation ("net.dongliu:apk-parser:2.6.10")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}