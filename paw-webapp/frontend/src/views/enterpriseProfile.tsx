import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import ProfileEnterpriseCard from "../components/cards/profileEnterpriseCard"
import JobOfferEnterpriseCard from "../components/cards/jobOfferEnterpriseCard"
import Navigation from "../components/navbar"
import Pagination from "../components/pagination"
import { useTranslation } from "react-i18next"
import { useEffect, useState } from "react"
import { useSharedAuth } from "../api/auth"
import { useRequestApi } from "../api/apiRequest"
import EnterpriseDto from "../utils/EnterpriseDto"
import { useNavigate, useParams } from "react-router-dom"
import { BASE_URL } from "../utils/constants"

function ProfileEnterprise() {
  const { loading, apiRequest } = useRequestApi()

  const [enterprise, setEnterprise] = useState<EnterpriseDto | undefined>({} as EnterpriseDto)
  const [isEnterpriseLoading, setEnterpriseLoading] = useState(true)

  const [jobs, setJobs] = useState<any[]>([])
  const [jobsLoading, setJobsLoading] = useState(true)

  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const navigate = useNavigate()

  const API_URL = BASE_URL + `/enterprises/${id}/`

  useEffect(() => {
    const fetchEnterprise = async () => {
      const response = await apiRequest({
        url: `/enterprises/${id}/`,
        method: "GET",
      })

      if (response.status === 500) {
        navigate("/403")
      }

      setEnterprise(response.data)
      setEnterpriseLoading(false)
    }

    const fetchJobs = async () => {
      const response = await apiRequest({
        url: API_URL + "jobOffers",
        method: "GET",
      })
      setJobs(response.data)
      setJobsLoading(false)
    }
    if (isEnterpriseLoading === true) {
      fetchEnterprise()
    }
    if (jobsLoading === true) {
      fetchJobs()
    }
  }, [apiRequest, id])

  const enterprisesJobs = jobs.map((job) => {
    return <JobOfferEnterpriseCard job={job} key={job.id} />
  })

  document.title = enterprise?.name + " | ClonedIn"

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container fluid style={{ background: "#F2F2F2", height: "800px" }}>
        <Row className="row">
          <Col sm={3} className="col d-flex flex-column align-items-center">
            <br />
            <ProfileEnterpriseCard enterprise={enterprise} editable={true} />
          </Col>
          <Col sm={8} className="col d-flex flex-column align-items-center">
            <br />
            {userInfo?.role === "ENTERPRISE" ? (
              <Button variant="success" type="button" onClick={() => navigate(`jobOffers`)}>
                <div className="d-flex align-items-center justify-content-center">
                  <Icon.PlusSquare color="white" size={20} style={{ marginRight: "7px" }} />
                  {t("Add Job Offer")}
                </div>
              </Button>
            ) : (
              <></>
            )}
            <br />
            {enterprisesJobs.length > 0 ? (
              <div className="w-100">{enterprisesJobs}</div>
            ) : (
              <div style={{ fontWeight: "bold" }}>{"There are no job Offers"}</div>
            )}
            <Pagination />
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default ProfileEnterprise
