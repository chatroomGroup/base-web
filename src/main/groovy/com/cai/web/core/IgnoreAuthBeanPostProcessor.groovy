package com.cai.web.core

import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

class IgnoreAuthBeanPostProcessor implements InstantiationAwareBeanPostProcessor{

    @Autowired
    ApplicationContext ac

    @Autowired
    IgnoreAuthStore ignoreAuthStore

    @Override
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try{
            checkBean(bean,beanName)
            return bean
        }catch(Throwable t){}
        return bean
    }


    @Override
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try{
            return bean
        }catch(Throwable t){}
        return bean
    }


    void checkBean(Object bean, String beanName){
        println beanName
        if (bean.class.isAnnotationPresent(IgnoreAuth)){
            bean.class.getMethods().toList().each {it->
                if (it.isAnnotationPresent(RequestMapping)){
                    println "--------------------------------------------${beanName}"
                    String rootMap = getPath(bean.class.getAnnotation(RequestMapping))
                    ignoreAuthStore.addIgnoreAuthMapping(rootMap + getPath(it.getAnnotation(RequestMapping)), it.isAnnotationPresent(ReturnToken))
                }
            }
        }
        if (bean.class.isAnnotationPresent(RestController) || bean.class.isAnnotationPresent(Controller)){
            bean.class.getMethods().toList().each {it->
                if (it.isAnnotationPresent(IgnoreAuth) && it.isAnnotationPresent(RequestMapping)){
                    println "----------${beanName}"
                    String rootMap = getPath(bean.class.getAnnotation(RequestMapping))
                    ignoreAuthStore.addIgnoreAuthMapping(rootMap + getPath(it.getAnnotation(RequestMapping)), it.isAnnotationPresent(ReturnToken))
                }
            }
        }
    }


    String getPath(RequestMapping mapping){
        if (mapping.value())
            return matchDynamicCode(mapping.value()[0])
        if (mapping.path())
            return matchDynamicCode(mapping.path()[0])
        return ""
    }

    /**
     * 针对 @PathVariable 的情况下，动态的判断path
     * @param path
     * @return
     */
    // /1/2/{3}/{4} => /1/2/*/*  //
    static String matchDynamicCode(String path){
        String val = path
        Stack<Node> stack = new Stack()
        List<Integer> target = []
        List<String> group = path.split("/")
        for (int j = 1 ; j < group.size(); j++){
            for (int i = 0 ; i < group[j].length() ; i++){
                if("{" == group[j][i]){
                    if (stack.empty()){
                        stack.push(new Node("{", i, j))
                        continue
                    }
                    Node previous = stack.pop()
                    if(previous && "{" == previous.target){
                        stack.push(previous)
                        continue
                    }
                    stack.push(new Node("{", i, j))
                } else if("}" == group[j][i]){
                    if (stack.empty()){
                        break
                    }
                    Node previous = stack.pop()
                    if (previous && "{" == previous.target
                            && previous.group == j
                            && previous.position == 0
                            && i == group[j].length() - 1){
                        target.add(j)
                    }else{
                        stack.push(new Node("}", i, j))
                    }
                }
            }

        }

        target.each {it->
            group[it] = "*"
        }
        return group.join("/")
    }


}

class Node{
    String target

    Integer position

    Integer group

    Node(String target, Integer position, Integer group) {
        this.target = target
        this.position = position
        this.group = group
    }
}