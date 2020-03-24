package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single
import net.laggedhero.revolut.challenge.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.domain.*

class CurrencyRepositoryImpl(
    private val currencyApi: CurrencyApi
) : CurrencyRepository {
    override fun ratesFor(currencyCode: CurrencyCode): Single<Rates> {
        return currencyApi.latestRates(currencyCode.value)
            .map { it.toRates() }
    }

    private fun CurrencyRatesDto.toRates(): Rates {
        return Rates(
            baseCurrency = Currency(
                currencyCode = baseCurrency.toCurrencyCode(),
                referenceRate = CurrencyReferenceRate(1F),
                appliedConversion = CurrencyConversion(1F)
            ),
            rates = rates.toCurrencyList()
        )
    }

    private fun Map<String, Float>.toCurrencyList(): List<Currency> {
        return map {
            Currency(
                currencyCode = it.key.toCurrencyCode(),
                referenceRate = CurrencyReferenceRate(it.value),
                appliedConversion = CurrencyConversion(it.value)
            )
        }
    }

    private fun String.toCurrencyCode(): CurrencyCode {
        return when (this) {
            CurrencyCode.AED.value -> CurrencyCode.AED
            CurrencyCode.AFN.value -> CurrencyCode.AFN
            CurrencyCode.ALL.value -> CurrencyCode.ALL
            CurrencyCode.AMD.value -> CurrencyCode.AMD
            CurrencyCode.ANG.value -> CurrencyCode.ANG
            CurrencyCode.AOA.value -> CurrencyCode.AOA
            CurrencyCode.ARS.value -> CurrencyCode.ARS
            CurrencyCode.AUD.value -> CurrencyCode.AUD
            CurrencyCode.AWG.value -> CurrencyCode.AWG
            CurrencyCode.AZN.value -> CurrencyCode.AZN
            CurrencyCode.BAM.value -> CurrencyCode.BAM
            CurrencyCode.BBD.value -> CurrencyCode.BBD
            CurrencyCode.BDT.value -> CurrencyCode.BDT
            CurrencyCode.BGN.value -> CurrencyCode.BGN
            CurrencyCode.BHD.value -> CurrencyCode.BHD
            CurrencyCode.BIF.value -> CurrencyCode.BIF
            CurrencyCode.BMD.value -> CurrencyCode.BMD
            CurrencyCode.BND.value -> CurrencyCode.BND
            CurrencyCode.BOB.value -> CurrencyCode.BOB
            CurrencyCode.BOV.value -> CurrencyCode.BOV
            CurrencyCode.BRL.value -> CurrencyCode.BRL
            CurrencyCode.BSD.value -> CurrencyCode.BSD
            CurrencyCode.BTN.value -> CurrencyCode.BTN
            CurrencyCode.BWP.value -> CurrencyCode.BWP
            CurrencyCode.BYN.value -> CurrencyCode.BYN
            CurrencyCode.BZD.value -> CurrencyCode.BZD
            CurrencyCode.CAD.value -> CurrencyCode.CAD
            CurrencyCode.CDF.value -> CurrencyCode.CDF
            CurrencyCode.CHE.value -> CurrencyCode.CHE
            CurrencyCode.CHF.value -> CurrencyCode.CHF
            CurrencyCode.CHW.value -> CurrencyCode.CHW
            CurrencyCode.CLF.value -> CurrencyCode.CLF
            CurrencyCode.CLP.value -> CurrencyCode.CLP
            CurrencyCode.CNY.value -> CurrencyCode.CNY
            CurrencyCode.COP.value -> CurrencyCode.COP
            CurrencyCode.COU.value -> CurrencyCode.COU
            CurrencyCode.CRC.value -> CurrencyCode.CRC
            CurrencyCode.CUC.value -> CurrencyCode.CUC
            CurrencyCode.CUP.value -> CurrencyCode.CUP
            CurrencyCode.CVE.value -> CurrencyCode.CVE
            CurrencyCode.CZK.value -> CurrencyCode.CZK
            CurrencyCode.DJF.value -> CurrencyCode.DJF
            CurrencyCode.DKK.value -> CurrencyCode.DKK
            CurrencyCode.DOP.value -> CurrencyCode.DOP
            CurrencyCode.DZD.value -> CurrencyCode.DZD
            CurrencyCode.EGP.value -> CurrencyCode.EGP
            CurrencyCode.ERN.value -> CurrencyCode.ERN
            CurrencyCode.ETB.value -> CurrencyCode.ETB
            CurrencyCode.EUR.value -> CurrencyCode.EUR
            CurrencyCode.FJD.value -> CurrencyCode.FJD
            CurrencyCode.FKP.value -> CurrencyCode.FKP
            CurrencyCode.GBP.value -> CurrencyCode.GBP
            CurrencyCode.GEL.value -> CurrencyCode.GEL
            CurrencyCode.GHS.value -> CurrencyCode.GHS
            CurrencyCode.GIP.value -> CurrencyCode.GIP
            CurrencyCode.GMD.value -> CurrencyCode.GMD
            CurrencyCode.GNF.value -> CurrencyCode.GNF
            CurrencyCode.GTQ.value -> CurrencyCode.GTQ
            CurrencyCode.GYD.value -> CurrencyCode.GYD
            CurrencyCode.HKD.value -> CurrencyCode.HKD
            CurrencyCode.HNL.value -> CurrencyCode.HNL
            CurrencyCode.HRK.value -> CurrencyCode.HRK
            CurrencyCode.HTG.value -> CurrencyCode.HTG
            CurrencyCode.HUF.value -> CurrencyCode.HUF
            CurrencyCode.IDR.value -> CurrencyCode.IDR
            CurrencyCode.ILS.value -> CurrencyCode.ILS
            CurrencyCode.INR.value -> CurrencyCode.INR
            CurrencyCode.IQD.value -> CurrencyCode.IQD
            CurrencyCode.IRR.value -> CurrencyCode.IRR
            CurrencyCode.ISK.value -> CurrencyCode.ISK
            CurrencyCode.JMD.value -> CurrencyCode.JMD
            CurrencyCode.JOD.value -> CurrencyCode.JOD
            CurrencyCode.JPY.value -> CurrencyCode.JPY
            CurrencyCode.KES.value -> CurrencyCode.KES
            CurrencyCode.KGS.value -> CurrencyCode.KGS
            CurrencyCode.KHR.value -> CurrencyCode.KHR
            CurrencyCode.KMF.value -> CurrencyCode.KMF
            CurrencyCode.KPW.value -> CurrencyCode.KPW
            CurrencyCode.KRW.value -> CurrencyCode.KRW
            CurrencyCode.KWD.value -> CurrencyCode.KWD
            CurrencyCode.KYD.value -> CurrencyCode.KYD
            CurrencyCode.KZT.value -> CurrencyCode.KZT
            CurrencyCode.LAK.value -> CurrencyCode.LAK
            CurrencyCode.LBP.value -> CurrencyCode.LBP
            CurrencyCode.LKR.value -> CurrencyCode.LKR
            CurrencyCode.LRD.value -> CurrencyCode.LRD
            CurrencyCode.LSL.value -> CurrencyCode.LSL
            CurrencyCode.LYD.value -> CurrencyCode.LYD
            CurrencyCode.MAD.value -> CurrencyCode.MAD
            CurrencyCode.MDL.value -> CurrencyCode.MDL
            CurrencyCode.MGA.value -> CurrencyCode.MGA
            CurrencyCode.MKD.value -> CurrencyCode.MKD
            CurrencyCode.MMK.value -> CurrencyCode.MMK
            CurrencyCode.MNT.value -> CurrencyCode.MNT
            CurrencyCode.MOP.value -> CurrencyCode.MOP
            CurrencyCode.MRU.value -> CurrencyCode.MRU
            CurrencyCode.MUR.value -> CurrencyCode.MUR
            CurrencyCode.MVR.value -> CurrencyCode.MVR
            CurrencyCode.MWK.value -> CurrencyCode.MWK
            CurrencyCode.MXN.value -> CurrencyCode.MXN
            CurrencyCode.MXV.value -> CurrencyCode.MXV
            CurrencyCode.MYR.value -> CurrencyCode.MYR
            CurrencyCode.MZN.value -> CurrencyCode.MZN
            CurrencyCode.NAD.value -> CurrencyCode.NAD
            CurrencyCode.NGN.value -> CurrencyCode.NGN
            CurrencyCode.NIO.value -> CurrencyCode.NIO
            CurrencyCode.NOK.value -> CurrencyCode.NOK
            CurrencyCode.NPR.value -> CurrencyCode.NPR
            CurrencyCode.NZD.value -> CurrencyCode.NZD
            CurrencyCode.OMR.value -> CurrencyCode.OMR
            CurrencyCode.PAB.value -> CurrencyCode.PAB
            CurrencyCode.PEN.value -> CurrencyCode.PEN
            CurrencyCode.PGK.value -> CurrencyCode.PGK
            CurrencyCode.PHP.value -> CurrencyCode.PHP
            CurrencyCode.PKR.value -> CurrencyCode.PKR
            CurrencyCode.PLN.value -> CurrencyCode.PLN
            CurrencyCode.PYG.value -> CurrencyCode.PYG
            CurrencyCode.QAR.value -> CurrencyCode.QAR
            CurrencyCode.RON.value -> CurrencyCode.RON
            CurrencyCode.RSD.value -> CurrencyCode.RSD
            CurrencyCode.RUB.value -> CurrencyCode.RUB
            CurrencyCode.RWF.value -> CurrencyCode.RWF
            CurrencyCode.SAR.value -> CurrencyCode.SAR
            CurrencyCode.SBD.value -> CurrencyCode.SBD
            CurrencyCode.SCR.value -> CurrencyCode.SCR
            CurrencyCode.SDG.value -> CurrencyCode.SDG
            CurrencyCode.SEK.value -> CurrencyCode.SEK
            CurrencyCode.SGD.value -> CurrencyCode.SGD
            CurrencyCode.SHP.value -> CurrencyCode.SHP
            CurrencyCode.SLL.value -> CurrencyCode.SLL
            CurrencyCode.SOS.value -> CurrencyCode.SOS
            CurrencyCode.SRD.value -> CurrencyCode.SRD
            CurrencyCode.SSP.value -> CurrencyCode.SSP
            CurrencyCode.STN.value -> CurrencyCode.STN
            CurrencyCode.SVC.value -> CurrencyCode.SVC
            CurrencyCode.SYP.value -> CurrencyCode.SYP
            CurrencyCode.SZL.value -> CurrencyCode.SZL
            CurrencyCode.THB.value -> CurrencyCode.THB
            CurrencyCode.TJS.value -> CurrencyCode.TJS
            CurrencyCode.TMT.value -> CurrencyCode.TMT
            CurrencyCode.TND.value -> CurrencyCode.TND
            CurrencyCode.TOP.value -> CurrencyCode.TOP
            CurrencyCode.TRY.value -> CurrencyCode.TRY
            CurrencyCode.TTD.value -> CurrencyCode.TTD
            CurrencyCode.TWD.value -> CurrencyCode.TWD
            CurrencyCode.TZS.value -> CurrencyCode.TZS
            CurrencyCode.UAH.value -> CurrencyCode.UAH
            CurrencyCode.UGX.value -> CurrencyCode.UGX
            CurrencyCode.USD.value -> CurrencyCode.USD
            CurrencyCode.USN.value -> CurrencyCode.USN
            CurrencyCode.UYI.value -> CurrencyCode.UYI
            CurrencyCode.UYU.value -> CurrencyCode.UYU
            CurrencyCode.UZS.value -> CurrencyCode.UZS
            CurrencyCode.VEF.value -> CurrencyCode.VEF
            CurrencyCode.VND.value -> CurrencyCode.VND
            CurrencyCode.VUV.value -> CurrencyCode.VUV
            CurrencyCode.WST.value -> CurrencyCode.WST
            CurrencyCode.XAF.value -> CurrencyCode.XAF
            CurrencyCode.XCD.value -> CurrencyCode.XCD
            CurrencyCode.XDR.value -> CurrencyCode.XDR
            CurrencyCode.XOF.value -> CurrencyCode.XOF
            CurrencyCode.XPF.value -> CurrencyCode.XPF
            CurrencyCode.XSU.value -> CurrencyCode.XSU
            CurrencyCode.XUA.value -> CurrencyCode.XUA
            CurrencyCode.YER.value -> CurrencyCode.YER
            CurrencyCode.ZAR.value -> CurrencyCode.ZAR
            CurrencyCode.ZMW.value -> CurrencyCode.ZMW
            CurrencyCode.ZWL.value -> CurrencyCode.ZWL
            else -> throw IllegalArgumentException("Unknown Currency Code")
        }
    }
}