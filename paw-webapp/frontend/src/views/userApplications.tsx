import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Button from "react-bootstrap/Button"
import Form from "react-bootstrap/Form"
import Loader from "../components/loader"
import Navigation from "../components/navbar"
import Pagination from "../components/pagination"
import JobOfferUserCard from "../components/cards/jobOfferUserCard"
import ContactDto from "../utils/ContactDto"
import { FilledBy, SortBy, JobOfferStatus } from "../utils/constants"
import { useState, useEffect, useCallback } from "react"
import { useTranslation } from "react-i18next"
import { useSharedAuth } from "../api/auth"
import { useRequestApi } from "../api/apiRequest"
import { useNavigate } from "react-router-dom"
import { HttpStatusCode } from "axios"
function ApplicationsUser() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { loading, apiRequest } = useRequestApi()
  const { userInfo } = useSharedAuth()

  const [isLoading, setLoading] = useState(true)
  const [applications, setApplications] = useState<ContactDto[]>([])

  const [filterStatus, setFilterStatus] = useState("")
  const [sortBy, setSortBy] = useState(SortBy.ANY.toString())

  const [jobOfferToAnswerId, setJobOfferToAnswerId] = useState<any>()

  document.title = t("Applications Page Title")

  // const [searchParams, setSearchParams] = useSearchParams()
  let queryParams: Record<string, string> = {}

  const fetchEnterpriseInfo = useCallback(
    async (enterpriseUrl: string) => {
      try {
        const response = await apiRequest({
          url: enterpriseUrl,
          method: "GET",
        })

        if (response.status === HttpStatusCode.Ok) {
          return response.data
        } else {
          console.error("Error fetching user info:", response)
          return null
        }
      } catch (error) {
        console.error("Error fetching user info:", error)
        return null
      }
    },
    [apiRequest],
  )

  const fetchJobOfferInfo = useCallback(
    async (jobOfferUrl: string) => {
      try {
        const response = await apiRequest({
          url: jobOfferUrl,
          method: "GET",
        })

        if (response.status === HttpStatusCode.Ok) {
          return response.data
        } else {
          console.error("Error fetching job offer info:", response)
          return null
        }
      } catch (error) {
        console.error("Error fetching job offer info:", error)
        return null
      }
    },
    [apiRequest],
  )

  const fetchCategoryInfo = useCallback(
    async (categoryUrl: string) => {
      try {
        const response = await apiRequest({
          url: categoryUrl,
          method: "GET",
        })

        if (response.status === HttpStatusCode.Ok) {
          return response.data
        } else {
          console.error("Error fetching category info:", response)
          return null
        }
      } catch (error) {
        console.error("Error fetching category info:", error)
        return null
      }
    },
    [apiRequest],
  )

  const fetchNotifications = useCallback(
    async (status: string, sortBy: string) => {
      setLoading(true)
      if (status) queryParams.status = status
      if (sortBy) queryParams.sortBy = sortBy

      try {
        const response = await apiRequest({
          url: `/users/${userInfo?.id}/notifications`,
          method: "GET",
          queryParams: queryParams,
        })

        if (response.status === HttpStatusCode.InternalServerError) {
          navigate("/403")
        }

        if (response.status === HttpStatusCode.NoContent) {
          setApplications([])
        } else {
          const contactsData = await Promise.all(
            response.data.map(async (contact: ContactDto) => {
              const enterpriseInfo = await fetchEnterpriseInfo(contact.links.enterprise)
              const userCategoryInfo = await fetchCategoryInfo(enterpriseInfo.links.category)

              const jobOfferInfo = await fetchJobOfferInfo(contact.links.jobOffer)
              const jobOfferCategoryInfo = await fetchCategoryInfo(jobOfferInfo.links.category)

              return {
                ...contact,
                enterpriseInfo: {
                  ...enterpriseInfo,
                  categoryInfo: userCategoryInfo,
                },
                jobOfferInfo: {
                  ...jobOfferInfo,
                  categoryInfo: jobOfferCategoryInfo,
                },
              }
            }),
          )
          setApplications(contactsData)
        }
      } catch (error) {
        console.error("Error fetching jobs:", error)
      }
      setLoading(false)
    },
    [apiRequest, queryParams, navigate, userInfo?.id, fetchJobOfferInfo, fetchEnterpriseInfo, fetchCategoryInfo],
  )

  useEffect(() => {
    if (isLoading) {
      // setSearchParams(queryParams)
      fetchNotifications(filterStatus, sortBy)
    }
  }, [fetchNotifications, isLoading, filterStatus, sortBy])

  const handleFilter = (status: string) => {
    setFilterStatus(status)
    setLoading(true)
  }

  const handleSort = (sortBy: string) => {
    setSortBy(sortBy.toString())
    setLoading(true)
  }

  const handleCancel = async () => {
    const queryParams: Record<string, string> = {}
    queryParams.status = JobOfferStatus.CANCELLED

    const response = await apiRequest({
      url: `/users/${userInfo?.id}/applications/${jobOfferToAnswerId}`,
      method: "PUT",
      queryParams: queryParams,
    })

    if (response.status === HttpStatusCode.NoContent) {
      setLoading(true)
      const modalElement = document.getElementById("cancelModal")
      modalElement?.classList.remove("show")
      document.body.classList.remove("modal-open")
      const modalBackdrop = document.querySelector(".modal-backdrop")
      if (modalBackdrop) {
        modalBackdrop.remove()
      }
    }
  }

  const userApplications = applications.map((application, index) => {
    return (
      <JobOfferUserCard
        contact={application}
        job={application.jobOfferInfo}
        handler={handleCancel}
        setJobOfferId={setJobOfferToAnswerId}
        applicationsView={false}
        key={index}
      />
    )
  })

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container fluid>
        <Row className="align-items-start d-flex">
          <Col sm={2} className="sidebar">
            <div className="d-flex flex-column justify-content-center">
              <div className="search mx-auto">
                <h5 className="ml-2 mt-2">{t("Filter by status")}:</h5>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button
                  variant="outline-light "
                  className="filterbtn"
                  disabled={filterStatus === JobOfferStatus.ACCEPTED}
                  onClick={() => handleFilter(JobOfferStatus.ACCEPTED)}
                >
                  {t("Accepted")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button
                  variant="outline-light "
                  className="filterbtn"
                  disabled={filterStatus === JobOfferStatus.DECLINED}
                  onClick={() => handleFilter(JobOfferStatus.DECLINED)}
                >
                  {t("Rejected")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button
                  variant="outline-light "
                  className="filterbtn"
                  disabled={filterStatus === JobOfferStatus.PENDING}
                  onClick={() => handleFilter(JobOfferStatus.PENDING)}
                >
                  {t("Pending")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button
                  variant="outline-light "
                  className="filterbtn"
                  disabled={filterStatus === JobOfferStatus.CANCELLED}
                  onClick={() => handleFilter(JobOfferStatus.CANCELLED)}
                >
                  {t("Cancelled")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-4 mx-auto" style={{ maxWidth: "fit-content" }}>
                <Button variant="outline-light " className="filterbtn" onClick={() => handleFilter("")}>
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
                  <Form.Select
                    className="px-3"
                    aria-label="Sort by select"
                    value={sortBy}
                    onChange={(e) => handleSort(e.target.value)}
                  >
                    <option value={SortBy.ANY}> {t("Order By")} </option>
                    <option value={SortBy.JOB_OFFER_POSITION}> {t("Job Offer")} </option>
                    <option value={SortBy.USERNAME}> {t("Name")} </option>
                    <option value={SortBy.DATE_ASC}> {t("Date asc")} </option>
                    <option value={SortBy.DATE_DESC}> {t("Date desc")} </option>
                  </Form.Select>
                </div>
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
                {isLoading ? (
                  <div className="my-5">
                    <Loader />
                  </div>
                ) : userApplications.length > 0 ? (
                  userApplications
                ) : (
                  <div className="my-5 w-100">
                    <h5>{t("No job offers found")}</h5>
                  </div>
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
