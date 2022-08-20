package com.example.bitcointracker.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bitcointracker.R
import com.example.bitcointracker.databinding.FragmentDetails2Binding
import com.example.bitcointracker.modals.CryptoCurrencyListItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetails2Binding //use of data binding
    private val item: DetailsFragmentArgs by navArgs() //initializing the item var for navigation using navArgs
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetails2Binding.inflate(layoutInflater) // binding with the layout of details fragment
        val data: CryptoCurrencyListItem = item.data!!
        //if button is pressed, user will be redirected to previous activity
        binding.backStackButton.setOnClickListener{
            requireActivity().onBackPressed()
        }

        setUpDetails(data) //function which is used to setup the crypto details through API

        loadChartView(data) //function which is used to load the chart view

        loadChartViewDetails(data)//function which is used to load the chart view details

        addToWatchlist(data); //function which is used to add the crypto coins into the watchlist

        return binding.root
    }
    var watchList: ArrayList<String>?=null
    var watchListChecked = false

    private fun storeData(){
        //the function uses shared preferences to store the data in the watchlist
        val sharedPreferences = requireContext().getSharedPreferences("watchList",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchList)
        editor.putString("watchList",json)
        editor.apply()

    }

    private fun addToWatchlist(data: CryptoCurrencyListItem) {
        readData();
        watchListChecked = if(watchList!!.contains(data.symbol)){   //this statement checks weather the watchlist is checked or not, if not checked it checks and versa
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        }else{
            watchList!!.contains(data.symbol)
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                false
        }
        //handles the watchlist image button
        binding.addWatchlistButton.setOnClickListener{
            watchListChecked = if(!watchListChecked){ //this statement is used to add the coin int the watchlist and the else part contains to remove the coin added in the watchlist
                if(!watchList!!.contains(data.symbol)){
                    watchList!!.add(data.symbol)
                }
                storeData()
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                true
            }else{
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                watchList!!.remove(data.symbol)
                storeData()
                false
            }
        }

    }

    private fun readData() {
        //this function is used to read the data to be stored in the watchlist
        val sharedPreferences = requireContext().getSharedPreferences("watchList",Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchList",ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchList = gson.fromJson(json,type)

    }




    private fun loadChartViewDetails(data: CryptoCurrencyListItem) {
        //this function is used to get the details of chart of respected coin user has selected through API call
        //binding the data to the respected time views
        val oneMin = binding.button
        val fifteenMin = binding.button5
        val oneHour = binding.button4
        val fourHour = binding.button3
        val oneDay = binding.button2
        val oneWeek = binding.button1

        //this statement is used load the data into the respected time limits
        val clickListner = View.OnClickListener {
            when(it.id){
                oneMin.id->loadChartData(it,"1M",data,oneDay,oneWeek,fourHour,fifteenMin,oneHour)
                fifteenMin.id->loadChartData(it,"15",data,oneDay,oneWeek,fourHour,oneMin,oneHour)
                oneHour.id->loadChartData(it,"1H",data,oneDay,oneWeek,fourHour,fifteenMin,oneMin)
                fourHour.id->loadChartData(it,"4H",data,oneDay,oneWeek,oneMin,fifteenMin,oneHour)
                oneDay.id->loadChartData(it,"D",data,oneMin,oneWeek,fourHour,fifteenMin,oneHour)
                oneWeek.id->loadChartData(it,"W",data,oneDay,oneMin,fourHour,fifteenMin,oneHour)

            }
        }
        //these statements are executed when the user clicks on the respected time limits and the data of that particular time is shown
        fifteenMin.setOnClickListener(clickListner)
        oneHour.setOnClickListener(clickListner)
        oneMin.setOnClickListener(clickListner)
        fourHour.setOnClickListener(clickListner)
        oneDay.setOnClickListener(clickListner)
        oneWeek.setOnClickListener(clickListner)
    }

    private fun loadChartData(
        it: View?,
        s: String,
        item: CryptoCurrencyListItem,
        oneDay: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        fifteenMin: AppCompatButton,
        oneHour: AppCompatButton
    )
    {       //this function is used to load the chart of a particular coin
            disableButton(oneDay,oneWeek,oneHour,fourHour,fifteenMin);//this function is used to disable the other time buttons when the user is on a particular time view
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true

        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol"+ item.symbol
                .toString() + "USD&interval=" + s + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]" +
                    "&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]" +
                    "&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=" +
                    "BTCUSDT"
        )


    }

    private fun disableButton(oneDay: AppCompatButton, oneWeek: AppCompatButton, oneHour: AppCompatButton, fourHour: AppCompatButton, fifteenMin: AppCompatButton) {
            oneDay.background=null
            oneWeek.background=null
            oneHour.background=null
            fourHour.background=null
            fifteenMin.background=null


    }

    private fun loadChartView(item: CryptoCurrencyListItem) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true

        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        //chart is loaded through url and the symbol of the coin is passed to the url
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol"+item.symbol.toString()+"USD&interval=D" +
                    "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]" +
                    "&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}" +
                    "&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap" +
                    ".com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun setUpDetails(data: CryptoCurrencyListItem) {
        binding.detailSymbolTextView.text = data.symbol

        //the symbol of coin is loaded through the glide dependency
        Glide.with(requireContext()).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+data.id+ ".png" )
            .thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        //setting up the price of the crypto coin
        binding.detailPriceTextView.text = "${String.format("$%.04f",data.quotes!![0].price)}"

        //setting the increment/decrement in the current price of the coin
        if(data.quotes!![0].percentChange24h>0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text = "+ ${String.format("%.02f",data.quotes[0].percentChange24h)}"
        }
        else{
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text = "+ ${String.format("%.02f",data.quotes[0].percentChange24h)}"
        }
    }


}