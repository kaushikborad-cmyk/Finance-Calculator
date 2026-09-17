android {
    signingConfigs {
        create("release") {
            storeFile = file("my-release-key.jks")
            storePassword = "PASSWORD"
            keyAlias = "MY_ALIAS"
            keyPassword = "PASSWORD"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
}