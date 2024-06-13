package org.ergea.orders.kafkadynamic.listener;

import lombok.SneakyThrows;
import org.ergea.orders.kafkadynamic.configuration.CustomKafkaListenerProperties;
import org.ergea.orders.kafkadynamic.model.CustomKafkaListenerProperty;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomKafkaListenerRegister implements InitializingBean {

    @Autowired
    private CustomKafkaListenerProperties customKafkaListenerProperties;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private KafkaListenerContainerFactory kafkaListenerContainerFactory;

    @Override
    public void afterPropertiesSet() {
        customKafkaListenerProperties.getListeners()
                .forEach(this::registerCustomKafkaListener);
    }

    public void registerCustomKafkaListener(String name, CustomKafkaListenerProperty customKafkaListenerProperty) {
        this.registerCustomKafkaListener(name, customKafkaListenerProperty, false);
    }

    @SneakyThrows
    public void registerCustomKafkaListener(String name, CustomKafkaListenerProperty customKafkaListenerProperty,
                                            boolean startImmediately) {
        String listenerClass = String.join(".", CustomKafkaListenerRegister.class.getPackageName(),
                customKafkaListenerProperty.getListenerClass());
        CustomMessageListener customMessageListener =
                (CustomMessageListener) beanFactory.getBean(Class.forName(listenerClass));
        kafkaListenerEndpointRegistry.registerListenerContainer(
                customMessageListener.createKafkaListenerEndpoint(name, customKafkaListenerProperty.getTopic()),
                kafkaListenerContainerFactory, startImmediately);
    }
}
