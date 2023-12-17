import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Header from "../components/header"
import Container from "react-bootstrap/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { useState } from "react"
import { useLogin } from "../api/authService"
import * as formik from "formik"
import * as yup from "yup"
import { useSharedAuth } from "../api/auth"
import { UserRole } from "../utils/constants"

function Login() {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [passwordVisibility, setPasswordVisibility] = useState(false)
  const { loading, loginHandler } = useLogin()
  const { userInfo } = useSharedAuth()

  //TODO: Remember me
  //const [rememberMe, setRememberMe] = useState(false)

  const { t } = useTranslation()
  const navigate = useNavigate()

  document.title = t("Login Page Title")

  const handlePasswordVisibility = () => {
    setPasswordVisibility(!passwordVisibility)
  }

  const handleSubmit = async (e: any) => {
    e.preventDefault()
    const logged = await loginHandler(email, password)
    if (logged) {
      console.log("Logged in")
      if (userInfo?.role === UserRole.USER) {
        console.log(userInfo?.role)
        navigate("/jobs")
      } else {
        console.log(userInfo?.role)
        navigate("/profiles")
      }
    } else {
      console.log("Not logged in")
      //TODO: Show errors in the form
    }
  }

  return (
    <div>
      <Header />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>{t("Welcome to ClonedIN!")}</strong>
                </h2>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Form className="msform" onSubmit={handleSubmit}>
                      <div className="form-card">
                        <Form.Group className="mb-3" controlId="formBasicEmail">
                          <Form.Control
                            className="input"
                            type="email"
                            placeholder={t("Email").toString()}
                            onChange={(e) => setEmail(e.target.value)}
                          />
                        </Form.Group>
                        <Form.Group className="mb-3 d-flex" controlId="formBasicPassword">
                          <Form.Control
                            className="input"
                            type={passwordVisibility ? "text" : "password"}
                            placeholder={t("Password").toString()}
                            onChange={(e) => setPassword(e.target.value)}
                          />
                          <Button
                            className="pb-3"
                            onClick={handlePasswordVisibility}
                            style={{ backgroundColor: "white", color: "black", border: "none" }}
                          >
                            {passwordVisibility ? <Icon.Eye /> : <Icon.EyeSlash />}
                          </Button>
                        </Form.Group>
                        <Form.Group className="mb-3 rememberme" controlId="formBasicCheckbox">
                          <Form.Check type="checkbox" label={t("Remember me").toString()} />
                        </Form.Group>
                      </div>
                      <Button variant="success" type="submit">
                        <strong>{t("Log In")}</strong>
                      </Button>
                      <p>{t("No account yet?")}</p>
                    </Form>
                    <div className="row">
                      <div className="col">
                        <Button onClick={() => navigate("/registerUser")} variant="success">
                          <Icon.Person size={40} />
                        </Button>
                        <p>{t("Register as a User")}</p>
                      </div>
                      <div className="col">
                        <Button onClick={() => navigate("/registerEnterprise")} variant="success">
                          <Icon.Building size={40} />
                        </Button>
                        <p>{t("Register as an Enterprise")}</p>
                      </div>
                    </div>
                  </div>
                </div>
              </Card>
            </div>
          </div>
        </Container>
      </div>
    </div>
  )
}

export default Login
