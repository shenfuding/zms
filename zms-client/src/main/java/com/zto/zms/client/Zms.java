/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zto.zms.client;

import com.google.common.collect.Sets;
import com.zto.zms.client.common.SimpleMessage;
import com.zto.zms.common.ZmsConst;
import com.zto.zms.common.ZmsEnv;
import com.zto.zms.client.common.ZmsMessageBuilder;
import com.zto.zms.client.consumer.ConsumerFactory;
import com.zto.zms.client.consumer.ConsumerGroup;
import com.zto.zms.client.consumer.MessageListener;
import com.zto.zms.logger.ZmsLogger;
import com.zto.zms.client.metrics.ZmsStatsReporter;
import com.zto.zms.client.producer.Producer;
import com.zto.zms.client.producer.ProducerFactory;
import com.zto.zms.client.producer.SendResponse;
import com.zto.zms.client.producer.ZmsCallBack;
import com.zto.zms.zookeeper.RouterManager;
import org.slf4j.Logger;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by superheizai on 2017/7/26.
 */
public class Zms extends AbstractZmsService {

    public static final Logger logger = ZmsLogger.log;

    private final ZmsStatsReporter reporter;

    private static class InstanceHolder {
        private static final Zms ZMS = new Zms();
    }

    public static boolean isWorking() {
        return InstanceHolder.ZMS.isRunning();
    }

    private Zms() {
        logger.info("zms version {} initialized for {}", ZmsEnv.ZMS_VERSION, ZmsEnv.ZMS_IP);
        running = true;
        reporter = new ZmsStatsReporter();
        reporter.start(10, TimeUnit.SECONDS);
        logger.info("zms initilized");
    }

    @Override
    public void start() {
    }

    @Override
    public void shutdown() {
        reporter.shutdown();
        ProducerFactory.shutdown();
        ConsumerFactory.shutdown();
        RouterManager.getInstance().shutdown();
        running = false;
        logger.info("zms has been shutdown");
    }

    /**
     * 关闭指定Topic的Producer
     *
     * @param topic
     */
    public static void stopProducer(String topic) {
        ProducerFactory.shutdown(topic);
    }

    /**
     * 关闭指定消费组的Consumer
     *
     * @param consumerGroup
     */
    public static void stopConsumer(String consumerGroup) {
        ConsumerFactory.shutdown(consumerGroup);
    }

    public static void stop() {
        InstanceHolder.ZMS.shutdown();
    }

    public static void subscribe(String consumerGroup, MessageListener listener) {
        InstanceHolder.ZMS.doSubscribe(consumerGroup, listener);
    }

    public static void subscribe(String consumerGroup, Set<String> tags, MessageListener listener, Properties properties) {

        InstanceHolder.ZMS.doSubscribe(consumerGroup, tags, listener, properties);
    }


    public static void subscribe(String consumerGroup, MessageListener listener, Properties properties) {

        InstanceHolder.ZMS.doSubscribe(consumerGroup, Sets.newHashSet(), listener, properties);
    }

    public static void subscribe(String consumerGroup, Set<String> tags, MessageListener listener) {

        InstanceHolder.ZMS.doSubscribe(consumerGroup, tags, listener);
    }

    public static void subscribe(String consumerGroup, String tag, MessageListener listener) {

        InstanceHolder.ZMS.doSubscribe(consumerGroup, Sets.newHashSet(tag), listener);
    }

    public static SendResponse send(String topic, SimpleMessage simpleMessage) {
        return InstanceHolder.ZMS.doSendSync(topic, simpleMessage, null);
    }


    public static void sendAsync(String topic, SimpleMessage simpleMessage, ZmsCallBack callBack) {
        InstanceHolder.ZMS.doSendAsync(topic, simpleMessage, null, callBack);
    }


    public static void sendOneway(String topic, SimpleMessage simpleMessage) {
        InstanceHolder.ZMS.doSendOneway(topic, simpleMessage);
    }


    public static SendResponse send(String topic, SimpleMessage simpleMessage, Properties properties) {
        return InstanceHolder.ZMS.doSendSync(topic, simpleMessage, properties);
    }


    public static void sendAsync(String topic, SimpleMessage simpleMessage, Properties properties, ZmsCallBack callBack) {
        InstanceHolder.ZMS.doSendAsync(topic, simpleMessage, properties, callBack);
    }

    private void doSubscribe(String consumerGroup, MessageListener listener) {

        if (!running) {
            logger.error("ZMS is not running,will not consume message");
            return;
        }
        ConsumerFactory.getConsumer(new ConsumerGroup(consumerGroup), new Properties(), listener);

    }

    private void doSubscribe(String consumerGroup, Set<String> tags, MessageListener listener) {
        if (!running) {
            logger.error("ZMS is not running,will not consume message");
            return;
        }
        ConsumerFactory.getConsumer(new ConsumerGroup(consumerGroup, ZmsConst.DEFAULT_NAME_CONSUMER, tags), new Properties(), listener);
    }

    private void doSubscribe(String consumerGroup, Set<String> tags, MessageListener listener, Properties properties) {
        if (!running) {
            logger.error("ZMS is not running,will not consume message");
            return;
        }
        ConsumerFactory.getConsumer(new ConsumerGroup(consumerGroup, ZmsConst.DEFAULT_NAME_CONSUMER, tags), properties, listener);
    }

    private void doSendOneway(String topic, SimpleMessage simpleMessage) {
        if (!running) {
            logger.warn("ZMS is not running,will not send message");
            return;
        }
        Producer producer = ProducerFactory.getProducer(topic);
        producer.oneway(ZmsMessageBuilder.newInstance().buildPaylod(simpleMessage.getPayload()).buildKey(simpleMessage.getKey()).buildTags(simpleMessage.getTags()).buildDelayLevel(simpleMessage.getDelayLevel()).build());
    }

    private SendResponse doSendSync(String topic, SimpleMessage simpleMessage, Properties properties) {
        if (!running) {
            logger.warn("ZMS is not running,will not send message");
            return SendResponse.buildErrorResult("ZMS is not running");
        }
        Producer producer = ProducerFactory.getProducer(topic, properties);
        return producer.syncSend(ZmsMessageBuilder.newInstance().buildPaylod(simpleMessage.getPayload()).buildKey(simpleMessage.getKey()).buildTags(simpleMessage.getTags()).buildDelayLevel(simpleMessage.getDelayLevel()).build());
    }

    private void doSendAsync(String topic, SimpleMessage simpleMessage, Properties properties, ZmsCallBack callBack) {
        if (!running) {
            logger.warn("ZMS is not running,will not send message");
            return;
        }

        Producer producer = ProducerFactory.getProducer(topic, properties);
        producer.asyncSend(ZmsMessageBuilder.newInstance().buildPaylod(simpleMessage.getPayload()).buildKey(simpleMessage.getKey()).buildTags(simpleMessage.getTags()).buildDelayLevel(simpleMessage.getDelayLevel()).build(), callBack);
    }


}

