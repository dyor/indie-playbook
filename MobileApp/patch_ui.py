import re

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/presentation/screens/appstorydetail/AppStoryDetailScreen.kt', 'r') as f:
    content = f.read()

new_block = """                            if (story.appStoreRating != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Rating:",
                                        style = AppTheme.typography.bodySmall,
                                        color = AppTheme.colors.text.secondary,
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${story.appStoreRating} ★ (${story.appStoreReviews})",
                                        style = AppTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.primary,
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            if (!story.downloads.isNullOrBlank()) {"""

content = content.replace('                            if (!story.downloads.isNullOrBlank()) {', new_block)

with open('shared/src/commonMain/kotlin/com/indieplaybook/app/presentation/screens/appstorydetail/AppStoryDetailScreen.kt', 'w') as f:
    f.write(content)
