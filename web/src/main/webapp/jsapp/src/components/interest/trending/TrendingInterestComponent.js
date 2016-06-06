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

require('./TrendingInterest.scss');

import React, {Component} from 'react';
import TrendingInterestStore from './TrendingInterestStore';

const _store = new TrendingInterestStore();

class TrendingInterestComponent extends Component {
  
  constructor(props) {
    super(props);
    
    TrendingInterestStore.trending
      .then(function(result) {
        
        this.state = result;
      }, function(err) {
        
        console.error(err);
      }).bind(this);
  }
  
  componentDidMount() {
    _store.addChangeListener(this._onChange);
  }
  
  componentWillUnmount() {
    _store.removeChangeListener(this._onChange);
  }
  
  _onChange() {
    this.setState(TrendingInterestStore.trending);
  }
  
  render() {
    return (
      <div>
        <div>Trending</div>
      </div>
    );
  }
  
}

TrendingInterestComponent.displayName = 'TrendingInterestComponent';

TrendingInterestComponent.propTypes = {};
TrendingInterestComponent.defaultProps = {};

export default TrendingInterestComponent;