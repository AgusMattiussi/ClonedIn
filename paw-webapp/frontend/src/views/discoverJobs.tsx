import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Card from "react-bootstrap/Card"
import Navigation from "../components/navbar"
import CategoriesSelect from "../components/categoriesSelect"
import Form from "react-bootstrap/Form"
import Dropdown from "react-bootstrap/Dropdown"
import Badge from "react-bootstrap/Badge"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import CardHeader from "react-bootstrap/esm/CardHeader"

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
              <CategoriesSelect />
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
          <Col>
            <Row>
              <h3 style={{ textAlign: "left" }}>{t("Discover Jobs")}</h3>
            </Row>
            <Row style={{ marginLeft: "5px", marginRight: "5px" }}>
              <Card className="custom-card">
                <Card style={{ marginTop: "5px", marginBottom: "5px" }}>
                  <CardHeader className="d-flex justify-content-between align-items-center">
                    <div className="d-flex justify-content-start">
                      <h5>Enterprise's Name | Job Position</h5>
                    </div>
                    <span>
                      <h5>
                        <Badge pill bg="success">
                          Category
                        </Badge>
                      </h5>
                    </span>
                  </CardHeader>
                  <br />
                  <Row className="d-flex align-items-start">
                    <Col>
                      <Row>
                        <h5>{t("Modality")}</h5>
                        <p>modalidad</p>
                      </Row>
                    </Col>
                    <Col>
                      <h5>{t("Salary")}</h5>
                      <p>salary</p>
                    </Col>
                    <Col>
                      <h5>Required Skills</h5>
                      <span>
                        <h6>
                          <Badge pill bg="success">
                            Skill
                          </Badge>
                        </h6>
                      </span>
                    </Col>
                    <Col>
                      <Row>
                        <h5>
                          <Badge bg="secondary">Contacted/Applied</Badge>
                        </h5>
                      </Row>
                    </Col>
                  </Row>
                  <Row>
                    <Col>
                      <h5>Description</h5>
                    </Col>
                    <Col></Col>
                    <Col></Col>
                    <Col>
                      <h6>
                        <Button variant="outline-dark" href="/jobOffer">
                          View More
                        </Button>
                      </h6>
                    </Col>
                  </Row>
                  <Row>
                    <Col style={{ textAlign: "left" }}>Description goes here...</Col>
                  </Row>
                </Card>
              </Card>
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
