package com.example.bitcointracker.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.GONE
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.example.bitcointracker.R
import com.example.bitcointracker.adapter.MarketAdapter
import com.example.bitcointracker.apis.ApiInterface
import com.example.bitcointracker.apis.ApiUtilities
import com.example.bitcointracker.databinding.FragmentMarketBinding
import com.example.bitcointracker.modals.CryptoCurrencyListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList
class MarketFragment : Fragment() {
    private lateinit var binding: FragmentMarketBinding //use of data binding
    private lateinit var list: List<CryptoCurrencyListItem>
    private lateinit var adapter: MarketAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarketBinding.inflate(layoutInflater)
        list = listOf()
        adapter = MarketAdapter(requireContext(),list,"market")
        binding.currencyRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData() //crypto currencies are loaded in the recyclerview through the API call
            if(res.body()!=null){
                withContext(Dispatchers.Main){
                    list = res.body()!!.data.cryptoCurrencyList!!
                    adapter.updateData(list)
                    binding.spinKitView.visibility = GONE
                }
            }
        }
          searchCoins(); //function used to search the crypto coin
        return binding.root

    }
 lateinit var searchText: String //initializing a string which is used to store the text entered in the editText
    private fun searchCoins() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().lowercase(Locale.getDefault())
                //this function is used to load the searched crypto coin
                updateRecyclerView();
            }

        })
    }
    private fun updateRecyclerView() {

        val data = ArrayList<CryptoCurrencyListItem>()
        list.forEach{
            val coinName = it.name.lowercase(Locale.getDefault()) //getting all the coin name of the searched coin
            val coinSymbol = it.symbol.lowercase(Locale.getDefault())//getting all the coin symbol for the searched coin
            //condition which is used to check weather the searched text is present in the coin name and the symbol if present the data will be added in the list
            if(coinName.contains(searchText)||coinSymbol.contains(searchText)){
                data.add(it)
            }
            binding.currencyRecyclerView.adapter!!.notifyDataSetChanged()
        }
        adapter.updateData(data) //adapter is updated with the searched results
     }

}