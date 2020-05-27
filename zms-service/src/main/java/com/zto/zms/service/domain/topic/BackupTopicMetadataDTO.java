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

package com.zto.zms.service.domain.topic;

/**
 * <p>Description: </p>
 *
 * @author lidawei
 * @date 2020/5/12
 * @since
 **/
public class BackupTopicMetadataDTO {
    private Integer currentEnvId;
    private Integer originEnvId;
    private String clusterName;
    private String topic;

    public BackupTopicMetadataDTO(Integer originEnvId, Integer currentEnvId, String clusterName, String topic) {
        this.currentEnvId = currentEnvId;
        this.originEnvId = originEnvId;
        this.clusterName = clusterName;
        this.topic = topic;
    }

    public Integer getCurrentEnvId() {
        return currentEnvId;
    }

    public void setCurrentEnvId(Integer currentEnvId) {
        this.currentEnvId = currentEnvId;
    }

    public Integer getOriginEnvId() {
        return originEnvId;
    }

    public void setOriginEnvId(Integer originEnvId) {
        this.originEnvId = originEnvId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}

