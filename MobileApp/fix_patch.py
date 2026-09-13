import re

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/data/repository/AppStoryRepository.kt', 'r') as f:
    content = f.read()

# Fix the end of the file syntax error
content = content.replace('                )\n            )\n        }\n    }\n}\n', '                )\n            )\n            appStoryDao.insertAll(fullStories)\n        }\n    }\n}\n')

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/data/repository/AppStoryRepository.kt', 'w') as f:
    f.write(content)
