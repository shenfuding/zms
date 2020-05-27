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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.zto.zms.portal.dto.topic;

import com.zto.zms.utils.Utils;

import java.util.Map;

public class ZmsTopicConfigInfo {

    private String clusterName;

    private Map<String, Integer> topics;

    private Map<String, Integer> replications;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Map<String, Integer> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, Integer> topics) {
        this.topics = topics;
    }

    public Map<String, Integer> getReplications() {
        return replications;
    }

    public void setReplications(Map<String, Integer> replications) {
        this.replications = replications;
    }

    @Override
    public String toString() {
        return "ZmsTopicConfigInfo{" +
                "clusterName='" + clusterName + '\'' +
                ", topics=" + Utils.toString(topics) +
                '}';
    }
}

