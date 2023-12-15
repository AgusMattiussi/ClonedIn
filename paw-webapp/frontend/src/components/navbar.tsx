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
import { useSharedAuth } from "../api/auth"
import { UserRole } from "../utils/constants"

function Navigation({ role }: { role: UserRole }) {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const { userInfo, handleLogout } = useSharedAuth()

  const handleLogOut = async (e: any) => {
    e.preventDefault()
    await handleLogout()
    navigate("/login")
  }

  return (
    <Navbar expand="lg" className="color-nav" variant="dark">
      {role === "ENTERPRISE" ? (
        <div className="m-1 w-100 d-flex px-3">
          <Navbar.Brand onClick={() => navigate("/profiles")}>
            <img src={logo} alt="" height="40" className="d-inline-block align-top" />
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link className="nav-item" onClick={() => navigate("/profiles")}>
                {t("Discover Profiles")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/profileEnterprise/${userInfo?.id}`)}>
                {t("My Job Offers")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/contactsEnterprise/${userInfo?.id}`)}>
                {t("My Recruits")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/interestedEnterprise/${userInfo?.id}`)}>
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
          <Navbar.Brand onClick={() => navigate("/jobs")}>
            <img src={logo} alt="" height="40" className="d-inline-block align-top" />
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link className="nav-item" onClick={() => navigate("/jobs")}>
                {t("Discover Jobs")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/profileUser/${userInfo?.id}`)}>
                {t("My Profile")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/notificationsUser/${userInfo?.id}`)}>
                {t("Job Offers")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/applicationsUser/${userInfo?.id}`)}>
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
  role: "USER",
}

export default Navigation
