package me.fridayio.wasapplication.hrmeasure

import me.fridayio.wasapplication.filter.DxNClass
import me.fridayio.wasapplication.filter.MovingAverageClass
import me.fridayio.wasapplication.filter.PeakDetectionClass

class HRMeasureClass {
    private var listHR = mutableListOf<Float>()
    private var peak = PeakDetectionClass()
    private var o1 : Float = 0.0f
    private var o2 : Float = 0.0f
    private var o3 : Float = 0.0f
    private var o4 : Float = 0.0f
    private var t1 : Float = 0f
    private var t2 : Float = 0f
    private var i : Int = 1
    private var count: Int = 0
    private var res = mutableListOf<Double>()

    fun HRMeasurement(Data: MutableList<Float>): MutableList<Double> {
        res.clear()
        listHR.clear()
        o1 = 0f; o2 = 0f; o3 = 0f; o4 = 0f
        t1 = 0f; t2 = 0f
        i=1; var tmpTime = 0F
        count = 0

        val sizeList = Data.size
        val stamp = 15F/sizeList
        var totalHR = 0.0F
        for (o: Float in Data ) {
            o4 = o3; o3 = o2
            o2 = o1; o1= o
            val t = peak.peakAlgorithm(o1,o2,o3,o4)
            if(i > 300 && t){
                t2 = t1; t1 = tmpTime
                count++

                val time = t1 - t2
                val hrTime = 60F/time
                totalHR += hrTime
                listHR.add(hrTime)
            }
            i++
            tmpTime += stamp
        }

        val hr1 = totalHR/listHR.size.toFloat()
        val hr2 = count.toFloat()/0.25F

        res.add(hr1.toDouble())
        res.add(hr2.toDouble())

        return res
    }

}