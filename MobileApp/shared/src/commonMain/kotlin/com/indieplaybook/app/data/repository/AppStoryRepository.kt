package com.indieplaybook.app.data.repository

import com.indieplaybook.app.data.source.local.dao.AppStoryDao
import com.indieplaybook.app.data.source.local.entity.AppStoryEntity
import com.indieplaybook.app.data.source.local.entity.toModel
import com.indieplaybook.app.domain.model.AppStory
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppStoryRepository(
    private val appStoryDao: AppStoryDao
) {
    fun getAllStories(): Flow<List<AppStory>> {
        return appStoryDao.getAllFlow().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getStoryById(id: String): Flow<AppStory?> {
        return appStoryDao.getByIdFlow(id).map { entity ->
            entity?.toModel()
        }
    }

    suspend fun toggleBookmark(id: String) {
        appStoryDao.toggleBookmark(id)
    }

    suspend fun fetchFromFirebaseAndSyncLocal() {
        val firestore = Firebase.firestore

        try {
            val response = firestore.collection("app_stories").get()

            val remoteStories = response.documents.mapNotNull { doc ->
                val id = doc.get("id") as? String
                val name = doc.get("name") as? String
                val oneLiner = doc.get("oneLiner") as? String
                val techStack = doc.get("techStack") as? String
                val originStory = doc.get("originStory") as? String
                val growthPlaybook = doc.get("growthPlaybook") as? String
                val iconUrl = doc.get("iconUrl") as? String

                if (id != null && name != null && oneLiner != null && techStack != null && originStory != null && growthPlaybook != null && iconUrl != null) {
                    AppStoryEntity(
                        id = id,
                        name = name,
                        oneLiner = oneLiner,
                        techStack = techStack,
                        originStory = originStory,
                        growthPlaybook = growthPlaybook,
                        iconUrl = iconUrl,
                        isBookmarked = false,
                        publisher = doc.get("publisher") as? String,
                        releaseDate = doc.get("releaseDate") as? String,
                        category = doc.get("category") as? String,
                        downloads = doc.get("downloads") as? String,
                        revenue = doc.get("revenue") as? String
                    )
                } else null
            }

            if (remoteStories.isNotEmpty()) {
                appStoryDao.deleteAll()
                appStoryDao.insertAll(remoteStories)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun seedInitialDataIfEmpty() {
        val existingStories = appStoryDao.getAll()

        val hasOldDummyData = existingStories.any { it.id == "1" || it.id == "2" || it.id == "3" || it.id == "4" }

        if (existingStories.isEmpty() || hasOldDummyData) {
            appStoryDao.deleteAll()

            val fullStories = listOf(
                AppStoryEntity(
                    id = "shots-ai-photo-video-maker",
                    name = "Shots - AI Photo & Video Maker",
                    oneLiner = "AI photo and video generator by DeePix AI",
                    techStack = "React Native",
                    originStory = "Created by DeePix AI to give creators studio-quality photo and video transformations directly from their phones.",
                    growthPlaybook = "Leveraged Instagram Reels and TikTok AI trend showcases to gain rapid early traction.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "DeePix AI",
                    releaseDate = "2025-06-04",
                    category = "Photo & Video",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "fiveprayer-pray-on-time",
                    name = "FivePrayer - Pray on Time",
                    oneLiner = "Minimalist, precise prayer companion by FivePrayer",
                    techStack = "Flutter",
                    originStory = "Developed by FivePrayer to provide distraction-free, privacy-focused prayer times and direction guidance.",
                    growthPlaybook = "Word-of-mouth growth through community recommendation channels and local App Store optimization.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "FivePrayer",
                    releaseDate = "2026-02-08",
                    category = "Lifestyle",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "cardiora-blood-pressure",
                    name = "Cardiora: Blood Pressure",
                    oneLiner = "Heart health & blood pressure log by Tam Tran",
                    techStack = "Flutter",
                    originStory = "Built by indie developer Tam Tran to help users easily track cardiovascular metrics with clear charts.",
                    growthPlaybook = "Optimized for health and fitness search keywords with multi-language localization.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Tam Tran",
                    releaseDate = "2025-10-08",
                    category = "Health & Fitness",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "sticker-album-2026",
                    name = "Sticker Album 2026",
                    oneLiner = "Digital trading sticker collection by MoovTech",
                    techStack = "React Native",
                    originStory = "Created by MoovTech for sports enthusiasts to collect, trade, and organize digital sticker albums during major tournaments.",
                    growthPlaybook = "Gamified referral mechanics where users trade duplicate stickers with friends.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "MoovTech",
                    releaseDate = "2026-04-20",
                    category = "Sports",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "pluxee",
                    name = "Pluxee",
                    oneLiner = "Lifestyle benefits and rewards by Pluxee International",
                    techStack = "Flutter",
                    originStory = "Built by Pluxee International to consolidate employee benefits, merchant vouchers, and rewards into a seamless mobile wallet.",
                    growthPlaybook = "B2B distribution through corporate benefits packages and HR partnerships.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Pluxee International",
                    releaseDate = "2025-06-02",
                    category = "Lifestyle",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "betterspeak-ai-language-tutor",
                    name = "BetterSpeak: AI Language Tutor",
                    oneLiner = "Conversational AI language practice by HUBX",
                    techStack = "React Native",
                    originStory = "Developed by HUBX to give language learners realistic, pressure-free speaking practice with real-time AI feedback.",
                    growthPlaybook = "Targeted performance marketing on social media showcasing interactive AI voice conversations.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "HUBX",
                    releaseDate = "2025-08-08",
                    category = "Education",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "payme-claim-your-money",
                    name = "PayMe - Claim Your Money",
                    oneLiner = "Unclaimed property and refund finder by Control. Alt. Delete. LLC",
                    techStack = "React Native",
                    originStory = "Built by Control. Alt. Delete. LLC to aggregate public databases and help users discover unclaimed funds.",
                    growthPlaybook = "Viral video demonstrations on TikTok showing users finding real missing money live on screen.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Control. Alt. Delete. LLC",
                    releaseDate = "2025-08-11",
                    category = "Finance",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "yandex-ai-chatbot-assistant",
                    name = "yandex ai: chatbot & assistant",
                    oneLiner = "Cross-platform smart assistant built with KMP",
                    techStack = "Kotlin Multiplatform",
                    originStory = "Engineered using Kotlin Multiplatform to share intelligent assistant logic across desktop, web, and mobile clients.",
                    growthPlaybook = "Ecosystem integration and search engine placement across existing user touchpoints.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Direct Cursus Computer Systems Trading",
                    releaseDate = "2025-12-16",
                    category = "Productivity",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "scrambly-rewards-for-steps",
                    name = "Scrambly: Rewards for Steps",
                    oneLiner = "Gamified step counter and activity rewards by Scrambly",
                    techStack = "React Native",
                    originStory = "Created by Scrambly to incentivize healthy habits through real-world rewards and challenges.",
                    growthPlaybook = "User referral loops offering step boosts when inviting friends and family.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Scrambly",
                    releaseDate = "2025-07-10",
                    category = "Lifestyle",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "govauctions-shop-surplus",
                    name = "GovAuctions.com - Shop Surplus",
                    oneLiner = "Government surplus and seized property marketplace",
                    techStack = "React Native",
                    originStory = "Developed by Auctions to give deal hunters direct access to government surplus auctions.",
                    growthPlaybook = "SEO strategies targeting deal-seeking bargain hunters and thrift communities.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Auctions",
                    releaseDate = "2025-09-26",
                    category = "Shopping",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "her-75",
                    name = "Her 75",
                    oneLiner = "Fitness & mental toughness challenge for women",
                    techStack = "React Native",
                    originStory = "Created by My Viral Agent to tailor the popular 75-hard style challenge specifically to women's health and wellness goals.",
                    growthPlaybook = "Strong Instagram community building with daily accountability tag challenges.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "My Viral Agent",
                    releaseDate = "2025-06-27",
                    category = "Health & Fitness",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "posely-ai-photo-creator",
                    name = "Posely – AI Photo Creator",
                    oneLiner = "AI portrait and photoshoot generator by Soldd",
                    techStack = "React Native",
                    originStory = "Built by Soldd to generate professional headshots and creative portraits using fine-tuned diffusion models.",
                    growthPlaybook = "Influencer collaborations showing professional headshot transformations for LinkedIn.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Soldd",
                    releaseDate = "2025-11-20",
                    category = "Photo & Video",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "pecra-pro-camera-finish",
                    name = "Pecra: Pro Camera Finish",
                    oneLiner = "Pro color grading and vintage camera simulation by Abnous Nayyeri",
                    techStack = "React Native",
                    originStory = "Designed by indie creator Abnous Nayyeri for photographers who want film emulation on mobile.",
                    growthPlaybook = "Featured in mobile photography forums and shared aesthetic preset downloads on Pinterest.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Abnous Nayyeri",
                    releaseDate = "2026-01-17",
                    category = "Photo & Video",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "movies-hub-swipe-and-like",
                    name = "Movies Hub - Swipe and Like",
                    oneLiner = "Tinder for movie night decisions by CJ APPS",
                    techStack = "React Native",
                    originStory = "Built by CJ APPS to solve the endless scrolling debate when couples or friends pick a movie to watch.",
                    growthPlaybook = "Relatable meme marketing on Reddit (r/movies) and TikTok about couples struggling to pick movies.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "CJ APPS",
                    releaseDate = "2025-09-07",
                    category = "Entertainment",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "aleem-english-with-ai",
                    name = "Aleem - English with AI",
                    oneLiner = "AI-powered English tutor by Asylzhan Altay",
                    techStack = "React Native",
                    originStory = "Created by Kazakh developer Asylzhan Altay to make high-quality English tutoring accessible to non-native speakers.",
                    growthPlaybook = "Targeted localization and educational grants in emerging non-English markets.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Asylzhan Altay",
                    releaseDate = "2025-10-15",
                    category = "Education",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "protube-block-ads-on-video",
                    name = "ProTube: Block Ads on Video",
                    oneLiner = "Distraction-free video browser by Carla Berti",
                    techStack = "React Native",
                    originStory = "Built by Carla Berti to offer a fast, clean video playback experience without intrusive ad interruptions.",
                    growthPlaybook = "Organic search positioning for utility keywords and privacy tech blogs.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Carla Berti",
                    releaseDate = "2025-07-28",
                    category = "Photo & Video",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "pushscroll-screen-time-gym",
                    name = "Pushscroll: Screen-Time Gym",
                    oneLiner = "Earn doomscroll time by doing pushups (Built with KMP)",
                    techStack = "Kotlin Multiplatform",
                    originStory = "Indie developer Mario Ortiz Manero built Pushscroll using KMP to force users to exercise before unlocking social media apps.",
                    growthPlaybook = "Went viral on Hacker News and Product Hunt as a unique digital detox utility.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Mario Ortiz Manero",
                    releaseDate = "2025-06-24",
                    category = "Health & Fitness",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "lyra-music-radio-esound",
                    name = "Lyra - Music, Radio, eSound",
                    oneLiner = "Free background music & radio stream player by Nadia Ferrari",
                    techStack = "React Native",
                    originStory = "Created by Nadia Ferrari to offer a lightweight offline-friendly music streaming alternative.",
                    growthPlaybook = "App Store optimization targeting free music player search queries.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Nadia Ferrari",
                    releaseDate = "2025-06-25",
                    category = "Music",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "shortical",
                    name = "Shortical",
                    oneLiner = "Short-form drama and bite-sized stories (KMP)",
                    techStack = "Kotlin Multiplatform",
                    originStory = "Engineered with KMP by Short Entertainment to deliver high-performance video streaming across iOS and Android.",
                    growthPlaybook = "Cliffhanger social media clips driving users to app for the next episode.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Short Entertainment",
                    releaseDate = "2025-06-12",
                    category = "Entertainment",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "minglotalk-ai-character-chat",
                    name = "MingloTalk: AI Character Chat",
                    oneLiner = "Interactive AI persona chat by Brad Schulte",
                    techStack = "Flutter",
                    originStory = "Built by indie dev Brad Schulte to allow users to roleplay and converse with customizable AI personalities.",
                    growthPlaybook = "Community-generated character sharing on Discord and Reddit.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Brad Schulte",
                    releaseDate = "2026-04-21",
                    category = "Entertainment",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "eaze-app-talk-and-feel-better",
                    name = "Eaze App: Talk & Feel better",
                    oneLiner = "Anonymous mental wellness and vent spaces",
                    techStack = "React Native",
                    originStory = "Created by Lokal to provide a safe, judgement-free community for expressing emotional struggles.",
                    growthPlaybook = "Partnerships with student mental health advocates and peer support groups.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Lokal Made in India Local Updates",
                    releaseDate = "2025-07-28",
                    category = "Social Networking",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "vigorbuy-chinas-best-yours",
                    name = "Vigorbuy: China’s Best, Yours",
                    oneLiner = "Global shopping agent and forwarding suite",
                    techStack = "Flutter",
                    originStory = "Built by Vigorbuy to simplify purchasing products directly from Chinese manufacturers for overseas buyers.",
                    growthPlaybook = "Community guides on Reddit (r/FashionReps, r/Couriers) showcasing unboxing hauls.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Vigorbuy",
                    releaseDate = "2025-09-23",
                    category = "Shopping",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "disha-ai-health-coach",
                    name = "Disha: AI Health Coach",
                    oneLiner = "Personalized metabolic health and diabetes management",
                    techStack = "Flutter",
                    originStory = "Developed by CURELINK PRIVATE LIMITED to assist diabetic patients with continuous glucose insights.",
                    growthPlaybook = "Endorsements from clinic partners and healthcare webinars.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "CURELINK PRIVATE LIMITED",
                    releaseDate = "2025-07-03",
                    category = "Health & Fitness",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "v2ray-client-plus",
                    name = "V2Ray Client+",
                    oneLiner = "High-speed encrypted network tunnel utility (KMP)",
                    techStack = "Kotlin Multiplatform",
                    originStory = "Built by Digital Ejendomsservice ApS using KMP to share low-level network protocol handlers across OS targets.",
                    growthPlaybook = "Privacy blog reviews and GitHub community recommendation.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Digital Ejendomsservice ApS",
                    releaseDate = "2025-07-14",
                    category = "Travel",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "formo-calorie-counter",
                    name = "Formo - Calorie Counter",
                    oneLiner = "Clean, fast macronutrient logging by Ruslan Moroziuk",
                    techStack = "Flutter",
                    originStory = "Designed by Ruslan Moroziuk for fitness enthusiasts wanting friction-free macro tracking.",
                    growthPlaybook = "#BuildInPublic updates on Twitter/X and fitness subreddit outreach.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Ruslan Moroziuk",
                    releaseDate = "2026-01-20",
                    category = "Health & Fitness",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "ai-maker-photo-art-generator",
                    name = "AI Maker: Photo Art Generator",
                    oneLiner = "Artistic style transfer and avatar generator (KMP)",
                    techStack = "Kotlin Multiplatform",
                    originStory = "Created by Hungry Birds using KMP to optimize image filtering pipelines across mobile devices.",
                    growthPlaybook = "Social sharing badges allowing users to share AI avatars directly to Instagram.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Hungry Birds",
                    releaseDate = "2025-11-16",
                    category = "Photo & Video",
                    downloads = null,
                    revenue = null
                ),
                AppStoryEntity(
                    id = "wbuilds-for-building-guide",
                    name = "WBuilds for Building Guide",
                    oneLiner = "Gamer loadout and build guide companion (KMP)",
                    techStack = "Kotlin Multiplatform",
                    originStory = "Built by indie developer Sagar Khurana using KMP for fast, offline-first access to game strategy guides.",
                    growthPlaybook = "SEO traffic from gaming strategy searches and Discord communities.",
                    iconUrl = "",
                    isBookmarked = false,
                    publisher = "Sagar Khurana",
                    releaseDate = "2025-12-11",
                    category = "Reference",
                    downloads = null,
                    revenue = null
                )
            )
            appStoryDao.insertAll(fullStories)
        }
    }
}
