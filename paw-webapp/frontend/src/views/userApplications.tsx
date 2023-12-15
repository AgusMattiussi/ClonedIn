import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import JobOfferApplicationsCard from "../components/cards/jobOfferApplicationCard"
import FilterStatusSideBar from "../components/sidebars/filterStatusSideBar"
import UserSortBySelect from "../components/selects/userSortBySelect"
import Pagination from "../components/pagination"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useSharedAuth } from "../api/auth"

function ApplicationsUser() {
  const [users, setUsers] = useState<any[]>([])
  const { userInfo } = useSharedAuth()

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
    document.title = t("Applications Page Title")
  }, [])

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container fluid>
        <Row className="align-items-start d-flex">
          <FilterStatusSideBar />
          <Col className="d-flex flex-column my-2">
            <Row className="my-2">
              <div className="d-flex flex-row justify-content-between">
                <h3>{t("My Applications")}</h3>
                <UserSortBySelect />
              </div>
            </Row>
            <Row>
              <Container
                className="mx-3 p-2 rounded-3"
                fluid
                style={{
                  background: "#F2F2F2",
                  boxShadow: "0 2px 4px rgba(0,0,0,0.16), 0 2px 4px rgba(0,0,0,0.23)",
                  maxWidth: "98%",
                }}
              >
                <JobOfferApplicationsCard
                  enterpriseName="Fake Enterprise"
                  category="Finance"
                  position="CEO"
                  date="18/12/2022"
                />
                <JobOfferApplicationsCard
                  enterpriseName="Fake Enterprise"
                  category="Technology"
                  position="CTO"
                  description="Loren Ipsum"
                  date="9/12/2018"
                  status="Aceptada"
                />
                <Pagination />
              </Container>
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default ApplicationsUser
