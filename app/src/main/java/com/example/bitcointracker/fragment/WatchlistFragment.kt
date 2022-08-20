package com.example.bitcointracker.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.bitcointracker.R
import com.example.bitcointracker.adapter.MarketAdapter
import com.example.bitcointracker.apis.ApiInterface
import com.example.bitcointracker.apis.ApiUtilities
import com.example.bitcointracker.databinding.FragmentWatchlistBinding
import com.example.bitcointracker.modals.CryptoCurrencyListItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistFragment : Fragment() {
    lateinit var binding: FragmentWatchlistBinding //use of data binding
    lateinit var watchList: ArrayList<String>
    lateinit var watchListItem: ArrayList<CryptoCurrencyListItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchlistBinding.inflate(layoutInflater)// data binded with the watchlist layout
        //function is used to store all the currencies in the watchlist
        readData()
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()  //crypto currencies are loaded in the recyclerview through the API call
            if(res!=null){
                withContext(Dispatchers.Main) {
                    watchListItem = ArrayList()
                    watchListItem.clear()
                    for (watchData in watchList) {
                        for (item in res.body()!!.data.cryptoCurrencyList!!) {
                            if (watchData == item.symbol) {
                                watchListItem.add(item)
                            }
                        }
                    }
                   binding.spinKitView.visibility = GONE
                    binding.watchlistRecyclerView.adapter = MarketAdapter(requireContext(),watchListItem,"watchFragment")
                }
            }

        }


        return binding.root

    }
    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchList", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchList",ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchList = gson.fromJson(json,type)

    }
}
