import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import ProfileEnterpriseCard from "../components/cards/profileEnterpriseCard"
import JobOfferEnterpriseCard from "../components/cards/jobOfferEnterpriseCard"
import Navigation from "../components/navbar"
import Pagination from "../components/pagination"
import Loader from "../components/loader"
import EnterpriseDto from "../utils/EnterpriseDto"
import { JobOfferAvailability, UserRole } from "../utils/constants"
import { useTranslation } from "react-i18next"
import { useEffect, useState } from "react"
import { useSharedAuth } from "../api/auth"
import { useGetEnterpriseById } from "../hooks/useGetEnterpriseById"
import { useGetEnterpriseJobOffers } from "../hooks/useGetEnterpriseJobOffers"
import { useGetJobOffersForUser } from "../hooks/useGetJobOffersForUser"
import { usePostJobOfferStatus } from "../hooks/usePostJobOfferStatus"
import { createSearchParams, useNavigate, useParams } from "react-router-dom"
import { HttpStatusCode } from "axios"

function ProfileEnterprise() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()

  const { getEnterpriseById } = useGetEnterpriseById()
  const { getEnterpriseJobOffers } = useGetEnterpriseJobOffers()
  const { getUserJobs } = useGetJobOffersForUser()
  const { closeJobOffer } = usePostJobOfferStatus()

  const [enterprise, setEnterprise] = useState<EnterpriseDto | undefined>({} as EnterpriseDto)
  const [isEnterpriseLoading, setEnterpriseLoading] = useState(true)

  const [jobs, setJobs] = useState<any[]>([])
  const [userJobs, setUserJobs] = useState<any[]>([])
  const [jobsLoading, setJobsLoading] = useState(true)

  const [jobOfferToCloseId, setJobOfferToCloseId] = useState<number | null>(null)

  const [totalPages, setTotalPages] = useState("")
  const [page, setPage] = useState("1")

  let queryParams: Record<string, string> = {}

  useEffect(() => {
    const fetchEnterprise = async () => {
      const response = await getEnterpriseById(id)
      if (response.status === HttpStatusCode.Forbidden) {
        navigate("/403")
      } else if (response.status === HttpStatusCode.InternalServerError) {
        navigate("/500")
      } else if (response.status === HttpStatusCode.Unauthorized) {
        navigate("/401")
      } else if (response.status === HttpStatusCode.Ok) {
        setEnterprise(response.data)
      } else {
        console.error("Error getting enterprise info:", response)
      }
      setEnterpriseLoading(false)
    }

    const fetchEnterpriseJobs = async (page: string) => {
      if (page) queryParams.page = page
      queryParams.onlyActive = "false"
      queryParams.pageSize = "3"

      const response = await getEnterpriseJobOffers(id, queryParams)

      if (response.status === HttpStatusCode.NoContent) {
        setJobs([])
      } else {
        setJobs(response.data)
        setTotalPages(response.headers["x-total-pages"] as string)
      }
      setJobsLoading(false)
    }

    const fetchUserJobs = async (page: string) => {
      if (page) queryParams.page = page
      queryParams.pageSize = "3"

      const response = await getUserJobs(id, queryParams)

      if (response.status === HttpStatusCode.NoContent) {
        setUserJobs([])
      } else {
        setUserJobs(response.data)
        setTotalPages(response.headers["x-total-pages"] as string)
      }
      setJobsLoading(false)
    }

    if (isEnterpriseLoading) {
      fetchEnterprise()
    }
    if (jobsLoading) {
      if (userInfo?.role === UserRole.ENTERPRISE) {
        fetchEnterpriseJobs(page)
      } else {
        fetchUserJobs(page)
      }
    }
  }, [
    getEnterpriseById,
    getEnterpriseJobOffers,
    getUserJobs,
    id,
    isEnterpriseLoading,
    jobsLoading,
    navigate,
    userInfo?.role,
    page,
    queryParams,
  ])

  const handleClose = async () => {
    const response = await closeJobOffer(jobOfferToCloseId, JobOfferAvailability.CLOSED)

    if (response.status === HttpStatusCode.Ok) {
      setJobsLoading(true)
      const modalElement = document.getElementById("cancelModal")
      modalElement?.classList.remove("show")
      document.body.classList.remove("modal-open")
      const modalBackdrop = document.querySelector(".modal-backdrop")
      if (modalBackdrop) {
        modalBackdrop.remove()
      }
    }
  }

  const handlePage = (pageNumber: string) => {
    setPage(pageNumber)
    setJobsLoading(true)
    navigate({
      search: createSearchParams({ page: pageNumber }).toString(),
    })
  }

  const enterprisesJobs = jobs.map((job) => {
    return (
      <JobOfferEnterpriseCard job={job} key={job.id} handleClose={handleClose} setJobOfferId={setJobOfferToCloseId} />
    )
  })

  const usersJobs = userJobs.map((job) => {
    return (
      <JobOfferEnterpriseCard job={job} key={job.id} handleClose={handleClose} setJobOfferId={setJobOfferToCloseId} />
    )
  })

  document.title = enterprise?.name + " | ClonedIn"

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container fluid style={{ background: "#F2F2F2", height: "800px" }}>
        <Row className="row" style={{ backgroundColor: "#f2f2f2" }}>
          <Col sm={3} className="col d-flex flex-column align-items-center">
            {isEnterpriseLoading ? (
              <div className="my-5">
                <Loader />
              </div>
            ) : (
              <ProfileEnterpriseCard enterprise={enterprise} />
            )}
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
            {userInfo?.role === UserRole.ENTERPRISE ? (
              enterprisesJobs.length > 0 ? (
                <>
                  <div className="w-100">{enterprisesJobs}</div>
                  <Pagination pages={totalPages} setter={handlePage} />
                </>
              ) : (
                <div style={{ fontWeight: "bold" }}>{t("No Job Offers")}</div>
              )
            ) : usersJobs.length > 0 ? (
              <>
                <div className="w-100">{usersJobs}</div>
                <Pagination pages={totalPages} setter={handlePage} />
              </>
            ) : (
              <div style={{ fontWeight: "bold" }}>{t("No Job Offers")}</div>
            )}
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default ProfileEnterprise
