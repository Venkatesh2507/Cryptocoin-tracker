package com.example.bitcointracker.modals

data class QuotesItem(val marketCap: Double = 0.0,
                      val volumeH: Double = 0.0,
                      val marketCapByTotalSupply: Double = 0.0,
                      val percentChange30d: Double=0.0,
                      val lastUpdated: String = "",
                      val percentChange1h: Double = 0.0,
                      val percentChange60d: Double = 0.0,
                      val price: Double = 0.0,
                      val ytdPriceChangePercentage: Double = 0.0,
                      val percentChange7d: Double = 0.0,
                      val name: String = "",
                      val percentChange90d: Double = 0.0,
                      val dominance: Double = 0.0,
                      val fullyDilluttedMarketCap: Double = 0.0,
                      val percentChange24h: Double = 0.0,
                      val turnover: Double = 0.0)