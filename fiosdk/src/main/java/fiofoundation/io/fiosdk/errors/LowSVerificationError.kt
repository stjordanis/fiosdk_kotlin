package fiofoundation.io.fiosdk.errors

class LowSVerificationError: FIOError {
    constructor():super()
    constructor(message: String):super(message)
    constructor(exception: Exception):super(exception)
    constructor(message: String,exception: Exception):super(message,exception)
}