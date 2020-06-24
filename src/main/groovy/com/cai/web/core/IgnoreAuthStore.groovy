package com.cai.web.core

import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class IgnoreAuthStore {
    Collection<MappingWrapper> store = []

    @PostConstruct
    void init(){
        addIgnoreAuthMapping("/error", false)
        addIgnoreAuthMapping("/templates/error", false)
    }

    void addIgnoreAuthMapping(String mapping, boolean isReturnToken){
        store.add(new MappingWrapper(mapping, isReturnToken))
    }

    MappingWrapper getMapping(String mapping){
        return store.find {it->
            it.mapping == mapping
        }
    }

    boolean hasMapping(String mapping){
        return store.find {it->
            it.mapping == mapping
        } != null
    }

    String returnToken(String mapping) {
        MappingWrapper wrapper = getMapping(mapping)
        if (wrapper && wrapper.isReturnToken){
            return UUID.randomUUID().toString().replace("-", "")
        }
    }

    boolean hasReturnToken(String mapping){
        return store.find {it->
            it.mapping == mapping && it.isReturnToken
        } != null
    }
}

class MappingWrapper {

    MappingWrapper(String mapping, boolean isReturnToken) {
        this.mapping = mapping
        this.isReturnToken = isReturnToken
    }

    String mapping

    boolean isReturnToken = false
}
