import re

with open('shared/build.gradle.kts', 'r') as f:
    content = f.read()

content = re.sub(r'\s*val webMain by getting \{(?:[^{}]*|\{[^{}]*\})*\}', '', content)

with open('shared/build.gradle.kts', 'w') as f:
    f.write(content)

