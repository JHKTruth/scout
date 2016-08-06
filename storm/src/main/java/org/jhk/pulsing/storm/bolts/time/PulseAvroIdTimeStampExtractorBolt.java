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
package org.jhk.pulsing.storm.bolts.time;

import static org.jhk.pulsing.storm.common.FieldConstants.*;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.jhk.pulsing.serialization.avro.records.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ji Kim
 */
public final class PulseAvroIdTimeStampExtractorBolt extends BaseBasicBolt {

    private static final long serialVersionUID = -8825186311978632181L;
    private static final Logger _LOG = LoggerFactory.getLogger(PulseAvroIdTimeStampExtractorBolt.class);
    
    @Override
    public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
        _LOG.info("PulseAvroIdTimeStampExtractorBolt.execute: " + tuple);
        
        Pulse pulse = (Pulse) tuple.getValueByField(AVRO_PULSE);
        
        outputCollector.emit(new Values(pulse.getTimeStamp(), pulse.getId().getId()));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer fieldsDeclarer) {
        fieldsDeclarer.declare(new Fields(TIMESTAMP, ID));
    }

}