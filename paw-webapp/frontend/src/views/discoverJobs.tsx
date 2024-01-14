import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import JobOfferDiscoverCard from "../components/cards/jobOfferDiscoverCard"
import Button from "react-bootstrap/Button"
import Form from "react-bootstrap/Form"
import * as Icon from "react-bootstrap-icons"
import { useNavigate, useSearchParams } from "react-router-dom"
import { useState, useEffect, useCallback } from "react"
import { useRequestApi } from "../api/apiRequest"
import { useTranslation } from "react-i18next"
import { useSharedAuth } from "../api/auth"
import { HttpStatusCode } from "axios"
import Pagination from "../components/pagination"
import Loader from "../components/loader"

function DiscoverJobs() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { loading, apiRequest } = useRequestApi()
  const { userInfo } = useSharedAuth()

  const [isLoading, setLoading] = useState(true)
  const [jobs, setJobs] = useState<any[]>([])

  const [searchTerm, setSearchTerm] = useState("")

  const [categoryList, setCategoryList] = useState([])
  const [categoryName, setCategoryName] = useState("")

  const [modality, setModality] = useState("")

  const [minSalary, setMinSalary] = useState("")
  const [maxSalary, setMaxSalary] = useState("")

  document.title = t("Discover Jobs") + " | ClonedIn"

  const [searchParams, setSearchParams] = useSearchParams()
  let queryParams: Record<string, string> = {}

  const fetchJobs = useCallback(
    async (categoryName: string, modality: string, searchTerm: string, minSalary: string, maxSalary: string) => {
      setLoading(true)
      if (categoryName) queryParams.categoryName = categoryName
      if (modality) queryParams.modality = modality
      if (searchTerm) queryParams.searchTerm = searchTerm
      if (minSalary) queryParams.minSalary = minSalary
      if (maxSalary) queryParams.maxSalary = maxSalary

      try {
        const response = await apiRequest({
          url: "/jobOffers",
          method: "GET",
          queryParams: queryParams,
        })

        if (response.status === HttpStatusCode.InternalServerError) {
          navigate("/403")
        }

        if (response.status === HttpStatusCode.NoContent) {
          setJobs([])
        } else {
          setJobs(response.data)
        }
      } catch (error) {
        console.error("Error fetching jobs:", error)
      }
      setLoading(false)
    },
    [apiRequest, queryParams, navigate],
  )

  const handleSearch = () => {
    console.log("Search")
    setLoading(true)
  }

  const handleFilter = () => {
    console.log("Filter")
    setLoading(true)
  }

  const handleClear = () => {
    setSearchTerm("")
    setCategoryName("")
    setModality("")
    setMinSalary("")
    setMaxSalary("")
  }

  useEffect(() => {
    const fetchCategories = async () => {
      const response = await apiRequest({
        url: "/categories",
        method: "GET",
      })
      setCategoryList(response.data)
    }

    if (categoryList.length === 0) {
      fetchCategories()
    }
  }, [apiRequest, categoryList.length])

  useEffect(() => {
    if (isLoading) {
      // setSearchParams(queryParams)
      fetchJobs(categoryName, modality, searchTerm, minSalary, maxSalary)
    }
  }, [categoryName, modality, searchTerm, minSalary, maxSalary, isLoading, fetchJobs])

  const jobsList = jobs.map((job) => {
    return <JobOfferDiscoverCard job={job} key={job.id} />
  })

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container fluid>
        <Row className="align-items-start d-flex">
          <Col sm={2} className="sidebar">
            <Row className="search">
              <h5 className="ml-2 mt-2">{t("Search By")}</h5>
            </Row>
            <Row>
              <Form className="search">
                <Form.Control
                  type="search"
                  placeholder={t("Search Job Offer Placeholder").toString()}
                  className="me-2"
                  aria-label="Search"
                  onChange={(e) => setSearchTerm(e.target.value)}
                  value={searchTerm}
                />
                <div className="d-flex flex-wrap justify-content-center mt-2">
                  <Button variant="outline-light" className="filterbtn" onClick={() => handleSearch()}>
                    <Icon.Search size={15} />
                  </Button>
                </div>
              </Form>
            </Row>
            <br />
            <Row className="search">
              <h5>{t("Filter By")}</h5>
            </Row>
            <Row>
              <div>
                <Form.Select
                  className="px-3"
                  aria-label="Categories select"
                  value={categoryName}
                  onChange={(e) => setCategoryName(e.target.value)}
                >
                  <option key="1" value="No-Especificado">
                    {t("Category")}
                  </option>
                  {categoryList.map((categoryListItem: any) => (
                    <option key={categoryListItem.id} value={categoryListItem.name}>
                      {t(categoryListItem.name)}
                    </option>
                  ))}
                </Form.Select>
              </div>
            </Row>
            <br />
            <Row>
              <div>
                <Form.Select
                  className="px-3"
                  aria-label="Categories select"
                  value={modality}
                  onChange={(e) => setModality(e.target.value)}
                >
                  <option key="1" value="">
                    {t("Modality")}
                  </option>
                  <option value="Remoto">{t("Home Office")}</option>
                  <option value="Presencial">{t("On Site")}</option>
                  <option value="Mixto">{t("Mixed")}</option>
                </Form.Select>
              </div>
            </Row>
            <Row className="search mt-2">
              <div className="d-flex justify-content-center">
                <h6>{t("Salary")}</h6>
              </div>
            </Row>
            <Row className="search">
              <div className="d-flex">
                <Form.Control
                  type="number"
                  min="0"
                  step="1"
                  placeholder={t("Minimum").toString()}
                  className="me-2"
                  aria-label="Search"
                  onChange={(e) => setMinSalary(e.target.value)}
                  value={minSalary}
                />
                -
                <Form.Control
                  type="number"
                  min="0"
                  step="1"
                  placeholder={t("Maximum").toString()}
                  className="ms-2"
                  aria-label="Search"
                  onChange={(e) => setMaxSalary(e.target.value)}
                  value={maxSalary}
                />
              </div>
              <br />
              <div className="d-flex flex-wrap justify-content-center mt-2">
                <Button variant="outline-light " className="filterbtn" onClick={() => handleFilter()}>
                  {t("Filter")}
                </Button>
              </div>
            </Row>
            <br />
            <Row>
              <div className="d-flex flex-wrap justify-content-center">
                <Button variant="outline-light " className="filterbtn" onClick={() => handleClear()}>
                  {t("Clear Filter")}
                </Button>
              </div>
            </Row>
          </Col>
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
                ) : jobsList.length === 0 ? (
                  <div className="my-5 w-100">
                    <h5>{t("No job offers found")}</h5>
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
