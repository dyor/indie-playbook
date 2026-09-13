import re

with open('build-logic/src/main/kotlin/configure-kmp-library-module.gradle.kts', 'r') as f:
    content = f.read()

content = re.sub(r'\s*wasmJs \{(?:[^{}]*|\{[^{}]*\})*\}\s*', '\n    ', content)
content = re.sub(r'\s*js\(IR\) \{(?:[^{}]*|\{[^{}]*\})*\}\s*', '\n    ', content)

# Instead of removing the tags directly, let's target the exact lines where they are used inside the group hierarchy
content = re.sub(r'\s*withJs\(\)', '', content)
content = re.sub(r'\s*withWasmJs\(\)', '', content)

with open('build-logic/src/main/kotlin/configure-kmp-library-module.gradle.kts', 'w') as f:
    f.write(content)

