import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import * as React from "react"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { registerEnterprise } from "../api/authService"

function RegisterEnterprise() {
  const [categoryList, setCategoryList] = useState([])
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [repeatPassword, setRepeatPassword] = useState("")
  const [name, setName] = useState("")
  const [city, setCity] = useState("")
  const [workers, setWorkers] = useState("")
  const [year, setYear] = useState("")
  const [link, setLink] = useState("")
  const [aboutUs, setAboutUs] = useState("")
  const [category, setCategory] = useState("")
  const [error, setError] = useState(null)

  /* Cargar lista de rubros */
  useEffect(() => {
    fetch("http://localhost:8080/webapp_war/categories")
      .then((response) => response.json())
      .then((response) => {
        setCategoryList(response)
        setError(null)
      })
      .catch(setError)
  }, [])

  const handleSubmit = (e: any) => {
    e.preventDefault()
    registerEnterprise(email, password, repeatPassword, name, city, workers, year, link, aboutUs, category)
    navigate("/profiles")
  }

  /* TODO: En caso de que haya ERRORS, devolver pantalla adecuada */
  const { t } = useTranslation()
  const navigate = useNavigate()

  document.title = t("Register Page Title")

  return (
    <div>
      <Header />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>{t("Register")}</strong>
                </h2>
                <p>{t("Fill all fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Form className="msform" onSubmit={handleSubmit}>
                      <div className="form-card">
                        <h2 className="fs-title">{t("Basic Information")}</h2>
                        <Form.Group className="mb-3 mt-3" controlId="formBasicEmail">
                          <Form.Control
                            className="input"
                            type="email"
                            placeholder={t("Email*").toString()}
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                          />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicName">
                          <Form.Control
                            className="input"
                            placeholder={t("Enterprise Name*").toString()}
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                          />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicPassword">
                          <Form.Control
                            className="input"
                            type="password"
                            placeholder={t("Password*").toString()}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                          />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicCheckPassword">
                          <Form.Control
                            className="input"
                            type="password"
                            placeholder={t("Repeat Password*").toString()}
                            value={repeatPassword}
                            onChange={(e) => setRepeatPassword(e.target.value)}
                          />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicLocation">
                          <Form.Control
                            className="input"
                            placeholder={t("Location").toString()}
                            value={city}
                            onChange={(e) => setCity(e.target.value)}
                          />
                        </Form.Group>
                        <div className="d-flex mb-4">
                          <label className="area">{t("Quantity of employees")}</label>
                          <Form.Select
                            className="selectFrom"
                            value={workers}
                            onChange={(e) => setWorkers(e.target.value)}
                          >
                            <option value="No-especificado"> {t("No-especificado")} </option>
                            <option value="1-10">1-10</option>
                            <option value="11-50">11-50</option>
                            <option value="51-100">51-100</option>
                            <option value="101-200">101-200</option>
                            <option value="201-500">201-500</option>
                            <option value="501-1000">501-1000</option>
                            <option value="1001-5000">1001-5000</option>
                            <option value="5001-10000">5001-10000</option>
                            <option value="10000+">10001+</option>
                          </Form.Select>
                        </div>
                        <div className="d-flex mb-4">
                          <label className="area">{t("Job Category")}</label>
                          <Form.Select
                            className="selectFrom"
                            aria-label="Categories select"
                            value={category}
                            onChange={(e) => setCategory(e.target.value)}
                          >
                            <option key="1" value="No-Especificado">
                              {t("No-especificado")}
                            </option>
                            {categoryList.map((categoryListItem: any) => (
                              <option key={categoryListItem.id} value={categoryListItem.name}>
                                {t(categoryListItem.name)}
                              </option>
                            ))}
                          </Form.Select>
                        </div>
                        <Form.Group className="mb-3" controlId="formBasicYear">
                          <Form.Control
                            className="input"
                            placeholder={t("Funding Year").toString()}
                            value={year}
                            onChange={(e) => setYear(e.target.value)}
                          />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicWebsite">
                          <Form.Control
                            className="input"
                            placeholder={t("Website").toString()}
                            value={link}
                            onChange={(e) => setLink(e.target.value)}
                          />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                          <Form.Control
                            placeholder={t("About Us").toString()}
                            as="textarea"
                            rows={3}
                            value={aboutUs}
                            onChange={(e) => setAboutUs(e.target.value)}
                          />
                        </Form.Group>
                      </div>
                      <p>{t("Fields required")}</p>
                      <Button variant="success" type="submit">
                        <strong>{t("Register")}</strong>
                      </Button>
                    </Form>
                    <div className="row">
                      <div className="col mt-2 mb-2">
                        <Button onClick={() => navigate(-1)} variant="outline-secondary">
                          <strong>{t("Return")}</strong>
                        </Button>
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

export default RegisterEnterprise
