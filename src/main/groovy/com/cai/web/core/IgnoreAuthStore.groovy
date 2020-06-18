package com.cai.web.core

import org.springframework.stereotype.Component

@Component
class IgnoreAuthStore {
    Collection<String> store = []

    void addIgnoreAuthMapping(String mapping){
        store.add(mapping)
    }

    boolean hasMapping(String mapping){
        return store.contains(mapping)
    }
}
