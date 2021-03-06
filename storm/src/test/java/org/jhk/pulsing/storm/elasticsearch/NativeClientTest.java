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
package org.jhk.pulsing.storm.elasticsearch;

import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.apache.avro.specific.SpecificRecord;
import org.elasticsearch.node.NodeValidationException;
import org.jhk.pulsing.serialization.avro.records.Pulse;
import org.jhk.pulsing.serialization.avro.records.User;
import org.jhk.pulsing.serialization.avro.records.UserId;
import org.jhk.pulsing.shared.util.Util;
import org.jhk.pulsing.storm.converter.AvroToElasticDocumentConverter;
import org.jhk.pulsing.storm.elasticsearch.NativeClient.NativeClientDocument;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Ji Kim
 */
public class NativeClientTest {
    
    private static NativeClient _nClient;
    
    @BeforeClass
    public static void setup() {
        
        try {
            _nClient = new NativeClient();
            assertTrue("Successfully created the NativeCLient.", true);
        } catch (NodeValidationException | UnknownHostException nvException) {
            assertTrue("Failed in creating the NativeCLient.", false);
            nvException.printStackTrace();
            throw new RuntimeException(nvException);
        }
        
    }
    
    @Test
    public void testCreatePulses() {
        class PulseTest {
            private String description;
            private String name;
            private long userId;
            private long timeStamp;
            private List<CharSequence> tags;
            private double lat;
            private double lon;
            
            private PulseTest(String description, String name, long userId, long timeStamp, List<CharSequence> tags, double lat, double lon) {
                super();
                
                this.description = description;
                this.name = name;
                this.userId = userId;
                this.timeStamp = timeStamp;
                this.tags = tags;
                this.lat = lat;
                this.lon = lon;
            }
        }
        
        final PulseTest[] pulseData = new PulseTest[] {
                new PulseTest("Basketball @SanFran", "Basketball", Util.uniqueId(), Instant.now().getEpochSecond(), new LinkedList<CharSequence>(){{
                    add("basketball");
                    add("sport");
                    add("bayarea");
                }}, 37.773972, -122.431297),
                new PulseTest("Pizza @Pizza Suprema", "Pizza Eat", Util.uniqueId(), Instant.now().getEpochSecond(), new LinkedList<CharSequence>(){{
                    add("pizza");
                    add("eat");
                    add("food");
                    add("eastside");
                }}, 40.7128, 74.0059),
                new PulseTest("Overwatch @6PM PST", "Overwatch", Util.uniqueId(), Instant.now().getEpochSecond(), new LinkedList<CharSequence>(){{
                    add("overwatch");
                    add("game");
                }}, 40.7128, 74.0059),
                new PulseTest("Study @9PM PST Starbucks", "Reading", Util.uniqueId(), Instant.now().getEpochSecond(), new LinkedList<CharSequence>(){{
                    add("study");
                    add("coffee");
                }}, 40.7128, 74.0059),
                new PulseTest("Big bang theory @8PM PST", "TV - Big bang theory", Util.uniqueId(), Instant.now().getEpochSecond(), new LinkedList<CharSequence>(){{
                    add("bigbangtheory");
                    add("tv");
                }}, 37.773972, -122.431297)
        };
        
        Function<SpecificRecord, JSONObject> _toJsonConverter = AvroToElasticDocumentConverter.getAvroToElasticDocFunction(AvroToElasticDocumentConverter.AVRO_TO_ELASTIC_DOCUMENT.PULSE);
        List<NativeClientDocument> documents = new LinkedList<>();
        
        Arrays.asList(pulseData).forEach(pData -> {
            Pulse user = Pulse.newBuilder().setDescription(pData.description).setValue(pData.name)
                    .setTimeStamp(pData.timeStamp).setTags(pData.tags)
                    .setLat(pData.lat).setLng(pData.lon)
                    .setUserId(UserId.newBuilder().setId(pData.userId).build())
                    .build();
            
            documents.add(new NativeClientDocument("pulse", "pulse_tags", Util.uniqueId() + "", _toJsonConverter.apply(user).toString()));
        });
        
        _nClient.bulkAdd(documents);
        _nClient.refresIndices("pulse");
    }
    
    @Test
    public void testCreateUsers() {
        final String[][] userData = new String[][] {
            new String[]{"Isaac Newton", "iNewton@mathPhysics.com", "52.809167", "-0.630556"},
            new String[]{"Euclid of Alexandria", "eAlexandria@mathPhysics.com", "31.2", "29.916667"},
            new String[]{"Archimedes", "archimedes@mathPhysics.com", "37.083333", "15.283333"},
            new String[]{"Pascal", "pascal@mathPhysics.com", "45.7831", "3.0824"},
            new String[]{"James Clerk Maxwell", "jcMaxwell@mathPhysics.com", "55.953056", "-3.188889"},
            new String[]{"Socrates", "socrates@philosophy.com", "37.9667", "23.7167"},
            new String[]{"Plato", "plato@philosophy.com", "37.9667", "23.7167"}
        };
        
        Function<SpecificRecord, JSONObject> _toJsonConverter = AvroToElasticDocumentConverter.getAvroToElasticDocFunction(AvroToElasticDocumentConverter.AVRO_TO_ELASTIC_DOCUMENT.USER);
        List<NativeClientDocument> documents = new LinkedList<>();
        
        Arrays.asList(userData).forEach(uData -> {
            long id = Util.uniqueId();
            User user = User.newBuilder().setName(uData[0]).setEmail(uData[1]).setId(UserId.newBuilder().setId(id).build())
                    .setLat(Double.parseDouble(uData[2])).setLng(Double.parseDouble(uData[3]))
                    .build();
            
            documents.add(new NativeClientDocument("user", "user_tags", id + "", _toJsonConverter.apply(user).toString()));
        });
        
        _nClient.bulkAdd(documents);
        _nClient.refresIndices("user");
    }
    
}
