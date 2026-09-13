with open('shared/build.gradle.kts', 'r') as f:
    content = f.read()

# I am going to properly setup gitlive to only be included on iOS and Android
import re

common_dependencies = """
        commonMain.dependencies {
            api(libs.gitlive.firebase.firestore)
"""

android_dependencies = """
        androidMain.dependencies {
            implementation(libs.firebase.firestore)
"""

content = content.replace("commonMain.dependencies {", common_dependencies)
content = content.replace("androidMain.dependencies {", android_dependencies)

with open('shared/build.gradle.kts', 'w') as f:
    f.write(content)

