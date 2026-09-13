import json

def convert_to_kotlin(json_file):
    with open(json_file, "r") as f:
        data = json.load(f)

    print("val fullStories = listOf(")
    
    for item in data:
        print("    AppStoryEntity(")
        
        # Helper to escape strings properly
        def escape(s):
            if s is None:
                return ""
            return str(s).replace('"', '\\"').replace('\n', '\\n')

        # Helper to emit a string field
        def emit_str(field, name=None):
            if name is None:
                name = field
            val = item.get(field)
            if val is not None:
                print(f'        {name} = "{escape(val)}",')
        
        # Helper to emit a raw field
        def emit_raw(field, name=None):
            if name is None:
                name = field
            val = item.get(field)
            if val is not None:
                print(f'        {name} = {val},')

        emit_str('id')
        emit_str('name')
        emit_str('oneLiner')
        emit_str('techStack')
        emit_str('originStory')
        emit_str('growthPlaybook')
        emit_str('iconUrl')
        
        print("        isBookmarked = false,")
        
        emit_str('downloads')
        emit_str('revenue')
        emit_str('publisher')
        emit_str('releaseDate')
        emit_str('category')
        emit_str('googlePlayUrl')
        emit_str('appStoreUrl')
        emit_str('googlePlayReviews')
        emit_raw('googlePlayRating')
        emit_str('googlePlayAbout')
        emit_raw('appStoreRating')
        emit_raw('appStoreReviews')
        emit_str('appStoreSize')
        emit_raw('appStoreChartRank')
        emit_str('appStoreChartCategory')
        emit_str('appStoreAbout')
        emit_str('appStoreAgeRating')
        emit_str('appStorePrivacyPolicy')
        emit_str('googlePlayPrivacyPolicy')
        emit_str('appWebsiteUrl')
        
        print("    ),")
        
    print(")")

convert_to_kotlin("../english_indie_cross_platform_apps_full.json")
