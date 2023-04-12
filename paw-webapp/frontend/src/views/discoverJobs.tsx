import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
// import Card from 'react-bootstrap/Card';
import Navigation from "../components/navbar"
import Form from "react-bootstrap/Form"
import Dropdown from "react-bootstrap/Dropdown"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"

function DiscoverJobs() {
  const [users, setUsers] = useState<any[]>([])
  useEffect(() => {
    fetch("http://localhost:8080/webapp_war/users")
      .then((response) => response.json())
      .then((data) => {
        console.log(data)
        setUsers(data)
      })
      .catch((err) => {
        console.log(err.message)
      })
  }, [])

  const { t } = useTranslation()

  return (
    <div>
      <Navigation />
      <Container fluid>
        <Row className="align-items-start d-flex">
          <Col sm={2} className="sidebar">
            <Row className="search">
              <h5 className="ml-2 mt-2">{t("Search By")}</h5>
            </Row>
            <Row>
              <Form className="search">
                <Form.Control
                  type="search"
                  placeholder={t("Search Job Offer Placeholder").toString()}
                  className="me-2"
                  aria-label="Search"
                />
                <div className="d-flex flex-wrap justify-content-center mt-2">
                  <Button variant="outline-light" className="filterbtn" type="submit">
                    <Icon.Search size={15} />
                  </Button>
                </div>
              </Form>
            </Row>
            <br />
            <Row className="search">
              <h5>{t("Filter By")}</h5>
            </Row>
            <Row>
              <Dropdown>
                <Dropdown.Toggle variant="light" id="dropdown-basic" className="dropdown">
                  {t("Category")}
                </Dropdown.Toggle>
                <Dropdown.Menu>
                  <Dropdown.Item>Category 1</Dropdown.Item>
                  <Dropdown.Item>Category 2</Dropdown.Item>
                  <Dropdown.Item>Category 3</Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            </Row>
            <br />
            <Row>
              <Dropdown>
                <Dropdown.Toggle variant="light" id="dropdown" className="dropdown">
                  {t("Modality")}
                </Dropdown.Toggle>
                <Dropdown.Menu>
                  <Dropdown.Item>{t("Home Office")}</Dropdown.Item>
                  <Dropdown.Item>{t("On Site")}</Dropdown.Item>
                  <Dropdown.Item>{t("Mixed")}</Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            </Row>
            <br />
            <Row className="search">
              <h5>{t("Salary")}</h5>
            </Row>
            <Row className="search">
              <Form className="d-flex">
                <Form.Control
                  type="search"
                  placeholder={t("Minimum").toString()}
                  className="me-2"
                  aria-label="Search"
                />
                -
                <Form.Control
                  type="search"
                  placeholder={t("Maximum").toString()}
                  className="ms-2"
                  aria-label="Search"
                />
              </Form>
              <br />
              <div className="d-flex flex-wrap justify-content-center mt-2">
                <Button variant="outline-light " className="filterbtn">
                  {t("Filter")}
                </Button>
              </div>
            </Row>
            <br />
            <Row>
              <div className="d-flex flex-wrap justify-content-center">
                <Button variant="outline-light " className="filterbtn">
                  {t("Clear Filter")}
                </Button>
              </div>
            </Row>
          </Col>
          <Col sm={6} className="align-items-start d-flex mt-2 mr-2 mb-2">
            <Row>
              <h3>{t("Discover Jobs")}</h3>
            </Row>
            <Row>
              {users.map((user) => {
                return (
                  <div className="post-card" key={user.username}>
                    <h2 className="post-title">{user.username}</h2>
                    <p className="post-body">{user.self}</p>
                  </div>
                )
              })}
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default DiscoverJobs
