package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.runner.AndroidJUnit4
import android.util.Log
import fiofoundation.io.fiosdk.FIOSDK
import fiofoundation.io.fiosdk.enums.FioDomainVisiblity
import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetFIONamesError
import fiofoundation.io.fiosdk.models.fionetworkprovider.FIOApiEndPoints
import fiofoundation.io.fiosdk.models.fionetworkprovider.FundsRequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.RecordSendContent
import fiofoundation.io.fiosdk.utilities.CryptoUtils
import fiofoundation.io.androidfioserializationprovider.*
import fiofoundation.io.fiosdk.implementations.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.interfaces.ISignatureProvider
import org.bitcoinj.crypto.MnemonicCode

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.lang.AssertionError
import java.lang.Exception
import java.math.BigInteger
import java.security.SecureRandom

@RunWith(AndroidJUnit4::class)
class TestNetSdkTests {

    private val baseUrl = "https://testnet.fioprotocol.io:443/v1/"

    private var alicePrivateKey = "5HpdzXwqQGdu2UCRgzqD1HPQEFQhAwbuWEfzt4bmC1MX8FvruvM"
    private var alicePublicKey = "FIO5XhhneWkku7rCGfTedXHgfYQomhoQA5oQNXMumC5c2YNQSfraH"
    private var bobPrivateKey = "5JRN3nAJfBnu53LupB8dCfaX9H1Trst9Txu2bMkURec4kmrHj9U"
    private var bobPublicKey = "FIO8h4Gk85PnQd24n3HmTANCD3dR5cKjoAYXNuj7uFvibYHG8jNEm"

    private var aliceFioAddress = "alicetest1:fiotestnet"
    private var bobFioAddress = "bobtest1:fiotestnet"

    private var fioTestNetDomain = "fiotestnet"
    private var defaultFee = BigInteger("400000000000")

    private val alicePublicTokenAddress = "1PzCN3cBkTL72GPeJmpcueU4wQi9guiLa6"
    private val alicePublicTokenCode = "BTC"
    private val bobPublicTokenAddress = "1AkZGXsnyDfp4faMmVfTWsN1nNRRvEZJk8"
    private var otherBlockChainId = "123456789"

    private var aliceFioSdk = createSdkInstance(alicePrivateKey,alicePublicKey)
    private var bobFioSdk = createSdkInstance(bobPrivateKey,bobPublicKey)


