with open('shared/src/commonMain/kotlin/com/indieplaybook/app/data/repository/AppStoryRepository.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'import com.indieplaybook.app.data.source.local.entity.toModel',
    'import com.indieplaybook.app.data.source.local.entity.toModel\nimport com.indieplaybook.app.data.source.local.entity.toEntity'
)

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/data/repository/AppStoryRepository.kt', 'w') as f:
    f.write(content)
