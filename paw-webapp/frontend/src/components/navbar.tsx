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
          <Navbar.Brand onClick={() => navigate("/users")}>
            <img src={logo} alt="" height="40" className="d-inline-block align-top" />
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link className="nav-item" onClick={() => navigate("/users")}>
                {t("Discover Profiles")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/enterprises/${userInfo?.id}`)}>
                {t("My Job Offers")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/enterprises/${userInfo?.id}/contacts`)}>
                {t("My Recruits")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/enterprises/${userInfo?.id}/interested`)}>
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
          <Navbar.Brand onClick={() => navigate("/jobOffers")}>
            <img src={logo} alt="" height="40" className="d-inline-block align-top" />
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link className="nav-item" onClick={() => navigate("/jobOffers")}>
                {t("Discover Jobs")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/users/${userInfo?.id}`)}>
                {t("My Profile")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/users/${userInfo?.id}/notifications`)}>
                {t("Job Offers")}
              </Nav.Link>
              <Nav.Link className="nav-item" onClick={() => navigate(`/users/${userInfo?.id}/applications`)}>
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
