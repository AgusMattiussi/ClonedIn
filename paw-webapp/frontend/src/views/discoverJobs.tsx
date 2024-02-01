import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import JobOfferDiscoverCard from "../components/cards/jobOfferDiscoverCard"
import Button from "react-bootstrap/Button"
import Form from "react-bootstrap/Form"
import * as Icon from "react-bootstrap-icons"
import Pagination from "../components/pagination"
import Loader from "../components/loader"
import { createSearchParams, useNavigate, useSearchParams } from "react-router-dom"
import { useState, useEffect, useCallback } from "react"
import { useTranslation } from "react-i18next"
import { useSharedAuth } from "../api/auth"
import { useGetCategories } from "../hooks/useGetCategories"
import { useGetJobOfferData } from "../hooks/useGetJobOfferData"
import { HttpStatusCode } from "axios"
import { SortBy } from "../utils/constants"

function DiscoverJobs() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()

  const { getCategories } = useGetCategories()
  const { getJobOffers } = useGetJobOfferData()

  const [isLoading, setLoading] = useState(true)
  const [jobs, setJobs] = useState<any[]>([])

  const [searchTerm, setSearchTerm] = useState("")
  const [totalPages, setTotalPages] = useState("")
  const [page, setPage] = useState("1")

  const [categoryList, setCategoryList] = useState([])
  const [categoryName, setCategoryName] = useState("")

  const [modality, setModality] = useState("")

  const [minSalary, setMinSalary] = useState("")
  const [maxSalary, setMaxSalary] = useState("")

  document.title = t("Discover Jobs") + " | ClonedIn"

  const [searchParams, setSearchParams] = useSearchParams()
  const [sortBy, setSortBy] = useState(SortBy.DEFAULT.toString())
  let queryParams: Record<string, string> = {}

  const fetchJobs = useCallback(
    async (categoryName: string, modality: string, searchTerm: string, minSalary: string, maxSalary: string, page: string, sortBy: string,) => {
      setLoading(true)

      if (categoryName) queryParams.categoryName = categoryName
      if (modality) queryParams.modality = modality
      if (searchTerm) queryParams.searchTerm = searchTerm
      if (minSalary) queryParams.minSalary = minSalary
      if (maxSalary) queryParams.maxSalary = maxSalary
      if (page) queryParams.page = page
      if (sortBy) queryParams.sortBy = sortBy

      try {
        const response = await getJobOffers(queryParams)

        if (response.status === HttpStatusCode.InternalServerError) {
          navigate("/403")
        }

        if (response.status === HttpStatusCode.NoContent) {
          setJobs([])
        } else {
          setJobs(response.data)
          setTotalPages(response.headers["x-total-pages"] as string)
        }
        navigate({  
          search: createSearchParams({ 
            page: page,
            categoryName: categoryName,
            modality: modality,
            searchTerm: searchTerm,
            minSalary: minSalary,
            maxSalary: maxSalary,
            sortBy: sortBy
          }).toString() 
        });
        setPage("1")
      } catch (error) {
        console.error("Error fetching jobs:", error)
      }
      setLoading(false)
    },
    [getJobOffers, queryParams, navigate],
  )

  useEffect(() => {
    const fetchCategories = async () => {
      const response = await getCategories()
      setCategoryList(response.data)
    }

    if (categoryList.length === 0) {
      fetchCategories()
    }
  }, [getCategories, categoryList.length])

  useEffect(() => {
    if (isLoading) {
      fetchJobs(categoryName, modality, searchTerm, minSalary, maxSalary, page, sortBy)
    }
  }, [categoryName, modality, searchTerm, minSalary, maxSalary, isLoading, fetchJobs, setSearchParams, queryParams])

  const handleSearch = () => {
    console.log("Search")
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
    setLoading(true)
  }

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
                  <option value="Remoto">{t("Remoto")}</option>
                  <option value="Presencial">{t("Presencial")}</option>
                  <option value="Mixto">{t("Mixto")}</option>
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
          <Col className="d-flex flex-column mt-2 mr-2 mb-2">
            <Row className="my-2">
            <div className="d-flex justify-content-between">
                <h3 style={{ textAlign: "left" }}>{t("Discover Jobs")}</h3>
                <div style={{ width: "200px" }}>
                  <Form.Select
                    className="px-3"
                    aria-label="Sort by select"
                    value={sortBy}
                    onChange={(e) => handleSort(e.target.value)}
                  >
                    <option value={SortBy.DEFAULT}> {t("Order By")} </option>
                    <option value={SortBy.SALARY_DESC}> {t("Higer Salary")} </option>
                    <option value={SortBy.SALARY_ASC}> {t("Lower Salary")} </option>
                    <option value={SortBy.OLDEST}> {t("Date asc")} </option>
                    <option value={SortBy.RECENT}> {t("Date desc")} </option>
                  </Form.Select>
                </div>
              </div>
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
                <Pagination pages={totalPages} setter={handlePage}/>
              </Container>
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default DiscoverJobs
