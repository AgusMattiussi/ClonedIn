import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import ProfileCard from "../components/profileCard"
import JobOfferCard from "../components/jobOfferCard"
import Navigation from "../components/navbar"
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
            <ProfileCard editable={true} />
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
            <JobOfferCard
              enterpriseName="Fake Enterprise"
              category="Finance"
              position="CEO"
              salary="100000"
              enterpriseProfileCard={true}
            />
            <JobOfferCard
              category="Technology"
              position="CTO"
              description="Loren ipsum"
              status="closed"
              enterpriseProfileCard={true}
            />
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default ProfileEnterprise
