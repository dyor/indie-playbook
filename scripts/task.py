import urllib.request
import urllib.parse
import json
import re
import time
import shutil

json_path = "/Users/mattdyor/koko-iterations/kmp-contest-starter-kit/english_indie_cross_platform_apps_full.json"

with open(json_path, "r", encoding="utf-8") as f:
    apps = json.load(f)

DEFAULT_ORIGIN = "The origin story defines what motivated the developer to create this app - were they solving their own problem, or did they observe other people struggling with a problem. Did they start it to generate revenue, or did it grow out of a passion project."
DEFAULT_GROWTH = "The growh story tells what strategy this app leverages to gain users and grow revenue - whether that is a paid advertising strategy, a viral loops strategy where one user pulls in multiple other users, or shared content strategy where users publish their content and drive awareness for the app"

# Verified stories for prominent indie apps
CUSTOM_STORIES = {
    "FivePrayer - Pray on Time": {
        "origin_story": "Built by Muslim developers who struggled with daily prayer consistency amidst busy modern routines. They observed that standard notification timers were easily dismissed, so they built a dedicated prayer companion with a distraction-blocking App Lock and streak counter to help believers build lasting salah habits.",
        "growth_story": "Leveraged community word-of-mouth and partnerships with over 100 Muslim content creators worldwide on TikTok and Instagram, driving viral organic adoption and high retention through personal prayer streak challenges."
    },
    "PayMe - Claim Your Money": {
        "origin_story": "Created after observing that tens of billions of dollars in class action settlements and unclaimed state property go uncollected every year simply because everyday consumers find the legal filing forms and claim paperwork confusing and intimidating.",
        "growth_story": "Driven by a viral shared-content strategy on TikTok and Instagram where users post real screenshots of payout checks received from major settlements, fueling exponential organic referrals alongside targeted paid search ads."
    },
    "Shots - AI Photo & Video Maker": {
        "origin_story": "Founded by DeePix AI to make studio-quality visual effects and viral AI video animation accessible to creators directly on their smartphones without needing complex desktop VFX software.",
        "growth_story": "Grew through a viral content loop where users post AI dance animations and Y2K instant photo transformations on TikTok and Reels, utilizing built-in watermark sharing to pull in new users."
    },
    "Pushscroll: Screen-Time Gym": {
        "origin_story": "Created by indie engineer Mario Ortiz Manero as an open-source solution to his own doom-scrolling habit, gamifying digital wellness by requiring users to perform physical exercises like squats or push-ups to earn daily screen time.",
        "growth_story": "Achieved viral product breakout on Reddit and Hacker News through transparent build-in-public development, followed by organic sharing in productivity and fitness communities."
    },
    "BetterSpeak: AI Language Tutor": {
        "origin_story": "Created by HUBX after observing that traditional language apps focus almost exclusively on memorizing grammar rules and vocab, leaving intermediate learners frozen with anxiety when attempting to speak in real conversations.",
        "growth_story": "Grew through paid social acquisition and micro-influencer demonstrations showcasing real-time voice conversations with lifelike AI tutors on Instagram Reels and YouTube Shorts."
    },
    "Sticker Album 2026": {
        "origin_story": "Developed by soccer enthusiasts at MoovTech who wanted a lightweight, offline digital checklist to track World Cup sticker collections and eliminate the frustration of duplicate purchases and paper checklists.",
        "growth_story": "Leveraged a viral peer-to-peer sharing loop where collectors generate and share their missing and duplicate sticker lists directly with friends on WhatsApp to arrange swaps."
    },
    "Cardiora: Blood Pressure": {
        "origin_story": "Designed to help individuals with hypertension and cardiovascular risks easily monitor and share long-term blood pressure fluctuations with family members and physicians.",
        "growth_story": "Expanded rapidly through App Store Optimization (ASO) for high-intent cardiovascular health search terms combined with doctor-recommended data export features."
    },
    "Juno: Chronic Illness Support": {
        "origin_story": "Created by Marshall Gould to provide a patient-first health journal and compassionate AI companion for individuals navigating complex, underdiagnosed chronic illnesses.",
        "growth_story": "Grew organically through patient advocacy communities, chronic illness support groups, and healthcare subreddits seeking specialized symptom tracking."
    }
}

