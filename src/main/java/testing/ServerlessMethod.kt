package testing

abstract class ServerlessMethod {
    var timesInvoked: Int = 0
        protected set
    var mostRecentInvokeArgument: Any? = null
        protected set
    var mostRecentValueReturned: Any? = null
        protected set
}