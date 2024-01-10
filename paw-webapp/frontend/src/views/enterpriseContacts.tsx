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
import { JobOfferStatus } from "../utils/constants"
import { HttpStatusCode } from "axios"
import { useTranslation } from "react-i18next"
import { useSharedAuth } from "../api/auth"
import { useRequestApi } from "../api/apiRequest"
import { useNavigate, useSearchParams, Link } from "react-router-dom"
import { useState, useEffect, useCallback } from "react"

function EnterpriseContacts() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { loading, apiRequest } = useRequestApi()
  const { userInfo } = useSharedAuth()

  const [isLoading, setLoading] = useState(true)
  const [contacts, setContacts] = useState<ContactDto[]>([])

  const [status, setStatus] = useState("")
  const [sortBy, setSortBy] = useState("")

  document.title = t("My Recruits Page Title")

  // const [searchParams, setSearchParams] = useSearchParams()
  let queryParams: Record<string, string> = {}

  const fetchUserInfo = useCallback(
    async (userUrl: string) => {
      try {
        const response = await apiRequest({
          url: userUrl,
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

  const fetchContacts = useCallback(
    async (status: string, sortBy: string) => {
      setLoading(true)
      if (status) queryParams.status = status
      if (sortBy) queryParams.sortBy = sortBy

      try {
        const response = await apiRequest({
          url: `/enterprises/${userInfo?.id}/contacts`,
          method: "GET",
          queryParams: queryParams,
        })

        if (response.status === 500) {
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
        }
      } catch (error) {
        console.error("Error fetching jobs:", error)
      }
      setLoading(false)
    },
    [apiRequest, queryParams, navigate, userInfo?.id, fetchJobOfferInfo, fetchUserInfo, fetchCategoryInfo],
  )

  useEffect(() => {
    if (isLoading) {
      // setSearchParams(queryParams)
      fetchContacts(status, sortBy)
    }
  }, [fetchContacts, isLoading, status, sortBy])

  const contactsList = contacts.map((contact, index) => {
    return (
      <tr key={index}>
        <td>
          <Link to={`/jobOffers/${contact.jobOfferInfo?.id}`} style={{ textDecoration: "none" }}>
            {contact.jobOfferInfo?.position}
          </Link>
        </td>
        <td>{t(contact.jobOfferInfo?.categoryInfo.name)}</td>
        <td>
          <Link to={`/users/${contact.userInfo?.id}`} style={{ textDecoration: "none" }}>
            {contact.userInfo?.name}
          </Link>
        </td>
        <td>{t(contact.userInfo?.categoryInfo.name)}</td>
        <td>{contact.date}</td>
        <td>
          {contact.status === JobOfferStatus.PENDING ? (
            <Button
              variant="danger"
              style={{ minWidth: "90px", marginBottom: "5px" }}
              data-bs-toggle="modal"
              data-bs-target="#cancelModal"
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
                <Button variant="outline-light " className="filterbtn">
                  {t("Accepted")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button variant="outline-light " className="filterbtn">
                  {t("Rejected")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button variant="outline-light " className="filterbtn">
                  {t("Pending")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
                <Button variant="outline-light " className="filterbtn">
                  {t("Cancelled")}
                </Button>
              </div>
              <div className="d-flex flex-wrap justify-content-center mt-4 mx-auto" style={{ maxWidth: "fit-content" }}>
                <Button variant="outline-light " className="filterbtn">
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
                    onChange={(e) => setSortBy(e.target.value)}
                  >
                    <option value="0"> {t("Order By")} </option>
                    <option value="1"> {t("Job Offer")} </option>
                    <option value="2"> {t("Name")} </option>
                    <option value="4"> {t("Date asc")} </option>
                    <option value="5"> {t("Date desc")} </option>
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
              <Pagination />
            </Row>
          </Col>
        </Row>
      </Container>
      <CancelModal
        title={t("Modal Title")}
        msg={t("Cancel JobOffer Modal Msg")}
        cancel={t("Cancel")}
        confirm={t("Confirm")}
      />
    </div>
  )
}

export default EnterpriseContacts