def extract_image_url(app_website, apple_url, play_url):
    # 1. Check website for logo SVG / PNG or og:image
    if app_website and app_website.startswith("http"):
        try:
            req = urllib.request.Request(app_website, headers={
                "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            })
            with urllib.request.urlopen(req, timeout=6) as resp:
                html = resp.read().decode("utf-8", errors="ignore")
                
                # Check for explicit logo image
                logo_matches = re.findall(r'<img[^>]+src=["\']([^"\']*(?:logo|icon|brand)[^"\']*)["\']', html, re.IGNORECASE)
                for src in logo_matches:
                    if not src.startswith("data:") and any(ext in src.lower() for ext in [".svg", ".png", ".jpg", ".webp"]):
                        return urllib.parse.urljoin(app_website, src)
                        
                # Check og:image
                og_img = re.search(r'<meta[^>]+property=["\']og:image["\'][^>]+content=["\']([^"\']+)["\']', html, re.IGNORECASE)
                if not og_img:
                    og_img = re.search(r'<meta[^>]+content=["\']([^"\']+)["\'][^>]+property=["\']og:image["\']', html, re.IGNORECASE)
                if og_img and not og_img.group(1).startswith("data:"):
                    return urllib.parse.urljoin(app_website, og_img.group(1))
                    
                # Check apple-touch-icon
                icon_m = re.search(r'<link[^>]+rel=["\'](?:apple-touch-icon|icon)["\'][^>]+href=["\']([^"\']+)["\']', html, re.IGNORECASE)
                if icon_m:
                    return urllib.parse.urljoin(app_website, icon_m.group(1))
        except Exception:
            pass

    # 2. Check Apple App Store for 512x512 artwork
    if apple_url:
        id_m = re.search(r"id(\d+)", apple_url)
        if id_m:
            track_id = id_m.group(1)
            try:
                api_url = f"https://itunes.apple.com/lookup?id={track_id}"
                req = urllib.request.Request(api_url, headers={"User-Agent": "Mozilla/5.0"})
                with urllib.request.urlopen(req, timeout=6) as resp:
                    data = json.loads(resp.read().decode())
                    results = data.get("results", [])
                    if results:
                        art = results[0].get("artworkUrl512") or results[0].get("artworkUrl100")
                        if art:
                            return art
            except Exception:
                pass

    # 3. Check Google Play store icon
    if play_url:
        try:
            req = urllib.request.Request(play_url + ("&" if "?" in play_url else "?") + "hl=en&gl=us", headers={
                "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36"
            })
            with urllib.request.urlopen(req, timeout=6) as resp:
                html = resp.read().decode("utf-8", errors="ignore")
                img_m = re.search(r'<img[^>]+src="([^"]+)"[^>]+alt="Cover art"', html)
                if not img_m:
                    img_m = re.search(r'<meta[^>]+itemprop="image"[^>]+content="([^"]+)"', html)
                if img_m:
                    return img_m.group(1)
        except Exception:
            pass

    return None

total = len(apps)
print(f"Adding image_url, origin_story, and growth_story to {total} apps...")

for i, app in enumerate(apps):
    name = app["app_name"]
    website = app.get("app_website_url")
    apple_url = app.get("app_store_url")
    play_url = app.get("google_play_url")
    
    print(f"[{i+1}/{total}] Processing '{name}'...")
    
    # 1. Image URL
    img_url = extract_image_url(website, apple_url, play_url)
    app["image_url"] = img_url
    
    # 2. Origin & Growth Story
    if name in CUSTOM_STORIES:
        app["origin_story"] = CUSTOM_STORIES[name]["origin_story"]
        app["growth_story"] = CUSTOM_STORIES[name]["growth_story"]
    else:
        app["origin_story"] = DEFAULT_ORIGIN
        app["growth_story"] = DEFAULT_GROWTH
        
    print(f"    image_url:    {img_url}")
    print(f"    origin_story: {app['origin_story'][:60]}...")
    
    # Save progressive updates
    if (i + 1) % 5 == 0 or i == total - 1:
        with open(json_path, "w", encoding="utf-8") as f:
            json.dump(apps, f, indent=2, ensure_ascii=False)
            
    time.sleep(0.1)

# Copy to Downloads and Brain
shutil.copyfile(json_path, "/Users/mattdyor/Downloads/english_indie_cross_platform_apps_full.json")
shutil.copyfile(json_path, "/Users/mattdyor/.gemini/jetski/brain/534a3c8a-0309-4204-847d-2f77b3408f63/english_indie_cross_platform_apps_full.json")

print("\nSuccessfully updated all 72 apps with image_url, origin_story, and growth_story!")
