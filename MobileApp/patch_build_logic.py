import re

with open('build-logic/src/main/kotlin/configure-kmp-library-module.gradle.kts', 'r') as f:
    content = f.read()

content = content.replace('withWasmJs()', '')
content = content.replace('withJs()', '')
content = content.replace('wasmJs {\n        browser()\n    }', '')
content = content.replace('js(IR) {\n        nodejs()\n        browser()\n        binaries.library()\n    }', '')

with open('build-logic/src/main/kotlin/configure-kmp-library-module.gradle.kts', 'w') as f:
    f.write(content)

