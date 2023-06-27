import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Navigation from "../components/navbar"
import FilterStatusSideBar from "../components/sidebars/filterStatusSideBar"
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
            <Row className="my-2"></Row>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default EnterpriseContacts
