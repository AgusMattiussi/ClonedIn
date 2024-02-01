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
import { usePutUserData } from "../hooks/usePutUserData"
import { useGetEnterpriseData } from "../hooks/useGetEnterpriseData"
import { useGetJobOfferData } from "../hooks/useGetJobOfferData"
import { useGetCategories } from "../hooks/useGetCategories"
import { useGetUserData } from "../hooks/useGetUserData"
import { createSearchParams, useNavigate } from "react-router-dom"
import { HttpStatusCode } from "axios"

function NotificationsUser() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()

  const { getUserContacts } = useGetUserData()
  const { getCategoryByUrl } = useGetCategories()
  const { getEnterpriseByUrl } = useGetEnterpriseData()
  const { getJobOfferByUrl } = useGetJobOfferData()
  const { answerUserContact } = usePutUserData()

  const [isLoading, setLoading] = useState(true)
  const [notifications, setNotifications] = useState<ContactDto[]>([])

  const [filterStatus, setFilterStatus] = useState("")
  const [sortBy, setSortBy] = useState(SortBy.ANY.toString())
  const [filledBy] = useState(FilledBy.ENTERPRISE.toString())

  const [jobOfferToAnswerId, setJobOfferToAnswerId] = useState<any>()
  const [totalPages, setTotalPages] = useState("")
  const [page, setPage] = useState("1")

  document.title = t("Notifications Page Title")

  let queryParams: Record<string, string> = {}

  const fetchEnterpriseInfo = useCallback(
    async (enterpriseUrl: string) => {
      try {
        const response = await getEnterpriseByUrl(enterpriseUrl)
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
    [getEnterpriseByUrl],
  )

  const fetchJobOfferInfo = useCallback(
    async (jobOfferUrl: string) => {
      try {
        const response = await getJobOfferByUrl(jobOfferUrl)
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
    [getJobOfferByUrl],
  )

  const fetchCategoryInfo = useCallback(
    async (categoryUrl: string) => {
      try {
        const response = await getCategoryByUrl(categoryUrl)
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
    [getCategoryByUrl],
  )

  const fetchNotifications = useCallback(
    async (status: string, sortBy: string, filledBy: string, page:string) => {
      setLoading(true)
      if (status) queryParams.status = status
      if (sortBy) queryParams.sortBy = sortBy
      if (filledBy) queryParams.filledBy = filledBy
      if (page) queryParams.page = page

      try {
        const response = await getUserContacts(userInfo?.id, queryParams)

        if (response.status === HttpStatusCode.InternalServerError) {
          navigate("/403")
        }

        if (response.status === HttpStatusCode.NoContent) {
          setNotifications([])
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
          setNotifications(contactsData)
          setTotalPages(response.headers["x-total-pages"] as string)
        }
        navigate({  
          search: createSearchParams({ 
            page: page,
            status: status,
            filledBy: filledBy,
            sortBy: sortBy
          }).toString() 
        });
        setPage("1")
      } catch (error) {
        console.error("Error fetching jobs:", error)
      }
      setLoading(false)
    },
    [getUserContacts, queryParams, navigate, userInfo?.id, fetchJobOfferInfo, fetchEnterpriseInfo, fetchCategoryInfo],
  )

  useEffect(() => {
    if (isLoading) {
      fetchNotifications(filterStatus, sortBy, filledBy, page)
    }
  }, [fetchNotifications, isLoading, filterStatus, sortBy, filledBy])

  const handleFilter = (status: string) => {
    setFilterStatus(status)
    setLoading(true)
  }

  const handleSort = (sortBy: string) => {
    setSortBy(sortBy.toString())
    setLoading(true)
  }

  const handlePage = (pageNumber: string) => {
    console.log("Page")
    setPage(pageNumber)
    setLoading(true)
  }

  const handleAnswer = async (answer: string) => {
    const queryParams: Record<string, string> = {}
    queryParams.status = answer === "Accept" ? JobOfferStatus.ACCEPTED : JobOfferStatus.DECLINED

    const response = await answerUserContact(userInfo?.id, jobOfferToAnswerId, queryParams)

    if (response.status === HttpStatusCode.NoContent) {
      setLoading(true)
      const modalElement = document.getElementById(answer === "Accept" ? "acceptModal" : "rejectModal")
      modalElement?.classList.remove("show")
      document.body.classList.remove("modal-open")
      const modalBackdrop = document.querySelector(".modal-backdrop")
      if (modalBackdrop) {
        modalBackdrop.remove()
      }
    }
  }

  const userNotifications = notifications.map((notification, index) => {
    return (
      <JobOfferUserCard
        contact={notification}
        job={notification.jobOfferInfo}
        handler={handleAnswer}
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
                <h3>{t("Job Offers")}</h3>
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
                ) : userNotifications.length > 0 ? (
                  userNotifications
                ) : (
                  <div className="my-5 w-100">
                    <h5>{t("No job offers found")}</h5>
                  </div>
                )}
                <Pagination pages={totalPages} setter={handlePage}/>
              </Container>
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default NotificationsUser
