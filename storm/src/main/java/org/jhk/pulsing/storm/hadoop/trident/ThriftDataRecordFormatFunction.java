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
package org.jhk.pulsing.storm.hadoop.trident;

import org.apache.storm.hdfs.trident.format.RecordFormat;
import org.apache.storm.trident.tuple.TridentTuple;
import org.jhk.pulsing.serialization.thrift.data.Data;
import org.jhk.pulsing.storm.common.FieldConstants;
import org.jhk.pulsing.storm.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ji Kim
 */
public final class ThriftDataRecordFormatFunction implements RecordFormat {
    
    private static final long serialVersionUID = -4520608157589524283L;
    private static final Logger _LOG = LoggerFactory.getLogger(ThriftDataRecordFormatFunction.class);
    
    @Override
    public byte[] format(TridentTuple tuple) {
        _LOG.info("ThriftDataRecordFormatFunction.format: " + tuple);
        
        Data tData = (Data) tuple.getValueByField(FieldConstants.THRIFT_DATA);
        byte[] bytes = Util.serializeThriftData(tData);
        
        _LOG.info("ThriftDataRecordFormatFunction.format serialized to bytes: " + bytes.length);
        return bytes;
    }

}