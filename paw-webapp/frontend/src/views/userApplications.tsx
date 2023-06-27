import Button from "react-bootstrap/Button"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import JobOfferCard from "../components/cards/jobOfferCard"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"

function ApplicationsUser() {
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

  const [orderBy, setOrderBy] = useState("")

  const { t } = useTranslation()

  useEffect(() => {
    document.title = t("Applications Page Title")
  }, [])

  return (
    <div>
      <Navigation />
      <Container fluid>
        <Row className="align-items-start d-flex">
          <Col sm={2} className="sidebar">
            <div className="d-flex flex-column justify-content-center">
              <div className="search mx-auto">
                <h5 className="ml-2 mt-2">{t("Filter by status")}:</h5>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button variant="outline-light " className="filterbtn">
                  {t("Accepted")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button variant="outline-light " className="filterbtn">
                  {t("Rejected")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button variant="outline-light " className="filterbtn">
                  {t("Pending")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button variant="outline-light " className="filterbtn">
                  {t("Cancelled")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-4 mx-auto" style={{ maxWidth: "fit-content" }}>
                <Button variant="outline-light " className="filterbtn">
                  {t("View All")}
                </Button>
              </div>
            </div>
          </Col>
          <Col className="d-flex flex-column my-2">
            <Row className="my-2">
              <div className="d-flex flex-row justify-content-between">
                <h3>{t("My Applications")}</h3>
                <div style={{ width: "200px" }}>
                  <Form.Select value={orderBy} onChange={(e) => setOrderBy(e.target.value)}>
                    <option value="0">{t("Order By")}</option>
                    <option value="4">{t("Date asc")}</option>
                    <option value="5">{t("Date desc")}</option>
                  </Form.Select>
                </div>
              </div>
            </Row>
            <Row>
              <Container
                className="mx-3 p-2 rounded-3 d-flex flex-wrap"
                fluid
                style={{
                  background: "#F2F2F2",
                  boxShadow: "0 2px 4px rgba(0,0,0,0.16), 0 2px 4px rgba(0,0,0,0.23)",
                  maxWidth: "98%",
                }}
              >
                <JobOfferCard
                  enterpriseName="Fake Enterprise"
                  category="Finance"
                  position="CEO"
                  date="18/12/2022"
                  contacted={true}
                  applicationsCard={true}
                />
                <JobOfferCard
                  enterpriseName="Fake Enterprise"
                  category="Technology"
                  position="CTO"
                  description="Loren Ipsum"
                  date="9/12/2018"
                  status="Aceptada"
                  applicationsCard={true}
                />
              </Container>
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default ApplicationsUser
