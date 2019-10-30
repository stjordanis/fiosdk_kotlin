package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.Validator
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIORequestData
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse
import java.math.BigInteger

class RegisterFIODomainAction(fioDomain: String,
                               ownerPublicKey: String,
                               walletFioAddress: String,
                               maxFee: BigInteger,
                               actorPublicKey: String) :
    IAction
{
    override var account = "fio.address"
    override var name = "regdomain"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            FIODomainRequestData(
                fioDomain,
                ownerPublicKey,
                maxFee,
                auth.actor,
                walletFioAddress
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class FIODomainRequestData(@field:SerializedName("fio_domain") var fioDomain:String,
                                @field:SerializedName("owner_fio_public_key") var ownerPublicKey:String,
                                @field:SerializedName("max_fee") var max_fee:BigInteger,
                                @field:SerializedName("actor") var actor:String,
                                @field:SerializedName("tpid") var walletFioAddress:String): FIORequestData()
}