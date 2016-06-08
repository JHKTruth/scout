'use strict';

require('./NavBar.scss');

import React, {Component, PropTypes} from 'react';
import {IndexLink} from 'react-router';
import {Navbar, Nav, NavItem} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';

class NavBarComponent extends Component {
  
  constructor(props) {
    super(props);
    
    this.state = {loggedIn: false};
  }
  
  render() {
    
    return (
        <Navbar inverse>
          <Navbar.Header>
            <Navbar.Brand>
              <IndexLink to='/'>Pulsing</IndexLink>
            </Navbar.Brand>
            <Navbar.Toggle/>
          </Navbar.Header>
          
          <Navbar.Collapse>
            <Nav>
              <LinkContainer to='/'><NavItem>Trending</NavItem></LinkContainer>
            </Nav>
            
            <Nav pullRight>
              {this.state.loggedIn ? <NavItem href='/logout'>Logout</NavItem> : <NavItem href='/login'>Login</NavItem>}
            </Nav>
          </Navbar.Collapse>
        </Navbar>
        );
  }
  
}

NavBarComponent.displayName = 'NavBarComponent';

NavBarComponent.propTypes = {
  user: PropTypes.object
};
NavBarComponent.defaultProps = {
  user: null
};

export default NavBarComponent;