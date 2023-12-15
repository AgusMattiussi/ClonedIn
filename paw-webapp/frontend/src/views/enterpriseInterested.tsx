import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import FilterStatusSideBar from "../components/sidebars/filterStatusSideBar"
import EnterpriseSortBySelect from "../components/selects/enterpriseSortBySelect"
import InterestedTable from "../components/tables/interestedTable"
import Pagination from "../components/pagination"
import { useTranslation } from "react-i18next"
import { useSharedAuth } from "../api/auth"

function EnterpriseInterested() {
  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()

  document.title = t("Interested Page Title")

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container fluid>
        <Row className="align-items-start d-flex">
          <FilterStatusSideBar />
          <Col className="d-flex flex-column my-2">
            <Row className="my-2">
              <div className="d-flex justify-content-between">
                <h3 style={{ textAlign: "left" }}>{t("Interested")}</h3>
                <EnterpriseSortBySelect />
              </div>
            </Row>
            <Row className="m-2">
              <InterestedTable />
              <Pagination />
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default EnterpriseInterested
