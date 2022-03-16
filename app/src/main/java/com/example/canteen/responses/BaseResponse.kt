package com.example.canteen.responses

class BaseResponse<T>(private val statusCode: StatusCode) {
    //状态码
    var code: Int = -1

    //描述信息
    var msg: String = "失败"

    //响应数据-采用泛型表示可以接受通用的数据类型
    var data: T? = null

    override fun toString(): String {
        return "BaseResponse(code=$code, msg='$msg', data=$data)"
    }

    init {
        this.code = statusCode.code
        this.msg = statusCode.msg
    }

    fun setStatusCode(statusCode: StatusCode) {
        this.code = statusCode.code
        this.msg = statusCode.msg
    }

    companion object {
        fun <T> Success() = BaseResponse<T>(StatusCode.Success)

        fun <T> Success(data: T): BaseResponse<T> {
            val retMsg = BaseResponse<T>(StatusCode.Success)
            retMsg.data = data
            return retMsg
        }

        fun Fail() = BaseResponse<String>(StatusCode.Fail)

        fun Fail(message: String): BaseResponse<String> {
            val retMsg = BaseResponse<String>(StatusCode.Fail)
            retMsg.msg = message
            return retMsg
        }
        fun NetworkError(message: String):BaseResponse<String>{
            val retMsg = BaseResponse<String>(StatusCode.NetWorkError)
            retMsg.msg = message
            return retMsg
        }
    }


}