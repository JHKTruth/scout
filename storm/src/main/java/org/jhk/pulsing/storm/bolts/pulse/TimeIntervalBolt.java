/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jhk.pulsing.storm.bolts.pulse;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.jhk.pulsing.storm.util.CommonBoltStreamUtil;
import org.jhk.pulsing.storm.util.PulsingConstants;

/**
 * @author Ji Kim
 */
public final class TimeIntervalBolt extends BaseBasicBolt {
    
    private static final long serialVersionUID = 3963343874691297355L;
    
    private int _secondsInterval;
    
    public TimeIntervalBolt() {
        this(PulsingConstants.DEFAULT_INTERVAL_SECONDS);
    }
    
    public TimeIntervalBolt(int secondsInterval) {
        super();
        
        _secondsInterval = secondsInterval;
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
        Long timeStamp = tuple.getLongByField("timeStamp");
        Long id = tuple.getLongByField("id");
        
        outputCollector.emit(new Values(CommonBoltStreamUtil.getTimeInterval(timeStamp, _secondsInterval),
                                        id));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer fieldsDeclarer) {
        fieldsDeclarer.declare(new Fields("timeInterval", "id"));
    }
    
}