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

/**
 * @author Ji Kim
 */
'use strict';

import User from '../avro/User';
import PulseId from '../avro/PulseId';

const PULSING_USER_KEY = 'pulsingUser';
const PULSING_SUBSCRIBED_PULSE_ID_KEY = 'pulsingSubscribedPulseId';
const MAPPER = new Map();

function _get(key, AvroClazz) {
  if(MAPPER.has(key)) {
    return MAPPER.get(key);
  }

  let jsonStr = sessionStorage.getItem(key);
  let obj = null;

  if(jsonStr !== null) {
    obj = AvroClazz.deserialize(JSON.parse(jsonStr));
    MAPPER.set(key, obj);
  }

  return obj;
}

function _set(key, json, AvroClazz) {
  if(!json) {
    sessionStorage.removeItem(key);
    MAPPER.delete(key);
    return;
  }

  let obj = AvroClazz.deserialize(json);

  MAPPER.set(key, obj);
  sessionStorage.setItem(key, obj.serialize());
}

export default Object.freeze(
    Object.create(null,
      {
        'user' : {
          get: function() {

            return _get(PULSING_USER_KEY, User);
          },
          set: function(userJson) {

            _set(PULSING_USER_KEY, userJson, User);
          },
          enumerable: true
        },

        'subscribedPulseId' : {
          get: function() {
            
            return _get(PULSING_SUBSCRIBED_PULSE_ID_KEY, PulseId);
          },
          set: function(pulseIdJson) {

            _set(PULSING_SUBSCRIBED_PULSE_ID_KEY, pulseIdJson, PulseId);
          },
          enumerable: true
        }
      }
    )
);
