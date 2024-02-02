import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import Button from "react-bootstrap/Button"
import Form from "react-bootstrap/Form"
import { MDBTable, MDBTableHead, MDBTableBody } from "mdb-react-ui-kit"
import ContactDto from "../utils/ContactDto"
import Pagination from "../components/pagination"
import CancelModal from "../components/modals/cancelModal"
import Loader from "../components/loader"
import { JobOfferStatus, SortBy, FilledBy } from "../utils/constants"
import { HttpStatusCode } from "axios"
import { useTranslation } from "react-i18next"
import { useSharedAuth } from "../api/auth"
import { useGetCategories } from "../hooks/useGetCategories"
import { useGetUserData } from "../hooks/useGetUserData"
import { useGetEnterpriseData } from "../hooks/useGetEnterpriseData"
import { usePutEnterpriseData } from "../hooks/usePutEnterpriseData"
import { useGetJobOfferData } from "../hooks/useGetJobOfferData"
import { useNavigate, Link, createSearchParams } from "react-router-dom"
import { useState, useEffect, useCallback } from "react"

function EnterpriseContacts() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()

  const { getCategoryByUrl } = useGetCategories()
  const { getUserByUrl } = useGetUserData()
  const { getEnterpriseContacts } = useGetEnterpriseData()
  const { answerEnterpriseContact } = usePutEnterpriseData()
  const { getJobOfferByUrl } = useGetJobOfferData()

  const [isLoading, setLoading] = useState(true)
  const [contacts, setContacts] = useState<ContactDto[]>([])

  const [filterStatus, setFilterStatus] = useState("")
  const [sortBy, setSortBy] = useState(SortBy.ANY.toString())
  const [filledBy] = useState(FilledBy.ENTERPRISE.toString())
  const [totalPages, setTotalPages] = useState("")
  const [page, setPage] = useState("1")

  const [jobOfferToCancelId, setJobOfferToCancelId] = useState<any>()
  const [userToCancelId, setUserToCancelId] = useState<any>()

  document.title = t("My Recruits Page Title")

  let queryParams: Record<string, string> = {}

  const fetchUserInfo = useCallback(
    async (userUrl: string) => {
      try {
        const response = await getUserByUrl(userUrl)

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
    [getUserByUrl],
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

  const fetchContacts = useCallback(
    async (status: string, sortBy: string, filledBy: string, page: string) => {
      setLoading(true)
      if (status) queryParams.status = status
      if (sortBy) queryParams.sortBy = sortBy
      if (filledBy) queryParams.filledBy = filledBy
      if (page) queryParams.page = page

      try {
        const response = await getEnterpriseContacts(userInfo?.id, queryParams)

        if (response.status === HttpStatusCode.InternalServerError) {
          navigate("/403")
        }

        if (response.status === HttpStatusCode.NoContent) {
          setContacts([])
        } else {
          const contactsData = await Promise.all(
            response.data.map(async (contact: ContactDto) => {
              const userInfo = await fetchUserInfo(contact.links.user)
              const userCategoryInfo = await fetchCategoryInfo(userInfo.links.category)

              const jobOfferInfo = await fetchJobOfferInfo(contact.links.jobOffer)
              const jobOfferCategoryInfo = await fetchCategoryInfo(jobOfferInfo.links.category)

              return {
                ...contact,
                userInfo: {
                  ...userInfo,
                  categoryInfo: userCategoryInfo,
                },
                jobOfferInfo: {
                  ...jobOfferInfo,
                  categoryInfo: jobOfferCategoryInfo,
                },
              }
            }),
          )
          setContacts(contactsData)
          setTotalPages(response.headers["x-total-pages"] as string)
        }
        navigate({
          search: createSearchParams({
            page: page,
            status: status,
            filledBy: filledBy,
            sortBy: sortBy,
          }).toString(),
        })
        setPage("1")
      } catch (error) {
        console.error("Error fetching jobs:", error)
      }
      setLoading(false)
    },
    [getEnterpriseContacts, queryParams, navigate, userInfo?.id, fetchJobOfferInfo, fetchUserInfo, fetchCategoryInfo],
  )

  useEffect(() => {
    if (isLoading) {
      fetchContacts(filterStatus, sortBy, filledBy, page)
    }
  }, [fetchContacts, isLoading, filterStatus, sortBy, filledBy, page])

  const handleFilter = (status: string) => {
    setFilterStatus(status)
    setLoading(true)
  }

  const handlePage = (pageNumber: string) => {
    console.log("Page")
    setPage(pageNumber)
    setLoading(true)
  }

  const handleSort = (sortBy: string) => {
    setSortBy(sortBy.toString())
    setLoading(true)
  }

  const setParams = (jobOfferId: number, userId: number) => {
    setJobOfferToCancelId(jobOfferId)
    setUserToCancelId(userId)
  }

  const handleCancel = async () => {
    const queryParams: Record<string, string> = {}
    queryParams.status = JobOfferStatus.CANCELLED

    const response = await answerEnterpriseContact(userInfo?.id, jobOfferToCancelId, userToCancelId, queryParams)

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

  const contactsList = contacts.map((contact, index) => {
    return (
      <tr key={index}>
        <td>
          <Link to={`/jobOffers/${contact.jobOfferInfo?.id}`} style={{ textDecoration: "none" }}>
            {contact.jobOfferInfo?.position}
          </Link>
        </td>
        <td>
          {contact.jobOfferInfo?.categoryInfo.name == "No-Especificado"
            ? t("No especificado")
            : t(contact.jobOfferInfo!.categoryInfo.name)}
        </td>
        <td>
          <Link to={`/users/${contact.userInfo?.id}`} style={{ textDecoration: "none" }}>
            {contact.userInfo?.name}
          </Link>
        </td>
        <td>
          {contact.userInfo?.categoryInfo.name == "No-Especificado"
            ? t("No especificado")
            : t(contact.userInfo!.categoryInfo.name)}
        </td>
        <td>{contact.date}</td>
        <td>
          {contact.status === JobOfferStatus.PENDING ? (
            <Button
              variant="danger"
              style={{ minWidth: "90px", marginBottom: "5px" }}
              data-bs-toggle="modal"
              data-bs-target="#cancelModal"
              onClick={() => setParams(contact.jobOfferInfo?.id, contact.userInfo?.id)}
            >
              {t("Cancel")}
            </Button>
          ) : (
            t(contact.status)
          )}
        </td>
        <td />
      </tr>
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
              <div className="d-flex justify-content-between">
                <h3 style={{ textAlign: "left" }}>{t("My Recruits")}</h3>
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
            <Row className="m-2">
              <MDBTable
                className="table-light"
                align="middle"
                style={{ boxShadow: "0 0 2px rgba(0,0,0,0.16), 0 0 1px rgba(0,0,0,0.23)" }}
              >
                <MDBTableHead>
                  <tr>
                    <th scope="col">{t("Job Offer")}</th>
                    <th scope="col">{t("Job Category")}</th>
                    <th scope="col">{t("Name")}</th>
                    <th scope="col">{t("Category")}</th>
                    <th scope="col">{t("Date")}</th>
                    <th scope="col">{t("Status")}</th>
                    <th />
                  </tr>
                </MDBTableHead>
                <MDBTableBody>
                  {isLoading ? (
                    <Loader />
                  ) : contactsList.length > 0 ? (
                    contactsList
                  ) : (
                    <tr>
                      <td>{t("No Contacts")}</td>
                      <td />
                      <td />
                      <td />
                      <td />
                      <td />
                      <td />
                    </tr>
                  )}
                  {}
                </MDBTableBody>
              </MDBTable>
              <Pagination pages={totalPages} setter={handlePage} />
            </Row>
          </Col>
        </Row>
      </Container>
      <CancelModal
        title={t("Modal Title")}
        msg={t("Cancel JobOffer Modal Msg")}
        cancel={t("Cancel")}
        confirm={t("Confirm")}
        onConfirmClick={handleCancel}
      />
    </div>
  )
}

export default EnterpriseContacts
