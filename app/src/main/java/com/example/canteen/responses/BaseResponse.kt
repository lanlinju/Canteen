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

}