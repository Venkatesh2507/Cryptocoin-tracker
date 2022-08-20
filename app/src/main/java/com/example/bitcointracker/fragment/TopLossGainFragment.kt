package com.example.bitcointracker.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.bitcointracker.adapter.MarketAdapter
import com.example.bitcointracker.apis.ApiInterface
import com.example.bitcointracker.apis.ApiUtilities
import com.example.bitcointracker.databinding.FragmentTopLossGainBinding
import com.example.bitcointracker.modals.CryptoCurrencyListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class TopLossGainFragment : Fragment() {
    lateinit var binding: FragmentTopLossGainBinding //use of data binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopLossGainBinding.inflate(layoutInflater) //data is binded with the top loss gain layout
        //function is used to get all the details of coins through API call
        getMarketData();
        return binding.root

    }

    private fun getMarketData() {
       val position = requireArguments().getInt("position")
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData() //crypto currencies are loaded in the recyclerview through the API call
            if(res.body()!=null){
                withContext(Dispatchers.Main){
                    val dataitem = res.body()!!.data.cryptoCurrencyList
                    Collections.sort(dataitem){
                        n1,n2 -> (n2.quotes!![0].percentChange24h.toInt()) //the current price of the coins are compared and placed into the top gainers and losers
                        .compareTo(n1.quotes!![0].percentChange24h.toInt())
                    }
                    binding.spinKitView.visibility = GONE
                    val list =  ArrayList<CryptoCurrencyListItem>()
                    if(position==0){
                        list.clear()
                        for(i in 0..9){
                            list.add(dataitem!![i])
                        }
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )

                    }
                    else{
                        list.clear()
                        for(i in 0..9){
                            list.add(dataitem!![dataitem.size-1-i])
                        }
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )
                    }

                }

            }
        }

    }
}