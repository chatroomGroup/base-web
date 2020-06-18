package com.cai.web.core

import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.Controller

@Component
class IgnoreAuthBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    ApplicationContext ac

    @Autowired
    IgnoreAuthStore ignoreAuthStore

    @Override
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.class.isAnnotationPresent(RestController) || bean.class.isAnnotationPresent(Controller)){
            bean.class.getMethods().toList().each {it
                if (it.isAnnotationPresent(IgnoreAuth) && it.isAnnotationPresent(RequestMapping)){
                    String rootMap = getPath(bean.class.getAnnotation(RequestMapping))
                    ignoreAuthStore.addIgnoreAuthMapping(rootMap + getPath(it.getAnnotation(RequestMapping)))
                }
            }
        }
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
