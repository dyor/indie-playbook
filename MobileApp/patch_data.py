import json
import re

with open("../english_indie_cross_platform_apps_full.json", "r") as f:
    data = json.load(f)

with open("shared/src/commonMain/kotlin/com/indieplaybook/app/data/repository/AppStoryRepository.kt", "r") as f:
    content = f.read()

# Find the start of the list
start_match = re.search(r'val fullStories = listOf\(', content)
if not start_match:
    print("Could not find start of list")
    exit(1)

# Find the end of the list
end_idx = content.find('            )\n            appStoryDao.insertAll(fullStories)')
if end_idx == -1:
    print("Could not find end of list")
    exit(1)

start_idx = start_match.start()

new_list = "val fullStories = listOf(\n"

def escape(s):
    if s is None:
        return ""
    return str(s).replace('"', '\\"').replace('\n', '\\n')

for item in data:
    new_list += "                AppStoryEntity(\n"
    
    val = item.get('id')
    if val is not None: new_list += f'                    id = "{escape(val)}",\n'
    
    val = item.get('app_name')
    if val is not None: new_list += f'                    name = "{escape(val)}",\n'
    
    val = item.get('oneLiner')
    if val is not None: 
        new_list += f'                    oneLiner = "{escape(val)}",\n'
    else:
        new_list += f'                    oneLiner = "",\n'
    
    val = item.get('framework')
    if val is not None: new_list += f'                    techStack = "{escape(val)}",\n'
    
    val = item.get('origin_story')
    if val is not None: new_list += f'                    originStory = "{escape(val)}",\n'
    
    val = item.get('growth_story')
    if val is not None: new_list += f'                    growthPlaybook = "{escape(val)}",\n'
    
    val = item.get('image_url')
    if val is not None: 
        new_list += f'                    iconUrl = "{escape(val)}",\n'
    else:
        new_list += f'                    iconUrl = "",\n'
    
    new_list += "                    isBookmarked = false,\n"
    
    val = item.get('google_play_downloads')
    if val is not None: new_list += f'                    downloads = "{escape(val)}",\n'
    
    val = item.get('revenue')
    if val is not None: new_list += f'                    revenue = "{escape(val)}",\n'
    
    val = item.get('publisher')
    if val is not None: new_list += f'                    publisher = "{escape(val)}",\n'
    
    val = item.get('release_date')
    if val is not None: new_list += f'                    releaseDate = "{escape(val)}",\n'
    
    val = item.get('category')
    if val is not None: new_list += f'                    category = "{escape(val)}",\n'
    
    val = item.get('google_play_url')
    if val is not None: new_list += f'                    googlePlayUrl = "{escape(val)}",\n'
    
    val = item.get('app_store_url')
    if val is not None: new_list += f'                    appStoreUrl = "{escape(val)}",\n'
    
    val = item.get('google_play_reviews')
    if val is not None: new_list += f'                    googlePlayReviews = "{escape(val)}",\n'
    
    val = item.get('google_play_rating')
    if val is not None: new_list += f'                    googlePlayRating = {val},\n'
    
    val = item.get('google_play_about')
    if val is not None: new_list += f'                    googlePlayAbout = "{escape(val)}",\n'
    
    val = item.get('app_store_rating')
    if val is not None: new_list += f'                    appStoreRating = {val},\n'
    
    val = item.get('app_store_reviews')
    if val is not None: new_list += f'                    appStoreReviews = {val},\n'
    
    val = item.get('app_store_size')
    if val is not None: new_list += f'                    appStoreSize = "{escape(val)}",\n'
    
    val = item.get('app_store_chart_rank')
    if val is not None: new_list += f'                    appStoreChartRank = {val},\n'
    
    val = item.get('app_store_chart_category')
    if val is not None: new_list += f'                    appStoreChartCategory = "{escape(val)}",\n'
    
    val = item.get('app_store_about')
    if val is not None: new_list += f'                    appStoreAbout = "{escape(val)}",\n'
    
    val = item.get('app_store_age_rating')
    if val is not None: new_list += f'                    appStoreAgeRating = "{escape(val)}",\n'
    
    val = item.get('app_store_privacy_policy')
    if val is not None: new_list += f'                    appStorePrivacyPolicy = "{escape(val)}",\n'
    
    val = item.get('google_play_privacy_policy')
    if val is not None: new_list += f'                    googlePlayPrivacyPolicy = "{escape(val)}",\n'
    
    val = item.get('app_website_url')
    if val is not None: new_list += f'                    appWebsiteUrl = "{escape(val)}",\n'
    
    new_list += "                ),\n"

new_content = content[:start_idx] + new_list + content[end_idx:]

with open("shared/src/commonMain/kotlin/com/indieplaybook/app/data/repository/AppStoryRepository.kt", "w") as f:
    f.write(new_content)

print("Patched!")
