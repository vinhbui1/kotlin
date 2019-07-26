package com.example.citysunriseapp

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    var oldcity=""

    protected fun Getsunset(view:View){
        var city=etCityname.text.toString()
        val url="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=d0185ba19c644307d95deef9e1b84683"
        myAsyncTask().execute(url)
    }
    inner class myAsyncTask: AsyncTask<String, String, String>() {


        override fun onPreExecute() {

            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL(params[0])
                val urlConnec=url.openConnection() as HttpURLConnection
                    urlConnec.connectTimeout=7000
                var inString=ConvertStreamToString(urlConnec.inputStream)
                //can not acces to UI
                publishProgress(inString)

            }catch (ex:Exception){}

            return ""

        }

        override fun onProgressUpdate(vararg values: String?) {
            try {

                var json=JSONObject(values[0])
                var sys=json.getJSONObject("sys")
                var sunrise=sys.getLong("sunrise")
                var main=json.getJSONObject("main")
                var temp=main.getLong("temp")
                var name=json.getString("name")


                /// convert Convert epoch to human-readable date
                val date = Date(sunrise* 1000L)
                val format = SimpleDateFormat("yyyy-MM-dd' is 'HH:mm:ss.SSSZ")
                format.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))
                System.out.println(format.format(date))


                    tvCity.text=name
                    tvTemp.text=(temp-273.15).toString()
                    tvSun.text="Sunrise of "+format.format(date).toString()



            }catch (ex:Exception){

                    tvCity.text="Not Fond Your City"
                    tvTemp.text="0"
                    tvSun.text="0"

            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }


    }

  fun ConvertStreamToString(inputStream: InputStream):String{
      val bufferedReader=BufferedReader(InputStreamReader(inputStream))
      var line:String
      var AllString:String=""
      try {
          do {
              line=bufferedReader.readLine()
              AllString +=line

          }while (line!=null)


      }catch (ex:Exception){}
      return AllString
  }

}

