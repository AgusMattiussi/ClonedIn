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
import { useNavigate } from "react-router-dom"
import { logout } from "../api/authService"

function Navigation({ isEnterprise }: { isEnterprise: boolean }) {
  const { t } = useTranslation()
  const navigate = useNavigate()

  const handleLogOut = async (e: any) => {
    e.preventDefault()
    logout()
    navigate("/login")
  }

  return (
    <Navbar expand="lg" className="color-nav" variant="dark">
      {isEnterprise ? (
        <div className="m-1 w-100 d-flex px-3">
          <Navbar.Brand href="/profiles">
            <img src={logo} alt="" height="40" className="d-inline-block align-top" />
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link className="nav-item" href="/profiles">
                {t("Discover Profiles")}
              </Nav.Link>
              <Nav.Link className="nav-item" href="/profileEnterprise">
                {t("My Job Offers")}
              </Nav.Link>
              <Nav.Link className="nav-item" href="/contactsEnterprise">
                {t("My Recruits")}
              </Nav.Link>
              <Nav.Link className="nav-item" href="/interestedEnterprise">
                {t("Interested")}
              </Nav.Link>
            </Nav>
            <Button onClick={handleLogOut} variant="outline-success" style={{ color: "white" }}>
              <Icon.BoxArrowRight color="white" size={25} />
              &nbsp;{t("Log Out")}
            </Button>
          </Navbar.Collapse>
        </div>
      ) : (
        <div className="m-1 w-100 d-flex px-3">
          <Navbar.Brand href="/jobs">
            <img src={logo} alt="" height="40" className="d-inline-block align-top" />
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link className="nav-item" href="/jobs">
                {t("Discover Jobs")}
              </Nav.Link>
              <Nav.Link className="nav-item" href="/profileUser">
                {t("My Profile")}
              </Nav.Link>
              <Nav.Link className="nav-item" href="/notificationsUser">
                {t("Job Offers")}
              </Nav.Link>
              <Nav.Link className="nav-item" href="/applicationsUser">
                {t("My Applications")}
              </Nav.Link>
            </Nav>
            <Button onClick={handleLogOut} variant="outline-success" style={{ color: "white" }}>
              <Icon.BoxArrowRight color="white" size={25} />
              &nbsp;{t("Log Out")}
            </Button>
          </Navbar.Collapse>
        </div>
      )}
    </Navbar>
  )
}

Navigation.defaultProps = {
  isEnterprise: false,
}

export default Navigation
