import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Card from "react-bootstrap/Card"
import Navigation from "../components/navbar"
import defaultProfile from "../images/defaultProfilePicture.png"
import { useTranslation } from "react-i18next"

function ProfileUser() {
  const { t } = useTranslation()
  return (
    <div>
      <Navigation />
      <Container fluid style={{ background: "#F2F2F2" }}>
        <Row className="row">
          <Col sm={3} className="col">
            <br />
            <Button variant="success" type="button">
              {t("Hide My Profile")}
            </Button>
            <br />
            <Card className="profileCard" style={{ width: "12rem" }}>
              <Card.Img variant="top" src={defaultProfile} />
              <Button type="button" variant="success">
                <Icon.PlusSquare color="white" size={20} style={{ marginRight: "5px" }} />
                {t("Edit Profile Picture")}
              </Button>
              <Card.Body style={{ alignContent: "left", alignItems: "left" }}>
                <Card.Title>
                  Username
                  <Button className="float-end" type="button" variant="outline-success">
                    <Icon.PencilSquare color="green" size={15} />
                  </Button>
                </Card.Title>
                <hr />
                <Card.Text>
                  <Icon.ListTask color="black" size={15} style={{ marginRight: "15px" }} />
                  {t("Job Category")}
                  <br />
                  <Icon.Briefcase color="black" size={15} style={{ marginRight: "15px" }} />
                  {t("Current Position")}
                  <br />
                  <Icon.Book color="black" size={15} style={{ marginRight: "15px" }} />
                  {t("Education Level")}
                  <br />
                  <Icon.GeoAltFill color="black" size={15} style={{ marginRight: "15px" }} />
                  {t("Location")}
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col sm={8} className="col">
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <strong>{t("About Me")}</strong>
                </Card.Title>
                <hr />
                Lorem ipsum
              </Card.Body>
            </Card>
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <strong>{t("Experience")}</strong>
                  <Button className="float-end" type="button" variant="success" style={{ marginLeft: "15px" }}>
                    <Icon.PlusSquare color="white" size={15} style={{ marginRight: "5px" }} />
                    {t("Add Experience")}
                  </Button>
                </Card.Title>
                <hr />
                Lorem ipsum
              </Card.Body>
            </Card>
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <strong>{t("Education Level")}</strong>
                  <Button className="float-end" type="button" variant="success" style={{ marginLeft: "5px" }}>
                    <Icon.PlusSquare color="white" size={15} style={{ marginRight: "5px" }} />
                    {t("Add Education")}
                  </Button>
                </Card.Title>
                <hr />
                Lorem ipsum
              </Card.Body>
            </Card>
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <strong>{t("Skills")}</strong>
                  <Button className="float-end" type="button" variant="success" style={{ marginLeft: "15px" }}>
                    <Icon.PlusSquare color="white" size={15} style={{ marginRight: "5px" }} />
                    {t("Add Skill")}
                  </Button>
                </Card.Title>
                <hr />
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default ProfileUser
