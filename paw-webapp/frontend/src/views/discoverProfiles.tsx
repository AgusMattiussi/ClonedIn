import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import ProfileUserCard from "../components/cards/profileUserCard"
import Button from "react-bootstrap/Button"
import Form from "react-bootstrap/Form"
import * as Icon from "react-bootstrap-icons"
import Loader from "../components/loader"
import Pagination from "../components/pagination"
import { createSearchParams, useNavigate, Link } from "react-router-dom"
import { useState, useEffect, useCallback } from "react"
import { useTranslation } from "react-i18next"
import { useGetCategories } from "../hooks/useGetCategories"
import { useGetUsers } from "../hooks/useGetUsers"
import { useSharedAuth } from "../api/auth"
import { HttpStatusCode } from "axios"
import { SortBy } from "../utils/constants"

function DiscoverProfiles() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()

  const { getUsers } = useGetUsers()
  const { getCategories } = useGetCategories()

  const [isLoading, setLoading] = useState(true)
  const [users, setUsers] = useState<any[]>([])

  const [searchTerm, setSearchTerm] = useState("")

  const [categoryList, setCategoryList] = useState([])
  const [categoryName, setCategoryName] = useState("")

  const [educationLevel, setEducationLevel] = useState("")
  const [totalPages, setTotalPages] = useState("")
  const [page, setPage] = useState("1")

  const [minExpYears, setMinExpYears] = useState("")
  const [maxExpYears, setMaxExpYears] = useState("")

  document.title = t("Discover Profiles") + " | ClonedIn"

  const [sortBy, setSortBy] = useState(SortBy.DEFAULT.toString())

  const fetchUsers = useCallback(
    async (
      categoryName: string,
      educationLevel: string,
      searchTerm: string,
      minExpYears: string,
      maxExpYears: string,
      page: string,
      sortBy: string,
    ) => {
      setLoading(true)

      const queryParams: Record<string, string> = {}

      if (categoryName) queryParams.categoryName = categoryName
      if (educationLevel) queryParams.educationLevel = educationLevel
      if (searchTerm) queryParams.searchTerm = searchTerm
      if (minExpYears) queryParams.minExpYears = minExpYears
      if (maxExpYears) queryParams.maxExpYears = maxExpYears
      if (page) queryParams.page = page
      if (sortBy) queryParams.sortBy = sortBy

      try {
        const response = await getUsers(queryParams)
        if (response.status === HttpStatusCode.Forbidden) {
          navigate("/403")
        } else if (response.status === HttpStatusCode.InternalServerError) {
          navigate("/500")
        } else if (response.status === HttpStatusCode.NoContent) {
          setUsers([])
        } else {
          setUsers(response.data)
          setTotalPages(response.headers["x-total-pages"] as string)
        }
        navigate({
          search: createSearchParams({
            page: page,
            categoryName: categoryName,
            educationLevel: educationLevel,
            searchTerm: searchTerm,
            minExpYears: minExpYears,
            maxExpYears: maxExpYears,
            sortBy: sortBy,
          }).toString(),
        })
        setPage("1")
      } catch (error) {
        console.error("Error fetching users:", error)
      }
      setLoading(false)
    },
    [getUsers, navigate],
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
      fetchUsers(categoryName, educationLevel, searchTerm, minExpYears, maxExpYears, page, sortBy)
    }
  }, [categoryName, educationLevel, searchTerm, minExpYears, maxExpYears, isLoading, fetchUsers])

  const handleSearch = () => {
    setLoading(true)
  }

  const handlePage = (pageNumber: string) => {
    setPage(pageNumber)
    setLoading(true)
  }

  const handleSort = (sortBy: string) => {
    setSortBy(sortBy.toString())
    setLoading(true)
  }

  const handleFilter = () => {
    setLoading(true)
  }

  const handleClear = () => {
    setSearchTerm("")
    setCategoryName("")
    setEducationLevel("")
    setMinExpYears("")
    setMaxExpYears("")
    setLoading(true)
  }

  const usersList = users.map((user) => {
    return (
      <Link to={`/users/${user.id}`} style={{ textDecoration: "none", color: "black" }} key={user.id}>
        <ProfileUserCard user={user} />
      </Link>
    )
  })

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container fluid>
        <Row className="align-items-start d-flex vh-100">
          <Col sm={2} className="sidebar">
            <Row className="search">
              <h5 className="ml-2 mt-2">{t("Search By")}</h5>
            </Row>
            <Row>
              <Form className="search">
                <Form.Control
                  type="search"
                  placeholder={t("Search Profile Placeholder").toString()}
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
            <Form className="filter">
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
                    aria-label="Education Level select"
                    value={educationLevel}
                    onChange={(e) => setEducationLevel(e.target.value)}
                  >
                    <option key="1" value="">
                      {t("Education Level")}
                    </option>
                    <option value="Primario"> {t("Primario")} </option>
                    <option value="Secundario"> {t("Secundario")} </option>
                    <option value="Terciario"> {t("Terciario")} </option>
                    <option value="Graduado"> {t("Graduado")} </option>
                    <option value="Postgrado"> {t("Postgrado")} </option>
                  </Form.Select>
                </div>
              </Row>
              <Row className="mt-2">
                <div className="d-flex justify-content-center" style={{ color: "white" }}>
                  <h6>{t("Years Of Experience")}</h6>
                </div>
              </Row>
              <Row>
                <div className="d-flex" style={{ color: "white" }}>
                  <Form.Control
                    type="number"
                    min="0"
                    step="1"
                    placeholder={t("Minimum").toString()}
                    className="me-2"
                    aria-label="Search"
                    onChange={(e) => setMinExpYears(e.target.value)}
                    value={minExpYears}
                  />
                  -
                  <Form.Control
                    type="number"
                    min="0"
                    step="1"
                    placeholder={t("Maximum").toString()}
                    className="ms-2"
                    aria-label="Search"
                    onChange={(e) => setMaxExpYears(e.target.value)}
                    value={maxExpYears}
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
            </Form>
            <Row>
              <div className="d-flex flex-wrap justify-content-center" style={{ marginBottom: "10px" }}>
                <Button variant="outline-light " className="filterbtn" onClick={() => handleClear()}>
                  {t("Clear Filter")}
                </Button>
              </div>
            </Row>
          </Col>
          <Col className="d-flex flex-column mt-2 mr-2 mb-2">
            <Row className="my-2">
              <div className="d-flex justify-content-between">
                <h3 style={{ textAlign: "left" }}>{t("Discover Profiles")}</h3>
                <div style={{ width: "200px" }}>
                  <Form.Select
                    className="px-3"
                    aria-label="Sort by select"
                    value={sortBy}
                    onChange={(e) => handleSort(e.target.value)}
                  >
                    <option value={SortBy.DEFAULT}> {t("Order By")} </option>
                    <option value={SortBy.OLDEST}> {t("Date asc")} </option>
                    <option value={SortBy.RECENT}> {t("Date desc")} </option>
                    <option value={SortBy.EDUCATION_DESC}> {t("Higer Education")} </option>
                    <option value={SortBy.EDUCATION_ASC}> {t("Lower Education")} </option>
                    <option value={SortBy.EXP_DESC}> {t("Higer Experience")} </option>
                    <option value={SortBy.EXP_ASC}> {t("Lower Experience")} </option>
                  </Form.Select>
                </div>
              </div>
            </Row>
            <Row
              className="rounded-3 d-flex flex-row flex-wrap w-100"
              style={{
                marginLeft: "0px",
                marginRight: "3px",
              }}
            >
              <Container
                className="p-2 rounded-3 d-flex flex-wrap w-100 justify-content-center"
                style={{
                  background: "#F2F2F2",
                  boxShadow: "0 0 10px rgba(0,0,0,0.16), 0 0 4px rgba(0,0,0,0.23)",
                  minWidth: "100vh",
                  flexDirection: "column",
                }}
              >
                <div className="d-flex flex-wrap w-100 justify-content-center">
                  {isLoading ? (
                    <div className="my-5">
                      <Loader />
                    </div>
                  ) : users.length === 0 ? (
                    <div className="my-5 w-100">
                      <h5>{t("No users found")}</h5>
                    </div>
                  ) : (
                    usersList
                  )}
                </div>
                <div className="mt-2">
                  {usersList.length > 0 ? (
                    <Pagination pages={totalPages} setter={handlePage} currentPage={page} />
                  ) : (
                    <></>
                  )}
                </div>
              </Container>
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default DiscoverProfiles
