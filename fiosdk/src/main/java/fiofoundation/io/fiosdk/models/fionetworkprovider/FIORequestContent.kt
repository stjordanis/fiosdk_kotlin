package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.toHexString
import fiofoundation.io.fiosdk.utilities.CompressionUtils
import fiofoundation.io.fiosdk.utilities.CryptoUtils
import java.lang.Error
import java.lang.Exception
import java.math.BigInteger

open class FIORequestContent {

    @field:SerializedName("fio_request_id") var fioRequestId:BigInteger= BigInteger.ZERO
    @field:SerializedName("payer_fio_address") var payerFioAddress:String = ""
    @field:SerializedName("payee_fio_address") var payeeFioAddress:String = ""
    @field:SerializedName("payer_fio_public_key") var payerFioPublicKey:String = ""
    @field:SerializedName("payee_fio_public_key") var payeeFioPublicKey:String = ""
    @field:SerializedName("content") private var content:String = ""
    @field:SerializedName("time_stamp") var timeStamp:String = ""

    var requestContent : FundsRequestContent? = null

    fun deserializeRequestContent(sharedSecretKey: ByteArray, serializationProvider: ISerializationProvider):FundsRequestContent?
    {
        try {
            val decryptedMessage = CryptoUtils.decryptSharedMessage(this.content,sharedSecretKey,CryptoUtils.EncrytionResultsEncoding.BASE64)
            val decompressedMessage = CompressionUtils.decompress(decryptedMessage.toUByteArray())

            val deserializedMessage = serializationProvider.deserializeNewFundsContent(decompressedMessage!!.toByteArray().toHexString())

            this.requestContent = Gson().fromJson(deserializedMessage, FundsRequestContent::class.java)

            return this.requestContent
        }
        catch(e:Exception)
        {
            this.requestContent = null
            return this.requestContent
        }

    }

    fun toJson(): String {
        val gson = GsonBuilder().create()
        return gson.toJson(this,this.javaClass)
    }
}