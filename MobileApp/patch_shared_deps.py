import re

with open('shared/build.gradle.kts', 'r') as f:
    content = f.read()

content = content.replace(
    'api(libs.kmpnotifier.push.firebase)',
    'api(libs.kmpnotifier.push.firebase)\n            api(libs.gitlive.firebase.firestore)'
)

with open('shared/build.gradle.kts', 'w') as f:
    f.write(content)
