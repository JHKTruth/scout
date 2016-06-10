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

const SPRING_CONTROLLER_SUFFIX = ':8083/pulsing-web/controller/';
const BASIC = Symbol('basic');

const FETCH_RESPONSE_HANDLER = Object.freeze(Object.create(null, {
  
  'json': {
    get: function() {
      
      return (response, resolve, reject) => {
        this[BASIC](response, resolve, reject, 'json');
      };
      
    },
    set: function() {},
    enumerable: true
  },
  
  'blob': {
    get: function() {
      
      return (response, resolve, reject) => {
        this[BASIC](response, resolve, reject, 'blob');
      };
      
    },
    set: function() {},
    enumerable: true
  },
  
  [BASIC]: {
    get: function() {
      
      return (response, resolve, reject, basic_type) => {
        
        response[basic_type]()
          .then(function(result) {
            resolve(result);
          })
          .catch(function(err) {
            console.error(`Failure in ${basic_type} `, err);
            reject(err);
          });
        
      };
      
    },
    set: function() {},
    enumerable: true
  },
  
  'raw': {
    get: function() {
      
      return (response, resolve) => {
        resolve(response);
      };
      
    },
    set: function() {},
    enumerable: true
  }
 })
);

function controllerUrl() {
  let location = global.location;
  return location.protocol + '//' + location.hostname + SPRING_CONTROLLER_SUFFIX;
}

function fetchContent(request, options, responseType='json') {
  
  return new Promise(function(resolve, reject) {

    fetch(request, options)
      .then(function(response) {

        if(response.ok) {
          
          FETCH_RESPONSE_HANDLER[responseType](response, resolve, reject);

        }else {
          console.error('Failure in getting response ', response);
          reject(response);
        }

      })
      .catch(function(err) {
        console.error('Failure in fetching ', err);
        reject(err);
      });

  });
  
}

export default Object.freeze(
    Object.create(null,
      {
        'GET_JSON' : {
          get: function() {

            return (gPath, options=Object.create(null)) => {
              
              const DEFAULT_HEADERS = new Headers({'Accept': 'application/json'});
              const DEFAULT_OPTIONS = {method: 'GET',  mode: 'cors', headers: DEFAULT_HEADERS};

              let request = new Request(controllerUrl() + gPath);
              let gOptions = Object.assign(DEFAULT_OPTIONS, options);

              return fetchContent(request, gOptions, 'json');
            }
          },

          set: function() {},
          enumerable: true
        },
        
        'POST_JSON' : {
          get: function() {

            return (pPath, options=Object.create(null)) => {
              
              const DEFAULT_HEADERS = new Headers({'Accept': 'application/json'});
              const DEFAULT_OPTIONS = {method: 'POST',  mode: 'cors', headers: DEFAULT_HEADERS};

              let request = new Request(controllerUrl() + pPath);
              let pOptions = Object.assign(DEFAULT_OPTIONS, options);

              return fetchContent(request, pOptions, 'json');
            }
          },

          set: function() {},
          enumerable: true
        }
      }
    )
);
