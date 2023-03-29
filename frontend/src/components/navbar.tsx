import logo from "../images/logo.png"
import * as Icon from "react-bootstrap-icons"
import Button from "react-bootstrap/Button"
import "../styles/App.css"
// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css"
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min"
import Navbar from "react-bootstrap/Navbar"
import Nav from "react-bootstrap/Nav"
import { useTranslation } from "react-i18next"

function Navigation() {
  const { t } = useTranslation()

  return (
    <Navbar expand="lg" className="color-nav" variant="dark">
      <div className="m-1 w-100 d-flex px-3">
        <Navbar.Brand href="/discoverJobs">
          <img src={logo} alt="" height="40" className="d-inline-block align-top" />
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link className="nav-item" href="/discoverJobs">
              {t("Discover Jobs")}
            </Nav.Link>
            <Nav.Link className="nav-item" href="/profileUser">
              {t("My Profile")}
            </Nav.Link>
            <Nav.Link className="nav-item" href="/notificationsUser">
              {t("Job Offers")}
            </Nav.Link>
            <Nav.Link className="nav-item" href="#applications">
              {t("My Applications")}
            </Nav.Link>
          </Nav>
          <Button href="/home" variant="outline-success" style={{ color: "white" }}>
            <Icon.BoxArrowRight color="white" size={25} />
            &nbsp;{t("Log Out")}
          </Button>
        </Navbar.Collapse>
      </div>
    </Navbar>
  )
}
export default Navigation
