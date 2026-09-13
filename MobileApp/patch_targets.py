import os
import glob

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Remove js() block entirely
    import re
    content = re.sub(r'\n    js \{(?:[^{}]*|\{[^{}]*\})*\}\n', '\n', content)
    
    # Remove @OptIn(ExperimentalWasmDsl::class) and wasmJs block
    content = re.sub(r'    @OptIn\(ExperimentalWasmDsl::class\)\n', '', content)
    content = re.sub(r'\n    wasmJs \{(?:[^{}]*|\{[^{}]*\})*\}\n', '\n', content)

    # Remove wasmJsMain related configs
    content = re.sub(r'\s*val (?:wasmJs|js|web)Main by (?:getting|creating).*?(?=\n\s*val|\n\s*\})', '', content, flags=re.DOTALL)
    
    # Remove the dependency blocks
    content = re.sub(r'\s*val (?:wasmJs|js|web)Main \{(?:[^{}]*|\{[^{}]*\})*\}', '', content)

    # Specific common main updates to remove sqlite-wasm-worker
    content = re.sub(r'\s*implementation\(npm\("@sqlite.org/sqlite-wasm", "3.50.1-build1"\)\)', '', content)
    content = re.sub(r'\s*implementation\(npm\("sqlite-wasm-worker", project.file\("sqlite-wasm-worker"\)\)\)', '', content)
    content = re.sub(r'\s*import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl\n', '', content)
    
    with open(filepath, 'w') as f:
        f.write(content)

for filepath in glob.glob('**/build.gradle.kts', recursive=True):
    patch_file(filepath)

