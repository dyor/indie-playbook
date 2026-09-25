package com.indieplaybook.app.presentation.components.ads.rewarded

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.indieplaybook.app.presentation.components.ads.AdsConfig
import com.indieplaybook.app.presentation.components.ads.FullScreenAdLoader
import com.indieplaybook.app.util.logging.AppLogger

class RewardedAdLoader(private val context: Context) : FullScreenAdLoader {
    private var isLoading = false

    var rewardedAd: RewardedAd? = null
        set(value) {
            field = value
            if (value == null) {
                isLoading = false
            }
        }

    override fun load() {
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            if (isLoading || rewardedAd != null) {
                return@post
            }
            isLoading = true
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                context,
                AdsConfig.getRewardedAdId(),
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        AppLogger.d("Rewarded ad is loaded")
                        rewardedAd = ad
                        isLoading = false
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        AppLogger.e("Error loading rewarded ad: ${adError.message}")
                        rewardedAd = null
                        isLoading = false
                    }
                },
            )
        }
    }
}
