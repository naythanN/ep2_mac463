package com.ln.lnplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_weather.weather_fragment
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import java.net.URL
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.ln.lnplayer.R
import kotlinx.android.synthetic.main.fragment_weather.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: PositionViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun accessNet(algo: LatLng) {
        val request = GlobalScope.launch(Dispatchers.IO){
            val response = URL("https://api.openweathermap.org/data/2.5/weather?lat=${algo.latitude}&lon=${algo.longitude}&units=metric&appid=$API").readText(Charsets.UTF_8)
            withContext(Dispatchers.Main) {
                val jsonObj = JSONObject(response)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                view?.humidity?.setText("A umidade é ${humidity}")
                view?.temperatura?.setText("A umidade é ${temp}")
                view?.description?.setText("A umidade é ${weatherDescription}")
                view?.wind_speed?.setText("A umidade é ${windSpeed}")
            }
        }

    }

    val API: String = "10a18ef6febbeb6d2c47e46c19426714"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.selectedLatLng.observe(
            viewLifecycleOwner,
            Observer { algo ->
                view.weather_fragment.setText("agora a longitude é ${algo.longitude} e a latitude é ${algo.latitude}")
                accessNet(algo)

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WeatherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}