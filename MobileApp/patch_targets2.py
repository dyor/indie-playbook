import os
import glob
import re

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Remove js() block entirely again more aggressively if missed
    content = re.sub(r'    js \{\n.*?\}\n', '', content, flags=re.DOTALL)
    
    with open(filepath, 'w') as f:
        f.write(content)

for filepath in glob.glob('**/build.gradle.kts', recursive=True):
    patch_file(filepath)

