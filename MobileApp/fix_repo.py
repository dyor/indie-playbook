import re

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/data/repository/AppStoryRepository.kt', 'r') as f:
    content = f.read()

# Replace the giant hardcoded list with just empty list since the intention was to use Firestore
content = re.sub(
    r'val fullStories = listOf\([\s\S]*?            \)\n            appStoryDao\.insertAll\(fullStories\)',
    'val fullStories = emptyList<AppStoryEntity>()\n            appStoryDao.insertAll(fullStories)',
    content
)

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/data/repository/AppStoryRepository.kt', 'w') as f:
    f.write(content)

