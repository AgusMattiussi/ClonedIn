import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import * as React from "react"
import * as Icon from "react-bootstrap-icons"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { registerUser } from "../api/authService"
import * as formik from 'formik';
import * as yup from 'yup';

function RegisterUser() {
  const [categoryList, setCategoryList] = useState([])
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [passwordVisibility, setPasswordVisibility] = useState(false)
  const [repeatPassword, setRepeatPassword] = useState("")
  const [repeatPasswordVisibility, setRepeatPasswordVisibility] = useState(false)
  const [name, setName] = useState("")
  const [city, setCity] = useState("")
  const [category, setCategory] = useState("")
  const [position, setPosition] = useState("")
  const [description, setDescription] = useState("")
  const [studiesLevel, setStudiesLevel] = useState("")
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
    registerUser(email, password, repeatPassword, name, city, position, description, category, studiesLevel)
    navigate("/jobs")
  }

  const handlePasswordVisibility = () => {
    setPasswordVisibility(!passwordVisibility)
  }

  const handleRepeatPasswordVisibility = () => {
    setRepeatPasswordVisibility(!repeatPasswordVisibility)
  }

  /* TODO: En caso de que haya ERRORS, devolver pantalla adecuada */
  const { t } = useTranslation()
  const navigate = useNavigate()

  document.title = t("Register Page Title")

  const { Formik } = formik;

  const schema = yup.object().shape({
    email: yup.string().email('Invalid email').required('Required'),
    name: yup.string().required('Required'),
    pass: yup.string().required('Required'),
    repeatPass: yup.string().oneOf([yup.ref('pass')], 'Passwords must match').required('Required')
  });

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
                <p> {t("Fill all fields")} </p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                  <Formik
                    validationSchema={schema}
                    initialValues={{
                      email: '',
                      name: '',
                      pass: '',
                      repeatPass: '',
                    }}
                    onSubmit={values => {
                      console.log(values);
                    }}
                  >
                  {({handleSubmit, handleChange, values, touched, errors }) => (
                    <Form className="msform" noValidate onSubmit={handleSubmit}>
                      <div className="form-card">
                        <h2 className="fs-title">{t("Basic Information")}</h2>
                        <Form.Group className="mb-3 mt-3" controlId="formBasicEmail">
                          <Form.Control
                            name="email"
                            className="input"
                            type="email"
                            placeholder={t("Email*").toString()}
                            value={values.email}
                            onChange={handleChange}
                            isInvalid={!!errors.email}
                          />
                          <Form.Control.Feedback type="invalid">
                            {errors.email}
                          </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicName">
                          <Form.Control
                            name="name"
                            className="input"
                            placeholder={t("Name*").toString()}
                            value={values.name}
                            onChange={handleChange}
                            isInvalid={!!errors.name}
                          />
                          <Form.Control.Feedback type="invalid">
                            {errors.name}
                          </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3 d-flex" controlId="formBasicPassword">
                          <Form.Control
                            name="pass"
                            className="input"
                            type={passwordVisibility ? "text" : "password"}
                            placeholder={t("Password*").toString()}
                            value={values.pass}
                            onChange={handleChange}
                            isInvalid={!!errors.pass}
                          />
                          <Button
                            className="pb-3"
                            onClick={handlePasswordVisibility}
                            style={{ backgroundColor: "white", color: "black", border: "none" }}
                          >
                            {passwordVisibility ? <Icon.Eye /> : <Icon.EyeSlash />}
                          </Button>
                          <Form.Control.Feedback type="invalid">
                            {errors.pass}
                          </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3 d-flex" controlId="formBasicCheckPassword">
                          <Form.Control
                            name="repeatPass"
                            className="input"
                            type={repeatPasswordVisibility ? "text" : "password"}
                            placeholder={t("Repeat Password*").toString()}
                            value={values.repeatPass}
                            onChange={handleChange}
                            isInvalid={!!errors.repeatPass}
                          />
                          <Button
                            className="pb-3"
                            onClick={handleRepeatPasswordVisibility}
                            style={{ backgroundColor: "white", color: "black", border: "none" }}
                          >
                            {repeatPasswordVisibility ? <Icon.Eye /> : <Icon.EyeSlash />}
                          </Button>
                          <Form.Control.Feedback type="invalid">
                            {errors.repeatPass}
                          </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicLocation">
                          <Form.Control
                            className="input"
                            placeholder={t("Location").toString()}
                            value={city}
                            onChange={(e) => setCity(e.target.value)}
                          />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicPosition">
                          <Form.Control
                            className="input"
                            placeholder={t("Current Position").toString()}
                            value={position}
                            onChange={(e) => setPosition(e.target.value)}
                          />
                        </Form.Group>
                        <div className="d-flex mb-4 justify-content-between">
                          <label className="area pt-1 mx-2">{t("Education Level")}</label>
                          <Form.Select
                            className="selectFrom"
                            value={studiesLevel}
                            onChange={(e) => setStudiesLevel(e.target.value)}
                            style={{ width: "60%" }}
                          >
                            <option value="No-especificado"> {t("No-especificado")} </option>
                            <option value="Primario"> {t("Primario")} </option>
                            <option value="Secundario"> {t("Secundario")} </option>
                            <option value="Terciario"> {t("Terciario")} </option>
                            <option value="Graduado"> {t("Graduado")} </option>
                            <option value="Postgrado"> {t("Postgrado")} </option>
                          </Form.Select>
                        </div>
                        <div className="d-flex mb-4 justify-content-between">
                          <label className="area pt-1 mx-2">{t("Job Category")}</label>
                          <Form.Select
                            className="selectFrom"
                            aria-label="Default select example"
                            value={category}
                            onChange={(e) => setCategory(e.target.value)}
                            style={{ width: "60%" }}
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
                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                          <Form.Control
                            placeholder={t("About Me").toString()}
                            as="textarea"
                            rows={3}
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                          />
                        </Form.Group>
                      </div>
                      <p> {t("Fields required")} </p>
                      <Button variant="success" type="submit">
                        <strong>{t("Register")}</strong>
                      </Button>
                    </Form>
                  )}
                    </Formik>
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

export default RegisterUser
