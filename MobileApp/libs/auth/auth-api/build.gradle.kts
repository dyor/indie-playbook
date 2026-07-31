plugins {
    id("configure-kmp-library-module")
}

kotlin {
    android {
        namespace = "com.indieplaybook.app.auth.api"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
