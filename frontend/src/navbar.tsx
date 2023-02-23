import React from 'react';
import logo from './images/logo.png';
import * as Icon from 'react-bootstrap-icons';
import './App.css';
// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css";
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min";

import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';

function Navigation() {
  return (
    <Navbar expand="lg" className="color-nav">
      <Container>
        <Navbar.Brand href="#home">
          <img
            src={logo}
            alt=""
            height="40"
            className="d-inline-block align-top"
          />
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link className = "nav-item" href="#jobs">Discover Jobs</Nav.Link>
            <Nav.Link className = "nav-item" href="#profile">My Profile</Nav.Link>
            <Nav.Link className = "nav-item" href="#offers">Job Offers</Nav.Link>
            <Nav.Link className = "nav-item"href="#applications">My Applications</Nav.Link>
          </Nav>
          <button type="button" className="btn btn-outline-light waves-effect nav-item" >
                    <i className="bi bi-box-arrow-right pr-2 nav-item"></i>
                    <Icon.BoxArrowRight size={25}/>
                    Logout
          </button>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}
export default Navigation;