package com.cai.web.config

import com.cai.web.core.IgnoreAuthBeanPostProcessor
import com.cai.web.core.IgnoreAuthStore
import org.springframework.beans.BeansException
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.web.server.ErrorPageRegistrarBeanPostProcessor
import org.springframework.boot.web.server.WebServerFactoryCustomizerBeanPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.Ordered
import org.springframework.core.type.AnnotationMetadata
import org.springframework.util.ObjectUtils

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import([BeanPostProcessorsRegistrar])
class IgnoreAuthAutoConfiguration {

    @Bean
    IgnoreAuthStore ignoreAuthStore(){
        return new IgnoreAuthStore()
    }
}


class BeanPostProcessorsRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        }
    }

    @Override
    void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        registerNotSyntheticBeanIfMissing(registry, "ignoreAuthBeanPostProcessor", IgnoreAuthBeanPostProcessor)
    }

    private void registerNotSyntheticBeanIfMissing(BeanDefinitionRegistry registry, String name, Class<?> beanClass) {
        if (ObjectUtils.isEmpty(this.beanFactory.getBeanNamesForType(beanClass, true, false))) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
            beanDefinition.setSynthetic(false);
            registry.registerBeanDefinition(name, beanDefinition);
        }
    }

}