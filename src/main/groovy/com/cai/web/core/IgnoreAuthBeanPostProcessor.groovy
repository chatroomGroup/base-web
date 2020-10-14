package com.cai.web.core

import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Order(value = Integer.MIN_VALUE)
@Component
class IgnoreAuthBeanPostProcessor implements BeanPostProcessor{

    @Autowired
    ApplicationContext ac

    @Autowired
    IgnoreAuthStore ignoreAuthStore

    @Override
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try{
            if (bean.class.isAnnotationPresent(IgnoreAuth)){
                bean.class.getMethods().toList().each {it->
                    if (it.isAnnotationPresent(RequestMapping)){
                        String rootMap = getPath(bean.class.getAnnotation(RequestMapping))
                        ignoreAuthStore.addIgnoreAuthMapping(rootMap + getPath(it.getAnnotation(RequestMapping)), it.isAnnotationPresent(ReturnToken))
                    }
                }
                return bean
            }
            if (bean.class.isAnnotationPresent(RestController) || bean.class.isAnnotationPresent(Controller)){
                bean.class.getMethods().toList().each {it->
                    if (it.isAnnotationPresent(IgnoreAuth) && it.isAnnotationPresent(RequestMapping)){
                        String rootMap = getPath(bean.class.getAnnotation(RequestMapping))
                        ignoreAuthStore.addIgnoreAuthMapping(rootMap + getPath(it.getAnnotation(RequestMapping)), it.isAnnotationPresent(ReturnToken))
                    }
                }
            }
            return bean
        }catch(Throwable t){}
        return bean
    }

    String getPath(RequestMapping mapping){
        if (mapping.value())
            return mapping.value()[0]
        if (mapping.path())
            return mapping.path()[0]
        return ""
    }
}
