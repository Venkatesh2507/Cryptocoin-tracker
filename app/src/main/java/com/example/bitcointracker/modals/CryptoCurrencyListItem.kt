package com.example.bitcointracker.modals

import java.io.Serializable

data class CryptoCurrencyListItem(val symbol: String = "",
                                  val selfReportedCirculatingSupply: Double = 0.0,
                                  val totalSupply: Double = 0.0,
                                  val cmcRank: Double = 0.0,
                                  val isActive: Double = 0.0,
                                  val circulatingSupply: Double = 0.0,
                                  val dateAdded: String = "",
                                  val tags: List<String>?,
                                  val quotes: List<QuotesItem>?,
                                  val lastUpdated: String = "",
                                  val isAudited: Boolean = false,
                                  val name: String = "",
                                  val marketPairCount: Double = 0.0,
                                  val id: Int = 0,
                                  val maxSupply: Double = 0.0,
                                  val slug: String = ""): Serializable{
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}
