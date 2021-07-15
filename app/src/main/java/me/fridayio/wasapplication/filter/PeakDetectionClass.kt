package me.fridayio.wasapplication.filter

class PeakDetectionClass {
    private var ret = false;

    fun peakAlgorithm(i0: Float, i: Float, i1: Float, i2: Float): Boolean {
        ret = i0 < i && i > i1 && i1 > i2
        return ret
    }
}