    @Test
    @ExperimentalUnsignedTypes
    fun testGenericActions()
    {
        println("testGenericActions: Begin Test for Generic Actions")

        val newFioDomain = this.generateTestingFioDomain()
        val newFioAddress = this.generateTestingFioAddress(newFioDomain)

        println("testGenericActions: Test getFioBalance - Alice")
        try
        {
            val fioBalance = this.aliceFioSdk.getFioBalance().balance

            assertTrue("Balance not Available for Alice.",fioBalance!=null && fioBalance>=BigInteger.ZERO)
        }
        catch (e: FIOError)
        {
            throw AssertionError("GetFioBalance for Alice Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("GetFioBalance for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test registerFioDomain")
        try
        {
            val response = this.aliceFioSdk.registerFioDomain(newFioDomain, defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't register $newFioDomain for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Register Fio Domain for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Register Fio Domain for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test setFioDomainVisibility to True")
        try
        {
            val response = this.aliceFioSdk.setFioDomainVisibility(newFioDomain,FioDomainVisiblity.PUBLIC,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Visibility NOT set for $newFioDomain",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Setting Fio Domain Visibility for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Setting Fio Domain Visibility for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test registerFioAddress")
        try
        {
            val response = this.aliceFioSdk.registerFioAddress(newFioAddress,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't Register FioAddress $newFioAddress for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Register FioAddress for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Register FioAddress for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test renewFioAddress")
        try
        {
            val response = this.aliceFioSdk.renewFioAddress(newFioAddress,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't Renew FioAddress $newFioAddress for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Renew FioAddress for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Renew FioAddress for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test addPublicAddress")
        try
        {
            val response = this.aliceFioSdk.addPublicAddress(newFioAddress,this.alicePublicTokenCode,
                this.alicePublicTokenAddress,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't Add Public Address for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Add Public Address  for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Add Public Address for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test isFioAddressAvailable True")
        try
        {
            val testAddress = this.generateTestingFioAddress()
            val response = this.aliceFioSdk.isAvailable(testAddress)

            assertTrue("FioAddress, $testAddress, is NOT Available",response!=null && response.isAvailable)
        }
        catch (e: FIOError)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test isFioAddressAvailable False")
        try
        {
            val response = this.aliceFioSdk.isAvailable(this.aliceFioAddress)

            assertTrue("FioAddress, $aliceFioAddress, IS Available (not supposed to be)",response!=null && !response.isAvailable)
        }
        catch (e: FIOError)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test getFioNames")
        try
        {
            val response = this.aliceFioSdk.getFioNames()

            assertTrue("Couldn't Get FioNames for Alice",response.fioAddresses!!.isNotEmpty())
        }
        catch (e: FIOError)
        {
            throw AssertionError("Get FioNames for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Get FioNames for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test getFee")
        try
        {
            val response = this.aliceFioSdk.getFee(FIOApiEndPoints.EndPointsWithFees.RegisterFioAddress)

            assertTrue("Couldn't Get Fee for " + FIOApiEndPoints.EndPointsWithFees.RegisterFioAddress.endpoint,response.fee>=0)
        }
        catch (e: FIOError)
        {
            throw AssertionError("Get Fee Call Failed for Alice: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Get Fee Call Failed for Alice: " + generalException.message)
        }

        println("testGenericActions: End Test for Generic Actions")
    }

    @Test
    fun testFundsRequest()
    {
        println("testFundsRequest: Begin Test for NewFundsRequest")

        println("testFundsRequest: Test requestNewFunds")
        try
        {
            val response = this.aliceFioSdk.requestNewFunds(this.bobFioAddress,
                this.aliceFioAddress,this.alicePublicTokenAddress,"2.0",this.alicePublicTokenCode,
                this.defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Alice Couldn't Request Funds from Bob: " + response.toJson(),actionTraceResponse!=null && actionTraceResponse.status == "requested")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Alice's Funds Request Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Alice's Funds Request Failed: " + generalException.message)
        }

        Thread.sleep(4000)

        println("testFundsRequest: Test getSentFioRequests")
        try
        {

            val sentRequests = this.aliceFioSdk.getSentFioRequests()

            if(sentRequests.isNotEmpty())
            {
                assertTrue("Requests Sent by Alice are NOT Available",sentRequests.isNotEmpty())

                for (req in sentRequests)
                {
                    val sharedSecretKey = CryptoUtils.generateSharedSecret(this.alicePrivateKey,req.payerFioPublicKey)

                    req.deserializeRequestContent(sharedSecretKey,this.aliceFioSdk.serializationProvider)

                    if(req.requestContent!=null)
                    {
                        assertTrue("Funds Request Sent by Alice is NOT Valid",req.requestContent != null)
                    }
                }
            }

        }
        catch (e: FIOError)
        {
            throw AssertionError("Cannot Get List of Requests Sent by Alice: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Cannot Get List of Requests Sent by Alice: " + generalException.message)
        }

        println("testFundsRequest: Test getPendingFioRequests")
        try {

            val pendingRequests = this.bobFioSdk.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                assertTrue("Bob does not have requests from Alice that are pending",pendingRequests.isNotEmpty())

                for (req in pendingRequests)
                {
                    val sharedSecretKey = CryptoUtils.generateSharedSecret(this.bobPrivateKey,req.payeeFioPublicKey)

                    req.deserializeRequestContent(sharedSecretKey,this.bobFioSdk.serializationProvider)

                    if(req.requestContent!=null)
                    {
                        assertTrue("Pending Requests are Valid",req.requestContent != null)
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Pending Requests Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Pending Requests Failed: " + generalException.message)
        }

        println("testFundsRequest: Test recordSend")
        try
        {
            val sharedSecretKey = CryptoUtils.generateSharedSecret(this.bobPrivateKey,this.alicePublicKey)

            val pendingRequests = this.bobFioSdk!!.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                val firstPendingRequest = pendingRequests.firstOrNull{it.payerFioAddress == this.bobFioAddress}

                if(firstPendingRequest!=null)
                {
                    firstPendingRequest.deserializeRequestContent(sharedSecretKey,this.bobFioSdk.serializationProvider)

                    if(firstPendingRequest.requestContent!=null)
                    {
                        var recordSendContent = RecordSendContent(this.bobPublicTokenAddress,
                            firstPendingRequest.requestContent!!.payeeTokenPublicAddress,
                            firstPendingRequest.requestContent!!.amount,
                            firstPendingRequest.requestContent!!.tokenCode,this.otherBlockChainId)

                        val response = this.bobFioSdk.recordSend(firstPendingRequest.fioRequestId,firstPendingRequest.payerFioAddress
                            ,firstPendingRequest.payeeFioAddress,this.bobPublicTokenAddress,recordSendContent.payeeTokenPublicAddress,
                            recordSendContent.amount.toDouble(),recordSendContent.tokenCode,"",
                            recordSendContent.obtId,this.defaultFee)

                        val actionTraceResponse = response.getActionTraceResponse()

                        assertTrue("Couldn't Record Bob Sent Funds to Alice: "+ response.toJson(),actionTraceResponse!=null && actionTraceResponse.status == "sent_to_blockchain")
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Record Send Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Record Send Failed: " + generalException.message)
        }

        Thread.sleep(4000)

        //Set up test for rejecting funds request
        println("testFundsRequest: Test requestNewFunds")
        try
        {
            val response = this.aliceFioSdk.requestNewFunds(this.bobFioAddress,
                this.aliceFioAddress,this.alicePublicTokenAddress,"2.0",this.alicePublicTokenCode,
                this.defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Alice Couldn't Request Funds from Bob: "+ response.toJson(),actionTraceResponse!=null && actionTraceResponse.status == "requested")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Alice's Funds Request Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Alice's Funds Request Failed: " + generalException.message)
        }

        Thread.sleep(4000)

        println("testFundsRequest: Test getSentFioRequests")
        try
        {

            val sentRequests = this.aliceFioSdk.getSentFioRequests()

            if(sentRequests.isNotEmpty())
            {
                assertTrue("Requests Sent by Alice are NOT Available",sentRequests.isNotEmpty())

                for (req in sentRequests)
                {
                    val sharedSecretKey = CryptoUtils.generateSharedSecret(this.alicePrivateKey,req.payerFioPublicKey)

                    req.deserializeRequestContent(sharedSecretKey,this.aliceFioSdk.serializationProvider)

                    if(req.requestContent!=null)
                    {
                        assertTrue("Funds Request Sent by Alice is NOT Valid",req.requestContent != null)
                    }
                }
            }

        }
        catch (e: FIOError)
        {
            throw AssertionError("Cannot Get List of Requests Sent by Alice: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Cannot Get List of Requests Sent by Alice: " + generalException.message)
        }

        println("testFundsRequest: Test getPendingFioRequests")
        try {

            val pendingRequests = this.bobFioSdk.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                assertTrue("Bob does not have requests from Alice that are pending",pendingRequests.isNotEmpty())

                for (req in pendingRequests)
                {
                    val sharedSecretKey = CryptoUtils.generateSharedSecret(this.bobPrivateKey,req.payeeFioPublicKey)

                    req.deserializeRequestContent(sharedSecretKey,this.bobFioSdk.serializationProvider)

                    if(req.requestContent!=null)
                    {
                        assertTrue("Pending Requests are NOT Valid",req.requestContent != null)
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Pending Requests Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Pending Requests Failed: " + generalException.message)
        }

        println("testFundsRequest: Test rejectFundsRequest")
        try {
            val sharedSecretKey = CryptoUtils.generateSharedSecret(this.bobPrivateKey,this.alicePublicKey)

            val pendingRequests = this.bobFioSdk.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                val firstPendingRequest = pendingRequests.firstOrNull{it.payerFioAddress == this.bobFioAddress}

                if(firstPendingRequest!=null)
                {
                    firstPendingRequest.deserializeRequestContent(sharedSecretKey,this.bobFioSdk.serializationProvider)

                    if(firstPendingRequest.requestContent!=null)
                    {
                        val response = this.bobFioSdk.rejectFundsRequest(firstPendingRequest.fioRequestId,
                            this.defaultFee)

                        val actionTraceResponse = response.getActionTraceResponse()

                        if(actionTraceResponse!=null)
                        {
                            assertTrue("Bob Couldn't Reject Funds Request from Alice: "+ response.toJson(),actionTraceResponse.status == "request_rejected")
                        }
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Reject Funds Request Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Reject Funds Request Failed: " + generalException.message)
        }

        println("testFundsRequest: End Test for NewFundsRequest")

    }

    @Test
    fun testTransferFioTokens()
    {
        println("testTransferFioTokens: Begin Test for TransferFioTokens")

        val amountToTransfer = BigInteger("1000000000")   //Amount is in SUFs
        var bobBalanceBeforeTransfer = BigInteger.ZERO
        var bobBalanceAfterTransfer = BigInteger.ZERO

        println("testTransferFioTokens: Verify Bob's Current FIO Balance")
        try
        {
            bobBalanceBeforeTransfer = this.bobFioSdk.getFioBalance().balance
        }
        catch (e: FIOError)
        {
            throw AssertionError("GetFioBalance for Bob Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("GetFioBalance for Bob Failed: " + generalException.message)
        }

        println("testTransferFioTokens: Test transferTokens")
        try
        {
            val response = this.aliceFioSdk.transferTokens(this.bobPublicKey,amountToTransfer,this.defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Alice Failed to Transfer FIO to Bob",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("FIO Token Transfer Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("FIO Token Transfer Failed: " + generalException.message)
        }

        println("testTransferFioTokens: Verify Bob's New FIO Balance")
        try
        {
            bobBalanceAfterTransfer = this.bobFioSdk.getFioBalance().balance

            assertTrue("Alice Filed to Transfer FIO to Bob",(bobBalanceAfterTransfer - bobBalanceBeforeTransfer) == amountToTransfer)
        }
        catch (e: FIOError)
        {
            throw AssertionError("GetFioBalance for Bob Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("GetFioBalance for Bob Failed: " + generalException.message)
        }

        println("testTransferFioTokens: End Test for TransferFioTokens")
    }

    //Helper Methods
    private fun generateTestingFioDomain():String
    {
        val now = System.currentTimeMillis().toString()

        return "testing-domain-$now"
    }

    private fun generateTestingFioAddress(customDomain:String = fioTestNetDomain):String
    {
        val now = System.currentTimeMillis().toString()

        return "testing$now:$customDomain"
    }

    private fun createSdkInstance(privateKey: String, publicKey: String):FIOSDK
    {
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(privateKey)

        val serializer = AbiFIOSerializationProvider()

        return FIOSDK(privateKey,publicKey,"",serializer,signatureProvider,this.baseUrl)
    }

}