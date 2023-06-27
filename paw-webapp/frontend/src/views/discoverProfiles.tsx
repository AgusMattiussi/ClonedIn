import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import Form from "react-bootstrap/Form"
import CategoriesSelect from "../components/selects/categoriesSelect"
import EducationLevelSelect from "../components/selects/educationLevelSelect"
import ProfileCard from "../components/cards/profileCard"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"

function DiscoverProfiles() {
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

  useEffect(() => {
    document.title = t("Discover Profiles") + " | ClonedIn"
  }, [])

  return (
    <div>
      <Navigation isEnterprise={true} />
      <Container fluid>
        <Row className="align-items-start d-flex vh-100">
          <Col sm={2} className="sidebar">
            <Row className="search">
              <h5 className="ml-2 mt-2">{t("Search By")}</h5>
            </Row>
            <Row>
              <Form className="search">
                <Form.Control
                  type="search"
                  placeholder={t("Search Profile Placeholder").toString()}
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
              <EducationLevelSelect />
            </Row>
            <Row className="search mt-2">
              <div className="d-flex justify-content-center">
                <h6>{t("Years Of Experience")}</h6>
              </div>
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
          <Col className="align-items-start d-flex flex-column mt-2 mr-2 mb-2">
            <Row>
              <h3>{t("Discover Profiles")}</h3>
            </Row>
            <Row
              className="rounded-3 d-flex flex-row flex-wrap w-auto"
              style={{
                marginLeft: "0px",
                marginRight: "3px",
              }}
            >
              <Container
                className="p-2 rounded-3 d-flex flex-row flex-wrap w-auto justify-content-center"
                fluid
                style={{
                  background: "#F2F2F2",
                  boxShadow: "0 0 10px rgba(0,0,0,0.16), 0 0 4px rgba(0,0,0,0.23)",
                }}
              >
                <a href="/profileUser" style={{ textDecoration: "none", color: "black" }}>
                  <ProfileCard category="test" position="CEO" location="CABA" />
                </a>
                <a href="/profileUser" style={{ textDecoration: "none", color: "black" }}>
                  <ProfileCard category="test1" contacted={true} />
                </a>
              </Container>
            </Row>
            {/* <Row>
              {users.map((user) => {
                return (
                  <div className="post-card" key={user.username}>
                    <h2 className="post-title">{user.username}</h2>
                    <p className="post-body">{user.self}</p>
                  </div>
                )
              })}
            </Row> */}
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default DiscoverProfiles
