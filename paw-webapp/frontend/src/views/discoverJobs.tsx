import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import JobOfferDiscoverCard from "../components/cards/jobOfferDiscoverCard"
import FilterJobsSideBar from "../components/sidebars/filterJobsSideBar"
import { useState, useEffect } from "react"
import { useRequestApi } from "../api/apiRequest"
import { useTranslation } from "react-i18next"
import { useSharedAuth } from "../api/auth"
import Pagination from "../components/pagination"
import Loader from "../components/loader"
import { useNavigate } from "react-router-dom"

function DiscoverJobs() {
  const { loading, apiRequest } = useRequestApi()
  const [isLoading, setLoading] = useState(true)
  const [jobs, setJobs] = useState<any[]>([])
  const [error, setError] = useState(null)
  const navigate = useNavigate()

  useEffect(() => {
    const fetchJobs = async () => {
      const response = await apiRequest({
        url: "/jobOffers",
        method: "GET",
      })

      if (response.status === 500) {
        navigate("/403")
      }

      setJobs(response.data)
      setLoading(false)
      setError(null)
    }
    if (isLoading === true) {
      fetchJobs()
    }
  }, [apiRequest])

  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()

  document.title = t("Discover Jobs") + " | ClonedIn"

  const jobsList = jobs.map((job) => {
    return (
        <JobOfferDiscoverCard job={job} key={job.id}/>
    )
  })

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container fluid>
        <Row className="align-items-start d-flex">
          <FilterJobsSideBar />
          <Col className="align-items-start d-flex flex-column mt-2 mr-2 mb-2">
            <Row>
              <h3 style={{ textAlign: "left" }}>{t("Discover Jobs")}</h3>
            </Row>
            <Row className="w-100">
              <Container
                className="mx-3 p-2 rounded-3"
                fluid
                style={{
                  background: "#F2F2F2",
                  boxShadow: "0 2px 4px rgba(0,0,0,0.16), 0 2px 4px rgba(0,0,0,0.23)",
                }}
              >
                {isLoading ? (
                  <div className="my-5">
                    <Loader />
                  </div>
                ) : (
                  jobsList
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

export default DiscoverJobs
