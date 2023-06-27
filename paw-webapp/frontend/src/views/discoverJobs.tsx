import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import JobOfferDiscoverCard from "../components/cards/jobOfferDiscoverCard"
import FilterJobsSideBar from "../components/sidebars/filterJobsSideBar"
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

  useEffect(() => {
    document.title = t("Discover Jobs") + " | ClonedIn"
  }, [])

  return (
    <div>
      <Navigation />
      <Container fluid>
        <Row className="align-items-start d-flex">
          <FilterJobsSideBar />
          <Col className="align-items-start d-flex flex-column mt-2 mr-2 mb-2">
            <Row>
              <h3 style={{ textAlign: "left" }}>{t("Discover Jobs")}</h3>
            </Row>
            <Row className="w-100">
              <Container
                className="mx-3 p-2 rounded-3 d-flex flex-wrap"
                fluid
                style={{
                  background: "#F2F2F2",
                  boxShadow: "0 2px 4px rgba(0,0,0,0.16), 0 2px 4px rgba(0,0,0,0.23)",
                }}
              >
                <JobOfferDiscoverCard
                  enterpriseName="Fake Enterprise"
                  category="Finance"
                  position="CEO"
                  salary="100000"
                  contacted={true}
                />
                <JobOfferDiscoverCard category="Technology" position="CTO" description="Loren ipsum" />
              </Container>
              {/* {users.map((user) => {
                return (
                  <div className="post-card" key={user.username}>
                    <h2 className="post-title">{user.username}</h2>
                    <p className="post-body">{user.self}</p>
                  </div>
                )
              })} */}
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default DiscoverJobs
