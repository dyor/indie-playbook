import re

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/root/Di.kt', 'r') as f:
    content = f.read()

content = content.replace('import com.indieplaybook.app.data.source.remote.apiservices.AppStoryApiService\n', '')
content = content.replace('    single { AppStoryApiService() }\n', '')
content = content.replace('    single { AppStoryRepository(get(), get()) }\n', '    single { AppStoryRepository(get()) }\n')

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/root/Di.kt', 'w') as f:
    f.write(content)
