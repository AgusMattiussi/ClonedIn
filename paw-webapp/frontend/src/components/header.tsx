import logo from "../images/logo.png"
import "../styles/App.css"
// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css"
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min"
import Navbar from "react-bootstrap/Navbar"
import { useNavigate } from "react-router-dom"

function Header() {
  const navigate = useNavigate()

  return (
    <>
      <Navbar className="custom-header">
        <div className="m-1 w-100 d-flex px-3">
          <Navbar.Brand onClick={() => navigate("/login")}>
            <img src={logo} alt="logo" height="40" className="d-inline-block align-top" />
          </Navbar.Brand>
        </div>
      </Navbar>
    </>
  )
}

export default Header
