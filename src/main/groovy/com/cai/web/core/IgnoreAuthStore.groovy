package com.cai.web.core

import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class IgnoreAuthStore {
    Collection<MappingWrapper> store = []

    Map chains= [:]

    @PostConstruct
    void init(){
        addIgnoreAuthMapping("/error", false)
        addIgnoreAuthMapping("/templates/error", false)
        addIgnoreAuthMapping("/error4xx", false)

        fillChain("/error".toList())
        fillChain("/templates/error".toList())
        fillChain("/error4xx".toList())

    }

    void addIgnoreAuthMapping(String mapping, boolean isReturnToken){
        store.add(new MappingWrapper(mapping, isReturnToken))
        fillChain(mapping.split("/").toList())
    }

    void fillChain(List<String> group){
        if (!chains[group[0]]){
            chains.put(group[0],[:])
        }
        Map<String,Map> tail = chains.get(group[0])
        group[1..-1].each {it->
            if (!tail[it])
                tail.put(it,[:])
            tail = tail[it]
        }
    }

    MappingWrapper getMapping(String mapping){
        def dynamicPath = buildMapping(mapping)
        return store.find {it->
            it.mapping == dynamicPath
        }
    }

    boolean hasMapping(String mapping){
//        return store.find {it->
//            it.mapping == mapping
//        } != null
        if (buildMapping(mapping))
            return true
        return false
    }

    def buildMapping(String path){
        List<String> group = path.split("/")
        List<String> res = []
        Map<String,Map> tail = chains
        for(int i = 0 ; i < group.size() ; i++){
            if (tail[group[i]] == null  && tail["*"] == null)
                return null
            if(tail[group[i]] != null){
                tail = tail[group[i]]
                res.add(group[i])
            }
            else{
                tail = tail["*"]
                res.add("*")
            }
        }
        return res.join("/")
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
