import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import FilterStatusSideBar from "../components/sidebars/filterStatusSideBar"
import EnterpriseSortBySelect from "../components/selects/enterpriseSortBySelect"
import ContactsTable from "../components/tables/contactsTable"
import Pagination from "../components/pagination"
import { useEffect } from "react"
import { useTranslation } from "react-i18next"

function EnterpriseContacts() {
  const { t } = useTranslation()

  useEffect(() => {
    document.title = t("My Recruits Page Title")
  }, [])

  return (
    <div>
      <Navigation isEnterprise={true} />
      <Container fluid>
        <Row className="align-items-start d-flex">
          <FilterStatusSideBar />
          <Col className="d-flex flex-column my-2">
            <Row className="my-2">
              <div className="d-flex justify-content-between">
                <h3 style={{ textAlign: "left" }}>{t("My Recruits")}</h3>
                <EnterpriseSortBySelect />
              </div>
            </Row>
            <Row className="m-2">
              <ContactsTable />
              <Pagination />
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default EnterpriseContacts
