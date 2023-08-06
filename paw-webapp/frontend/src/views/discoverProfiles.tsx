import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import ProfileUserCard from "../components/cards/profileUserCard"
import FilterProfilesSideBar from "../components/sidebars/filterProfilesSideBar"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import Pagination from "../components/pagination"
import Loader from "../components/loader"

function DiscoverProfiles() {
  const [users, setUsers] = useState<any[]>([])
  const [isLoading, setLoading] = useState(true)

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
      .finally(() => {
        setLoading(false)
      })
  }, [])

  const { t } = useTranslation()

  document.title = t("Discover Profiles") + " | ClonedIn"

  const usersList = users.map((user) => {
    return (
      <a href="/profileUser" style={{ textDecoration: "none", color: "black" }}>
        <ProfileUserCard user={user} />
      </a>
    )
  })

  return (
    <div>
      <Navigation isEnterprise={true} />
      <Container fluid>
        <Row className="align-items-start d-flex vh-100">
          <FilterProfilesSideBar />
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
                className="p-2 rounded-3 d-flex flex-wrap w-auto justify-content-center"
                fluid
                style={{
                  background: "#F2F2F2",
                  boxShadow: "0 0 10px rgba(0,0,0,0.16), 0 0 4px rgba(0,0,0,0.23)",
                  minWidth: "100vh",
                }}
              >
                {isLoading ? (
                  <div className="my-5">
                    <Loader />
                  </div>
                ) : (
                  usersList
                )}
                {/* <Pagination /> */}
              </Container>
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default DiscoverProfiles
