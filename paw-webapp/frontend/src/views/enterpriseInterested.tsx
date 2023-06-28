import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import FilterStatusSideBar from "../components/sidebars/filterStatusSideBar"
import EnterpriseSortBySelect from "../components/selects/enterpriseSortBySelect"
import { useEffect } from "react"
import { useTranslation } from "react-i18next"
import InterestedTable from "../components/tables/interestedTable"

function EnterpriseInterested() {
  const { t } = useTranslation()

  useEffect(() => {
    document.title = t("Interested Page Title")
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
                <h3 style={{ textAlign: "left" }}>{t("Interested")}</h3>
                <EnterpriseSortBySelect />
              </div>
            </Row>
            <Row className="m-2">
              <InterestedTable />
            </Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default EnterpriseInterested
