import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import ProfileUserCard from "../components/cards/profileUserCard"
import Button from "react-bootstrap/Button"
import Form from "react-bootstrap/Form"
import * as Icon from "react-bootstrap-icons"
import { Link, useNavigate, useSearchParams } from "react-router-dom"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import Loader from "../components/loader"
import { useRequestApi } from "../api/apiRequest"
import { useSharedAuth } from "../api/auth"
import Pagination from "../components/pagination"
import { HttpStatusCode } from "axios"

function DiscoverProfiles() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { loading, apiRequest } = useRequestApi()
  const { userInfo } = useSharedAuth()

  const [isLoading, setLoading] = useState(true)
  const [users, setUsers] = useState<any[]>([])

  const [searchTerm, setSearchTerm] = useState("")

  const [categoryList, setCategoryList] = useState([])
  const [categoryName, setCategoryName] = useState("")

  const [educationLevel, setEducationLevel] = useState("")

  const [minExpYears, setMinExpYears] = useState("")
  const [maxExpYears, setMaxExpYears] = useState("")

  const [queryParams, setQueryParams] = useSearchParams()

  document.title = t("Discover Profiles") + " | ClonedIn"

  const fetchUsers = async (
    categoryName: string,
    educationLevel: string,
    searchTerm: string,
    minExpYears: string,
    maxExpYears: string,
  ) => {
    const queryParams: Record<string, string> = {}
    if (categoryName) queryParams.categoryName = categoryName
    if (educationLevel) queryParams.educationLevel = educationLevel
    if (searchTerm) queryParams.searchTerm = searchTerm
    if (minExpYears) queryParams.minExpYears = minExpYears
    if (maxExpYears) queryParams.maxExpYears = maxExpYears

    // setQueryParams(queryParams)

    const response = await apiRequest({
      url: "/users",
      method: "GET",
      queryParams: queryParams,
    })

    if (response.status === 500) {
      navigate("/403")
    }

    if (response.status === HttpStatusCode.NoContent) {
      setUsers([])
    } else {
      setUsers(response.data)
    }

    setLoading(false)
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

    if (isLoading === true) {
      fetchUsers(categoryName, educationLevel, searchTerm, minExpYears, maxExpYears)
    }
  }, [apiRequest])

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
    setEducationLevel("")
    setMinExpYears("")
    setMaxExpYears("")
  }

  //TODO: ordenamiento
  const usersList = users.map((user) => {
    return (
      <Link to={`/profileUser/${user.id}`} style={{ textDecoration: "none", color: "black" }} key={user.id}>
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
              <div className="d-flex flex-wrap justify-content-center">
                <Button variant="outline-light " className="filterbtn" onClick={() => handleClear()}>
                  {t("Clear Filter")}
                </Button>
              </div>
            </Row>
          </Col>
          <Col className="align-items-start d-flex flex-column mt-2 mr-2 mb-2">
            <Row>
              <h3>{t("Discover Profiles")}</h3>
            </Row>
            <Row
              className="rounded-3 d-flex flex-row flex-wrap w-auto"
              style={{
                marginLeft: "0px",
                marginRight: "3px",
              }}
            >
              <Container
                className="p-2 rounded-3 d-flex flex-wrap w-auto justify-content-center"
                fluid
                style={{
                  background: "#F2F2F2",
                  boxShadow: "0 0 10px rgba(0,0,0,0.16), 0 0 4px rgba(0,0,0,0.23)",
                  minWidth: "100vh",
                }}
              >
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
                <Pagination />
              </Container>
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default DiscoverProfiles
