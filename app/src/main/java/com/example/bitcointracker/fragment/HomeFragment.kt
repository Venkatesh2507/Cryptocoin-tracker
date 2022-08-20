package com.example.bitcointracker.fragment

import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.bitcointracker.R
import com.example.bitcointracker.adapter.TopLossGainPagerAdapter
import com.example.bitcointracker.adapter.TopMarketAdapter
import com.example.bitcointracker.apis.ApiInterface
import com.example.bitcointracker.apis.ApiUtilities
import com.example.bitcointracker.databinding.ActivityMainBinding
import com.example.bitcointracker.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
   private lateinit var binding: FragmentHomeBinding //use of data binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater) //binding with the home fragment's layout

        getTopCurrencyList()// function is used to get the top crypto currencies in a recyclerview

        setTabLayout()//function sets the tab layout

        return binding.root

    }

    private fun setTabLayout() {
        //function is responsible to set tab layout which contains top gainers and losers
        //setting the adapter
        val adapter = TopLossGainPagerAdapter(this);
        binding.contentViewPager.adapter = adapter
        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position==0){
                    binding.topGainIndicator.visibility = VISIBLE
                    binding.topLoseIndicator.visibility = GONE
                }
                else{
                    binding.topGainIndicator.visibility = GONE
                    binding.topLoseIndicator.visibility = VISIBLE
                }
            }
        })
        //setting up the tab layout for top gainers and losers
        TabLayoutMediator(binding.tabLayout,binding.contentViewPager){
            tab,position->
            var title = if(position==0){
                "Top Gainers"
            }
            else{
                "Top Losers"
            }
            tab.text = title

        }.attach()


    }

    private fun getTopCurrencyList() {
        //function is used to load the top crypto currencies in recyclerview through the API call
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            withContext(Dispatchers.Main){                        //setting up the recyclerview with the crypto through the API call
                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(),res.body()!!.data.cryptoCurrencyList)
            }


            Log.d("CurrencyList", "getTopCurrencyList: ${res.body()!!.data.cryptoCurrencyList}")
        }

    }
}