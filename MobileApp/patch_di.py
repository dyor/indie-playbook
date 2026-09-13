with open('shared/src/commonMain/kotlin/com/indieplaybook/app/root/Di.kt', 'r') as f:
    content = f.read()

import re

# Add import
content = content.replace(
    'import com.indieplaybook.app.data.repository.AppStoryRepository',
    'import com.indieplaybook.app.data.repository.AppStoryRepository\nimport com.indieplaybook.app.data.source.remote.apiservices.AppStoryApiService'
)

# Replace single
content = content.replace(
    '    single { AppStoryRepository(get()) }',
    '    single { AppStoryApiService() }\n    single { AppStoryRepository(get(), get()) }'
)

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/root/Di.kt', 'w') as f:
    f.write(content)
