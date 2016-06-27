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

require('./TrendingPulse.scss');

import {Grid, Row, Col, Thumbnail, Button} from 'react-bootstrap';
import React, {Component} from 'react';
import TrendingPulseStore from './TrendingPulseStore';

let _trending = new Map();

class TrendingPulseComponent extends Component {
  
  constructor(props) {
    super(props);
    
    this.state = {};
    this.store = new TrendingPulseStore();
  }
  
  componentDidMount() {
    this.store.addFetchedListener(this._onFetched.bind(this));
    this.store.fetchTrending();
  }
  
  componentWillUnmount() {
    this.store.removeFetchedListener(this._onFetched.bind(this));
  }
  
  handleSubscribe(evt) {
    console.debug('handleSubscribe', evt.target.id);
    
  }
  
  _onFetched(trending) {
    console.debug('_onFetched', trending);
    
    _trending = trending;
    this.setState({});
  }
  
  render() {
    let trending = _trending;
    let cols = [];
    
    trending.forEach((value, key) => {
      
      cols.push(<Col xs={12} sm={6} md={4} lg={3} key={key}>
        <Thumbnail alt='242x200'>
          <h3>{value}</h3>
          <p>Description</p>
          <p>
            <Button bsStyle="primary" id={key} onClick={this.handleSubscribe.bind(this)}>Subscribe</Button>
          </p>
        </Thumbnail>
      </Col>);
    });
    
    return (
      <div class='trendingpulse-component'>

        {(() => {
          
          return <Grid>
            <Row>
              {cols}
            </Row>
          </Grid>
        })()}

      </div>
    );
  }
  
}

TrendingPulseComponent.displayName = 'TrendingPulseComponent';

TrendingPulseComponent.propTypes = {};
TrendingPulseComponent.defaultProps = {};

export default TrendingPulseComponent;