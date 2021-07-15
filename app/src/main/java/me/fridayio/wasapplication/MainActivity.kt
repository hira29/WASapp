package me.fridayio.wasapplication

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import me.fridayio.wasapplication.api.Api
import me.fridayio.wasapplication.api.Client
import me.fridayio.wasapplication.api.PostRequest
import me.fridayio.wasapplication.api.PostResponse
import me.fridayio.wasapplication.databinding.ActivityMainBinding
import me.fridayio.wasapplication.dft.DFTClass
import me.fridayio.wasapplication.filter.DxNClass
import me.fridayio.wasapplication.filter.KalmanClass
import me.fridayio.wasapplication.filter.MovingAverageClass
import me.fridayio.wasapplication.filter.PeakDetectionClass
import me.fridayio.wasapplication.hrmeasure.HRMeasureClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : Activity(), SensorEventListener {

    //Construct Data Use For Filter and Other Process
    private lateinit var sensorMgr: SensorManager
    private var ppg: Sensor? = null
    private var list = mutableListOf<Float>()

    private var dxn = DxNClass()
    private var movg = MovingAverageClass()
    private var kalman = KalmanClass()
    private var hrm = HRMeasureClass()



    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorMgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        ppg = sensorMgr.getDefaultSensor(65572)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        //Do Accuracy Measure for Sensor
    }

    override fun onSensorChanged(event: SensorEvent) {
        //Get Sensor Values based on Event
        val x = event.values[0]
        //Signal Invert and Condition
        val invert = -x
        val condition = invert + (2*1000000)
        //Applied Kalman Filter to smooth Signal (1)
        val kalmanOutput = kalman.kalmanFilter(condition)
        list.add(kalmanOutput)
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "Start Record", Toast.LENGTH_SHORT).show()
        ppg?.also { ppg ->
            sensorMgr.registerListener(this, ppg, SensorManager.SENSOR_DELAY_FASTEST)
        }
        Handler(Looper.getMainLooper()).postDelayed(processing, 15000)
    }

    var recordData = Runnable() {
        sensorMgr.unregisterListener(this)
        Toast.makeText(this, "Data Sent", Toast.LENGTH_SHORT).show()
        for (data in list) {
            Log.d("output:", data.toString())
        }
    }

    var processing =  Runnable() {
        Toast.makeText(this, "Processing Data", Toast.LENGTH_SHORT).show()

        var dxnOutput = dxn.DxNFilter(list)
        var movAvgOutput = movg.MovAvg(dxnOutput)

        var hrResult = hrm.HRMeasurement(movAvgOutput)

        var dft = DFTClass(movAvgOutput)
        var dftRes = dft.Result()

        for(data in hrResult) {
            Log.d("HrOutput", data.toString())
        }

        for(data in dftRes) {
            Log.d("DftOutput", data.toString())
        }

        onPost(hrResult, dftRes)
        Toast.makeText(this,"Processing Done", Toast.LENGTH_SHORT).show()

        list.clear()
        onResume()
    }

    private fun onPost(hr : MutableList<Double>, dft : MutableList<Double>) {
        var postReq = PostRequest()
        postReq.hr1 = hr[0]
        postReq.hr2 = hr[1]
        postReq.peak_locff = dft[0]
        postReq.peak_locr = dft[1]
        postReq.peak_locim = dft[2]
        postReq.stdre = dft[3]
        postReq.stdim = dft[4]

        val retro = Client().retroInstance("https://wasml-service.herokuapp.com/").create(Api::class.java)
        retro.postML(postReq).enqueue(object : Callback<PostResponse>{
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.e("Error", "HTTP POST ERROR!")
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                val res = response.body()
                if (res?.result == 1) Log.d("Detection Output", "Bradycardia")
                if (res?.result == 2) Log.d("Detection Output", "Tachycardia")
                if (res?.result == 3) Log.d("Detection Output", "Normal")
            }
        })
    }
}