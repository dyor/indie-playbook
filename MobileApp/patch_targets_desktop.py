import re
import glob

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Remove js() and wasmJs() block entirely
    content = re.sub(r'\s*wasmJs \{(?:[^{}]*|\{[^{}]*\})*\}\s*', '\n    ', content)
    content = re.sub(r'\s*js\(IR\) \{(?:[^{}]*|\{[^{}]*\})*\}\s*', '\n    ', content)

    # Remove source sets
    content = re.sub(r'\s*val (?:wasmJs|js|web)Main by (?:getting|creating).*?(?=\n\s*val|\n\s*\})', '', content, flags=re.DOTALL)
    
    with open(filepath, 'w') as f:
        f.write(content)

for filepath in glob.glob('**/build.gradle.kts', recursive=True):
    if "desktopApp" in filepath:
        patch_file(filepath)

