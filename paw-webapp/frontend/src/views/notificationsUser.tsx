import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
// import Card from 'react-bootstrap/Card';
import Navigation from "../components/navbar"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"

function NotifcationsUser() {
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
              <h5 className="ml-2 mt-2">{t("Filter by status")}:</h5>
            </Row>
            <Row>
              <div className="d-flex flex-wrap justify-content-center mt-2">
                <Button variant="outline-light " className="filterbtn">
                  {t("Accepted")}
                </Button>
              </div>
            </Row>
            <Row>
              <div className="d-flex flex-wrap justify-content-center mt-2">
                <Button variant="outline-light " className="filterbtn">
                  {t("Rejected")}
                </Button>
              </div>
            </Row>
            <Row>
              <div className="d-flex flex-wrap justify-content-center mt-2">
                <Button variant="outline-light " className="filterbtn">
                  {t("Pending")}
                </Button>
              </div>
            </Row>
            <Row>
              <div className="d-flex flex-wrap justify-content-center mt-2">
                <Button variant="outline-light " className="filterbtn">
                  {t("Cancelled")}
                </Button>
              </div>
            </Row>
            <Row>
              <div className="d-flex flex-wrap justify-content-center mt-2">
                <Button variant="outline-light " className="filterbtn">
                  {t("View All")}
                </Button>
              </div>
            </Row>
          </Col>
          <Col sm={6} className="align-items-start d-flex mt-2 mr-2 mb-2">
            <Row>
              <h3>{t("Job Offers")}</h3>
            </Row>
            <Row></Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default NotifcationsUser
