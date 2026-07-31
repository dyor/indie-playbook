plugins {
    id("configure-kmp-library-module")
}

kotlin {
    android {
        namespace = "com.indieplaybook.app.auth.firebase"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.libs.auth.authApi)
            implementation(libs.kotlinx.coroutines.core)
        }
        mobileMain.dependencies {
            api(libs.kmpauth.firebase)
        }
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.bom))
        }
    }
}
