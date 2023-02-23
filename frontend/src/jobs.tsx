import Button from 'react-bootstrap/Button';
import * as Icon from 'react-bootstrap-icons';
import Container from 'react-bootstrap/esm/Container';
import Row from 'react-bootstrap/esm/Row';
import Col from 'react-bootstrap/esm/Col';
// import Card from 'react-bootstrap/Card';
import Navigation from './navbar'
import Form from 'react-bootstrap/Form';
import Dropdown from 'react-bootstrap/Dropdown';

function Jobs() {
  return (
    <div>
      <Navigation/>
      <Container fluid>
        <Row className="align-items-start d-flex">
          <Col sm={2} className="sidebar">
            <Row className="search">
              <h5>Search by:</h5>
            </Row>
            <Row>
              <Form className="d-flex">
                <Form.Control
                  type="search"
                  placeholder="Skill, position..."
                  className="me-2"
                  aria-label="Search"
                />
                <Button variant="outline-light "className="search"><Icon.Search size={15}/></Button>
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
                  <Dropdown.Item>Modality 1</Dropdown.Item>
                  <Dropdown.Item>Modality 2</Dropdown.Item>
                  <Dropdown.Item>Modality 3</Dropdown.Item>
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
                <Form.Control
                  type="search"
                  placeholder="Max"
                  className="me-2"
                  aria-label="Search"
                />
              </Form>
              <br/>
              <Button variant="outline-light "className="search">Filter</Button>
            </Row>
            <br/>
            <Row>
            <Button variant="outline-light "className="search">Clear Filters</Button>
            </Row>
          </Col>
          <Col sm={6} className="align-items-start d-flex">
            <Row>
              <h3>Discover Jobs</h3>
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default Jobs;