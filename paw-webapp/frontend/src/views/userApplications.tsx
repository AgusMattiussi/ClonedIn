import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import FilterStatusSideBar from "../components/sidebars/filterStatusSideBar"
import UserSortBySelect from "../components/selects/userSortBySelect"
import Pagination from "../components/pagination"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useSharedAuth } from "../api/auth"
import { useRequestApi } from "../api/apiRequest"
import UserDto from "../utils/UserDto"
import { useNavigate, useParams } from "react-router-dom"
import Loader from "../components/loader"
import JobOfferUserCard from "../components/cards/jobOfferUserCard"

function ApplicationsUser() {
  const { loading, apiRequest } = useRequestApi()

  const [user, setUser] = useState<UserDto | undefined>({} as UserDto)
  const [isUserLoading, setUserLoading] = useState(true)

  const [applications, setApplications] = useState<any[]>([])
  const [applicationsLoading, setApplicationsLoading] = useState(true)

  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const navigate = useNavigate()

  useEffect(() => {
    const fetchUser = async () => {
      const response = await apiRequest({
        url: `/users/${id}`,
        method: "GET",
      })

      if (response.status === 500) {
        navigate("/403")
      }

      setUser(response.data)
      setUserLoading(false)
    }

    const fetchAplications = async () => {
      const response = await apiRequest({
        url: `/users/${id}/applications`,
        method: "GET",
      })
      setApplications(response.data)
      setApplicationsLoading(false)
    }
    if (isUserLoading === true) {
      fetchUser()
    }
    if(applicationsLoading === true) {
      fetchAplications()
    } 

  }, [apiRequest, id])

  const userApplications = applications.map((application, index) => {
    return (
      <JobOfferUserCard job={application} key={index}/>
    )
  })

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
                {applicationsLoading ? (
                  <div className="my-5">
                    <Loader />
                  </div>
                ) : userApplications.length === 0 ? (
                  <div className="my-5 w-100">
                    <h5>{t("No job offers found")}</h5>
                  </div>
                ) : (
                  userApplications
                )}
                
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
