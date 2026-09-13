import re

with open('gradle/libs.versions.toml', 'r') as f:
    content = f.read()

content = content.replace(
    'gitlive-firebase-firestore = { module = "dev.gitlive:firebase-firestore", version = "2.1.0" }',
    'gitlive-firebase-firestore = { module = "dev.gitlive:firebase-firestore", version = "2.5.0" }'
)

with open('gradle/libs.versions.toml', 'w') as f:
    f.write(content)

