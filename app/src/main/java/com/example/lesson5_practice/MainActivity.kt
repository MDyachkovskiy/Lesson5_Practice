package com.example.lesson5_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lesson5_practice.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        with(binding) {
            ok.setOnClickListener {
                val uri = URL(this.url.text.toString())
                val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection)
                    .apply {
                        requestMethod = "GET"
                        connectTimeout = 10000
                        readTimeout = 10000
                    }

                Thread {
                    val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val result = getLinesAsOneBigString(buffer)
                    runOnUiThread {
                        this.webview.loadDataWithBaseURL(
                            null,
                            result,
                            "text/html; charset=utf-8",
                            "utf-8",
                        null
                            )
                    }
                }.start()

                urlConnection.disconnect()
            }
        }
    }

    fun getLinesAsOneBigString(bufferedReader: BufferedReader) : String =
        bufferedReader.lines().collect(Collectors.joining("\n"));


}