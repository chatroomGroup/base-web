package com.cai.web.controller

import com.cai.general.core.App
import com.cai.general.core.Session
import com.cai.general.util.session.SessionUtils
import com.cai.web.wrapper.WebSetting
import com.sun.javafx.charts.ChartLayoutAnimator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest

@Component
class BaseController extends com.cai.general.core.BaseController{

    @Autowired
    WebSetting webSetting

    @Override
    Session getSession(HttpServletRequest request) {
        if (webSetting.isLoose)
            return SessionUtils.createSession(DefaultSession.USER, DefaultSession.TOKEN,DefaultSession.APP)
        return super.getSession(request)
    }

    static class DefaultSession {
        static String TOKEN = "DEFAULT"

        static String USER = "DEFAULT"

        static String APP = "DEFAULT"

    }
}
