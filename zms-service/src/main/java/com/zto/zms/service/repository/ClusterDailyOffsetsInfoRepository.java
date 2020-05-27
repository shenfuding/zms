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

package com.zto.zms.service.repository;

import com.zto.zms.service.domain.influxdb.ClusterDailyOffsetsInfo;
import com.zto.zms.service.influx.InfluxdbClient;
import com.zto.zms.service.util.StringFormatUtils;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>Class: StatisticClusterDailyOffsetsInfoRepository</p>
 * <p>Description: </p>
 *
 * @author lidawei
 * @date 2020/1/20
 **/
@Repository
public class ClusterDailyOffsetsInfoRepository {
    @Autowired
    private InfluxdbClient influxdbClient;


    /**
     * 获取集群最大消息偏移量
     *
     * @param clusterName
     * @return
     */
    public ClusterDailyOffsetsInfo getLastClusterMaxOffset(String clusterName) {

        String sqlBuilder = "select * from statistic_cluster_daily_offsets_info" +
                " where \"clusterName\" = '{0}'" +
                " and \"name\" = 'maxOffset'" +
                " order by time desc limit 1";
        String sql = StringFormatUtils.format(sqlBuilder, clusterName);
        List<ClusterDailyOffsetsInfo> dailyOffsetsInfos = getQueryResult(sql);
        if (CollectionUtils.isEmpty(dailyOffsetsInfos)) {
            return null;
        }
        return dailyOffsetsInfos.get(0);
    }

    /**
     * 集群日消息增长量
     *
     * @param clusterName
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<ClusterDailyOffsetsInfo> queryClusterIncrementOffset(String clusterName, long beginTime, long endTime) {
        String sqlBuilder = "select * from statistic_cluster_daily_offsets_info" +
                " where  time >= {0}ms and time < {1}ms" +
                " and \"clusterName\" = '{2}'" +
                " and \"name\" = 'incrementVal'";
        String sql = StringFormatUtils.format(sqlBuilder, beginTime, endTime, clusterName);
        return getQueryResult(sql);
    }

    private List<ClusterDailyOffsetsInfo> getQueryResult(String sql) {
        QueryResult queryResult = influxdbClient.query(sql);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(queryResult, ClusterDailyOffsetsInfo.class);
    }

}

