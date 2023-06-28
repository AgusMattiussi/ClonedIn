import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import ProfileEnterpriseCard from "../components/cards/profileEnterpriseCard"
import JobOfferEnterpriseCard from "../components/cards/jobOfferEnterpriseCard"
import Navigation from "../components/navbar"
import Pagination from "../components/pagination"
import { useTranslation } from "react-i18next"
import { useEffect } from "react"

function ProfileEnterprise() {
  useEffect(() => {
    document.title = "EnterpriseName | ClonedIn" // TODO: Add enterprise name
  }, [])

  const { t } = useTranslation()

  return (
    <div>
      <Navigation isEnterprise={true} />
      <Container fluid style={{ background: "#F2F2F2", height: "800px" }}>
        <Row className="row">
          <Col sm={3} className="col d-flex flex-column align-items-center">
            <br />
            <ProfileEnterpriseCard editable={true} />
          </Col>
          <Col sm={8} className="col d-flex flex-column align-items-center">
            <br />
            <Button variant="success" type="button">
              <div className="d-flex align-items-center justify-content-center">
                <Icon.PlusSquare color="white" size={20} style={{ marginRight: "7px" }} />
                {t("Add Job Offer")}
              </div>
            </Button>
            <br />

            <JobOfferEnterpriseCard category="Finance" position="CEO" salary="100000" />
            <JobOfferEnterpriseCard category="Technology" position="CTO" description="Loren ipsum" status="closed" />
            <Pagination />
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default ProfileEnterprise
