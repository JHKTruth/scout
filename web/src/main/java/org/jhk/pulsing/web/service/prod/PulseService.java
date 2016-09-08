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
package org.jhk.pulsing.web.service.prod;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.jhk.pulsing.serialization.avro.records.ACTION;
import org.jhk.pulsing.serialization.avro.records.Pulse;
import org.jhk.pulsing.serialization.avro.records.PulseId;
import org.jhk.pulsing.shared.util.CommonConstants;
import org.jhk.pulsing.shared.util.Util;
import org.jhk.pulsing.web.common.Result;
import static org.jhk.pulsing.web.common.Result.CODE.*;
import org.jhk.pulsing.web.dao.prod.db.redis.RedisPulseDao;
import org.jhk.pulsing.web.service.IPulseService;
import org.jhk.pulsing.web.service.prod.helper.PulseServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Ji Kim
 */
@Service
public class PulseService extends AbstractStormPublisher 
                            implements IPulseService {
    
    private static final Logger _LOGGER = LoggerFactory.getLogger(PulseService.class);
    
    @Inject
    @Named("redisPulseDao")
    private RedisPulseDao redisPulseDao;
    
    private ObjectMapper _objectMapper = new ObjectMapper();

    @Override
    public Result<Pulse> getPulse(PulseId pulseId) {
        Optional<Pulse> optPulse = redisPulseDao.getPulse(pulseId);
        
        return optPulse.isPresent() ? new Result<>(SUCCESS, optPulse.get()) : new Result<>(FAILURE, "Unabled to find " + pulseId);
    }
    
    
    /**
     * For creation of pulse there are couple of tasks that must be done
     * 1) Add the pulse to Redis 
     * 2) Send the message to storm of the creation (need couple of different writes to Hadoop for Data + Edges for processing)
     * 3) Send the websocket topic to clients (namely MapComponent) that a new pulse has been created (to either map or not)
     * 
     * @param pulse
     * @return
     */
    @Override
    public Result<Pulse> createPulse(Pulse pulse) {
        
        PulseId pId = PulseId.newBuilder().build();
        pId.setId(Util.uniqueId());
        pulse.setAction(ACTION.CREATE);
        pulse.setId(pId);
        pulse.setTimeStamp(Instant.now().getEpochSecond());
        
        Result<Pulse> cPulse = redisPulseDao.createPulse(pulse);
        
        if(cPulse.getCode() == SUCCESS) {
            getStormPublisher().produce(CommonConstants.TOPICS.PULSE_CREATE.toString(), cPulse.getData());
        }
        
        return cPulse;
    }
    
    /**
     * 1) Send the message to storm of the subscription (update to redis taken care of by it)
     * 
     * @param pulse
     * @return
     */
    @Override
    public Result<PulseId> subscribePulse(Pulse pulse) {
        
        getStormPublisher().produce(CommonConstants.TOPICS.PULSE_SUBSCRIBE.toString(), pulse);
        return new Result<>(SUCCESS, pulse.getId());
    }
    
    @Override
    public Map<Long, String> getTrendingPulseSubscriptions(int numMinutes) {
        
        Instant current = Instant.now();
        Instant beforeRange = current.minus(numMinutes, ChronoUnit.MINUTES);
        
        Optional<Set<String>> optTps = redisPulseDao.getTrendingPulseSubscriptions(beforeRange.getEpochSecond(), current.getEpochSecond());
        
        @SuppressWarnings("unchecked")
        Map<Long, String> tpSubscriptions = Collections.EMPTY_MAP;
        
        if(optTps.isPresent()) {
            tpSubscriptions = PulseServiceUtil.processTrendingPulseSubscribe(optTps.get(), _objectMapper);
        };
        
        return tpSubscriptions;
    }
    
    @Override
    public List<Pulse> getMapPulseDataPoints(Double lat, Double lng) {
        
        return redisPulseDao.getMapPulseDataPoints(lat, lng);
    }
    
    private ExecutorService tempEService;
    
    @Override
    public void init() {
        super.init();
        
        final List<Pulse> entries = new LinkedList<>();
        for(int createCount=0; createCount < 10; createCount++) {
            Pulse pulse = org.jhk.pulsing.web.dao.dev.PulseDao.createMockedPulse();
            pulse.setAction(ACTION.SUBSCRIBE);
            entries.add(pulse);
        }
        
        tempEService = Executors.newSingleThreadExecutor();
        tempEService.submit(() -> {
            
            long userId = 1000L;
            for(int loop=0; loop < 20; loop++) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    
                    Pulse pulse = entries.get((int) Math.random()*entries.size());
                    pulse.getUserId().setId(userId++);
                    subscribePulse(pulse);
                    
                    _LOGGER.debug("Submitted..." + pulse.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        });
    }
    
    @Override
    public void destroy() {
        super.destroy();
        
        if(tempEService != null) {
            tempEService.shutdownNow();
        }
    }
    
}
