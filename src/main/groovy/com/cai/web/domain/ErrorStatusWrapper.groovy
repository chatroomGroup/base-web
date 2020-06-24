package com.cai.web.domain

class ErrorStatusWrapper {

    String path

    String msg

    String datetime

    String status

    ErrorStatusWrapper(String path, String msg, String datetime, String status) {
        this.path = path
        this.msg = msg
        this.datetime = datetime
        this.status = status
    }
}
