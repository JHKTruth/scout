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

import * as types from '../common/eventTypes';

const ERROR_MESSAGE_CAP = 20;
const STATE = {
  errorMessages: [],
  alerts: [],
};

export default function app(state = STATE, action) {
  switch (action.type) {
    case types.ERROR_MESSAGE: {
      if (state.errorMessages.length === ERROR_MESSAGE_CAP) {
        state.errorMessages.splice(0, 1);
      }
      state.errorMessages.push(action.payload.error);
      const errorMessages = state.errorMessages;
      return { ...state, ...errorMessages };
    }
    case types.ALERT_UPDATED: {
      return { ...state, ...action.payload };
    }
    default:
      return state;
  }
}
