import Button from 'react-bootstrap/Button';
import * as Icon from 'react-bootstrap-icons';
import Container from 'react-bootstrap/esm/Container';
import Row from 'react-bootstrap/esm/Row';
import Col from 'react-bootstrap/esm/Col';
// import Card from 'react-bootstrap/Card';
import Navigation from '../components/navbar'
import Form from 'react-bootstrap/Form';
import Dropdown from 'react-bootstrap/Dropdown';
import { useState, useEffect } from 'react';

function NotifcationsUser() {

  return (
    <div>
      <Navigation/>
      <Container fluid>
        <Row className="align-items-start d-flex">
          <Col sm={2} className="sidebar">
            <Row className="search">
              <h5 className="ml-2 mt-2">Search by:</h5>
            </Row>
            <Row>
              <Form className="search">
                <Form.Control
                  type="search"
                  placeholder="Skill, position..."
                  className="me-2"
                  aria-label="Search"
                />
                <div className='d-flex flex-wrap justify-content-center mt-2'>
                <Button variant="outline-light" className='filterbtn' type="submit"><Icon.Search size={15}/></Button>
                </div>
              </Form>
            </Row>
            <br/>
            <Row className="search">
              <h5>Filter by:</h5>
            </Row>
            <Row>
              <Dropdown>
                <Dropdown.Toggle variant="light" id="dropdown-basic" className="dropdown">
                  Category
                </Dropdown.Toggle>
                <Dropdown.Menu>
                  <Dropdown.Item>Category 1</Dropdown.Item>
                  <Dropdown.Item>Category 2</Dropdown.Item>
                  <Dropdown.Item>Category 3</Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            </Row>
            <br/>
            <Row>
              <Dropdown>
                <Dropdown.Toggle variant="light" id="dropdown" className="dropdown">
                  Modality
                </Dropdown.Toggle>
                <Dropdown.Menu>
                  <Dropdown.Item>Remoto</Dropdown.Item>
                  <Dropdown.Item>Presencial</Dropdown.Item>
                  <Dropdown.Item>Mixto</Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            </Row>
            <br/>
            <Row className = "search">
              <h5>Salary</h5>
            </Row>
            <Row className = "search">
              <Form className="d-flex" >
                <Form.Control
                  type="search"
                  placeholder="Min"
                  className="me-2"
                  aria-label="Search"
                />
                -
                <Form.Control
                  type="search"
                  placeholder="Max"
                  className="ms-2"
                  aria-label="Search"
                />
              </Form>
              <br/>
              <div className='d-flex flex-wrap justify-content-center mt-2'>
              <Button variant="outline-light "className="filterbtn">Filter</Button>
              </div>
            </Row>
            <br/>
            <Row>
            <div className='d-flex flex-wrap justify-content-center'>
            <Button variant="outline-light "className="filterbtn">Clear Filters</Button>
            </div>
            </Row>
          </Col>
          <Col sm={6} className="align-items-start d-flex mt-2 mr-2 mb-2">
            <Row>
              <h3>Job Offers</h3>
            </Row>
            <Row>
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default NotifcationsUser;