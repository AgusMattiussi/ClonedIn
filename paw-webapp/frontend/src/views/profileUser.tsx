import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import ProfileCard from "../components/profileCard"
import Navigation from "../components/navbar"
import { useTranslation } from "react-i18next"

function ProfileUser() {
  const { t } = useTranslation()
  return (
    <div>
      <Navigation />
      <Container fluid style={{ background: "#F2F2F2", height: "800px" }}>
        <Row className="row">
          <Col sm={3} className="col">
            <br />
            <Button variant="success" type="button">
              {t("Hide My Profile")}
            </Button>
            <br />
            <ProfileCard editable={true} />
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
                  <div className="d-flex flex-row justify-content-between align-items-center">
                    <strong>{t("Experience")}</strong>
                    <Button type="button" variant="success" style={{ width: "200px" }}>
                      <Icon.PlusSquare color="white" style={{ marginRight: "7px" }} />
                      {t("Add Experience")}
                    </Button>
                  </div>
                </Card.Title>
                <hr />
                <div className="d-flex flex-row justify-content-between align-items-center">
                  <h6>Enterprise Name - Job Position</h6>
                  <Button type="button" variant="outline-danger">
                    <Icon.Trash />
                  </Button>
                </div>
                <p style={{ fontSize: "10pt" }}>
                  {/* TODO: agregar condicionales si no especifico fecha */}
                  Enero 2019 - Presente
                </p>
                <p>Description</p>
                <hr />
              </Card.Body>
            </Card>
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <div className="d-flex flex-row justify-content-between align-items-center">
                    <strong>{t("Education Level")}</strong>
                    <Button type="button" variant="success" style={{ width: "200px" }}>
                      <Icon.PlusSquare color="white" style={{ marginRight: "7px" }} />
                      {t("Add Education")}
                    </Button>
                  </div>
                </Card.Title>
                <hr />
                <div className="d-flex flex-row justify-content-between align-items-center">
                  <h6>Institution Name - Degree</h6>
                  <Button type="button" variant="outline-danger">
                    <Icon.Trash />
                  </Button>
                </div>
                <p style={{ fontSize: "10pt" }}>Marzo 2013 - Diciembre 2018</p>
                <hr />
              </Card.Body>
            </Card>
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <div className="d-flex flex-row justify-content-between align-items-center">
                    <strong>{t("Skills")}</strong>
                    <Button type="button" variant="success" style={{ width: "200px" }}>
                      <Icon.PlusSquare color="white" style={{ marginRight: "7px" }} />
                      {t("Add Skill")}
                    </Button>
                  </div>
                </Card.Title>
                <hr />
                {/* View As Enterprise
                <Badge pill bg="success" className="mx-2 p-2">
                  skill1
                </Badge> */}
                <Badge pill bg="light" text="dark" className="mx-2">
                  skill1
                  <Button type="button" variant="outline-dark" style={{ borderStyle: "none" }}>
                    <Icon.X />
                  </Button>
                </Badge>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default ProfileUser
