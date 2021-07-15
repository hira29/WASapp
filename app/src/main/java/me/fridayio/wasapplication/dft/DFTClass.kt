package me.fridayio.wasapplication.dft

import kotlin.math.*

class DFTClass (data: MutableList<Float>) {
    private var freq_locF : Double = 0.0
    private var freq_locR : Double = 0.0
    private var freq_locI : Double = 0.0
    private var stdRe : Double = 0.0
    private var stdIm : Double = 0.0
    private var reOutput = mutableListOf<Double>()
    private var imOutput = mutableListOf<Double>()
    private var ftOutput = mutableListOf<Double>()
    private var freqRes = mutableListOf<Double>()
    private var freq : Double = 0.0
    init {
        calcFT(data)
        calcSTD()
    }
    fun calcFT(Data: MutableList<Float>){
        var peakRe = 0.0
        var peakIm = 0.0
        var peakFt = 0.0
        var loc_R = 0
        var loc_iM = 0
        var loc_ft = 0
        var i = 0;  var j = 0
        freqRes.clear()

        reOutput.clear(); imOutput.clear(); ftOutput.clear()

        var re = 0.0; var im = 0.0; var ft = 0.0
        while ( i <= Data.size-1) {
            re = 0.0; im = 0.0
            while ( j <= Data.size-1) {
                re += Data[j].toDouble() * cos(2 * PI * (j.toDouble() - 1F) * (i.toDouble() - 1F) / Data.size.toDouble())
                im += Data[j].toDouble() * sin(2 * PI * (j.toDouble() - 1F) * (i.toDouble() - 1F) / Data.size.toDouble())
                j++
            }
            ft = sqrt(re.pow(2) + im.pow(2))

            if(i < (Data.size-1)/2){
                if (peakRe < re) {
                    peakRe = re
                    loc_R = i
                }
                if (peakIm < im) {
                    peakIm = im
                    loc_iM = i
                }
                if (peakFt < ft) {
                    peakFt = ft
                    loc_ft = i
                }
            }

            reOutput.add(re)
            imOutput.add(im)
            ftOutput.add(ft)

            i++
        }
        freq = (Data.size.toDouble() / 15) / ftOutput.size.toDouble()
        freq_locF = freq * loc_ft
        freq_locR = freq * loc_R
        freq_locI = freq * loc_iM
    }

    fun calcSTD() {

        //Calculate STD of Real parts
        var uRe = 0.0
        var i = 0
        while( i <= reOutput.size-1 ){
            uRe += reOutput[i]
            i++
        }
        uRe /= reOutput.size.toFloat()

        //Calculate STD of Imaginary parts
        var uIm = 0.0
        i = 0
        while( i <= imOutput.size-1 ){
            uIm += imOutput[i]
            i++
        }
        uIm /= imOutput.size.toFloat()

        var sigRe = 0.0
        i = 0
        while(i <= reOutput.size-1){
            sigRe += (reOutput[i] - uRe).pow(2)
            i++
        }

        var sigIm = 0.0
        i = 0
        while(i <= imOutput.size-1){
            sigIm += (imOutput[i] - uIm).pow(2)
            i++
        }

        stdRe = sqrt((1/(reOutput.size.toDouble()-1)) * sigRe)
        stdIm = sqrt((1/(imOutput.size.toDouble()-1)) * sigIm)

    }

    fun Result(): MutableList<Double> {
        var res = mutableListOf<Double>()
        res.add(freq_locF)
        res.add(freq_locR)
        res.add(freq_locI)
        res.add(stdRe)
        res.add(stdIm)

        return res
    }

}