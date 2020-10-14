package com.cai.web.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter

@Configuration
class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper)
    ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("")
            }
        })
        return objectMapper;
    }

    @Autowired
    ResponseMessageReturnHandler returnHandler

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter

    @Bean
    void init() {
        final List<HandlerMethodReturnValueHandler> newHandlers = new LinkedList<>();
        final List<HandlerMethodReturnValueHandler> originalHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        if (null != originalHandlers) {
            newHandlers.addAll(originalHandlers);
            newHandlers.addFirst(returnHandler as HandlerMethodReturnValueHandler);
        } else {
            newHandlers.add(returnHandler as HandlerMethodReturnValueHandler);
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers as List<HandlerMethodReturnValueHandler>);
    }

}
