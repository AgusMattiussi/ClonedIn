import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import * as React from "react"
import * as Icon from "react-bootstrap-icons"
import { educationLevels } from "../utils/constants"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { useRegisterUser, useLogin } from "../api/authService"
import { useGetCategories } from "../hooks/useGetCategories"
import * as formik from "formik"
import * as yup from "yup"

function RegisterUser() {
  const [categoryList, setCategoryList] = useState([])
  const [passwordVisibility, setPasswordVisibility] = useState(false)
  const [repeatPasswordVisibility, setRepeatPasswordVisibility] = useState(false)
  const [category, setCategory] = useState("No-Especificado")
  const [educationLevel, setEducationLevel] = useState("No-especificado")

  const { getCategories } = useGetCategories()
  const { registerHandler } = useRegisterUser()
  const { loginHandler } = useLogin()
  const [error, setError] = useState("")

  useEffect(() => {
    const fetchCategories = async () => {
      const response = await getCategories()
      setCategoryList(response.data)
    }

    if (categoryList.length === 0) {
      fetchCategories()
    }
  }, [getCategories, categoryList.length])

  const handleRegister = async (e: any) => {
    const registered = await registerHandler(
      e.email,
      e.password,
      e.repeatPassword,
      e.name,
      e.location,
      e.currentPosition,
      e.description,
      category,
      educationLevel,
    )
    if (registered) {
      await loginHandler(e.email, e.password)
      navigate("/jobOffers")
    } else {
      console.log("Not registered")
      setError(t("Invalid Credentials") as string)
    }
  }

  const handlePasswordVisibility = () => {
    setPasswordVisibility(!passwordVisibility)
  }

  const handleRepeatPasswordVisibility = () => {
    setRepeatPasswordVisibility(!repeatPasswordVisibility)
  }

  const handleEducationLevelSelect = (e: any) => {
    if (e.target.value == "No-especificado" || educationLevels.includes(e.target.value)) {
      setEducationLevel(e.target.value)
    } else {
      alert("ERROR")
    }
  }

  const { t } = useTranslation()
  const navigate = useNavigate()

  document.title = t("Register Page Title")

  const { Formik } = formik

  const schema = yup.object().shape({
    email: yup
      .string()
      .email(t("Invalid Email") as string)
      .required(t("Required") as string)
      .max(100, t("Email Max Length") as string),
    name: yup
      .string()
      .required(t("Required") as string)
      .max(100, t("Line Max Length") as string),
    password: yup
      .string()
      .required(t("Required") as string)
      .min(8, t("Password Min Length") as string)
      .max(20, t("Password Max Length") as string),
    repeatPassword: yup
      .string()
      .oneOf([yup.ref("password")], t("Password Match") as string)
      .required(t("Required") as string),
    location: yup.string().max(50, t("Single Line Max Length") as string),
    currentPosition: yup.string().max(50, t("Single Line Max Length") as string),
    description: yup.string().max(600, t("Long Line Max Length") as string),
  })

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
                        email: "",
                        name: "",
                        password: "",
                        repeatPassword: "",
                        location: "",
                        currentPosition: "",
                        description: "",
                      }}
                      onSubmit={(values) => {
                        handleRegister(values)
                      }}
                    >
                      {({ handleSubmit, handleChange, values, touched, errors }) => (
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
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.email}
                              />
                              <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                              {error && (
                                <div className="error" style={{ color: "red" }}>
                                  {error}
                                </div>
                              )}
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicName">
                              <Form.Control
                                name="name"
                                className="input"
                                placeholder={t("Name*").toString()}
                                value={values.name}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.name}
                              />
                              <Form.Control.Feedback type="invalid">{errors.name}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3 d-flex" controlId="formBasicPassword">
                              <Form.Control
                                name="password"
                                className="input"
                                type={passwordVisibility ? "text" : "password"}
                                placeholder={t("Password*").toString()}
                                value={values.password}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.password}
                              />
                              <Button
                                className="pb-3"
                                onClick={handlePasswordVisibility}
                                style={{ backgroundColor: "white", color: "black", border: "none" }}
                              >
                                {passwordVisibility ? <Icon.Eye /> : <Icon.EyeSlash />}
                              </Button>
                              <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3 d-flex" controlId="formBasicCheckPassword">
                              <Form.Control
                                name="repeatPassword"
                                className="input"
                                type={repeatPasswordVisibility ? "text" : "password"}
                                placeholder={t("Repeat Password*").toString()}
                                value={values.repeatPassword}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.repeatPassword}
                              />
                              <Button
                                className="pb-3"
                                onClick={handleRepeatPasswordVisibility}
                                style={{ backgroundColor: "white", color: "black", border: "none" }}
                              >
                                {repeatPasswordVisibility ? <Icon.Eye /> : <Icon.EyeSlash />}
                              </Button>
                              <Form.Control.Feedback type="invalid">{errors.repeatPassword}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicLocation">
                              <Form.Control
                                name="location"
                                className="input"
                                placeholder={t("Location").toString()}
                                value={values.location}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.location}
                              />
                              <Form.Control.Feedback type="invalid">{errors.location}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicPosition">
                              <Form.Control
                                name="currentPosition"
                                className="input"
                                placeholder={t("Current Position").toString()}
                                value={values.currentPosition}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.currentPosition}
                              />
                              <Form.Control.Feedback type="invalid">{errors.currentPosition}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group>
                              <div className="d-flex mb-4 justify-content-between">
                                <label className="area pt-1 mx-2">{t("Education Level")}</label>
                                <Form.Select
                                  className="selectFrom"
                                  value={educationLevel}
                                  onChange={handleEducationLevelSelect}
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
                            </Form.Group>
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
                                name="description"
                                placeholder={t("About Me").toString()}
                                as="textarea"
                                rows={3}
                                value={values.description}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.description}
                              />
                            </Form.Group>
                            <Form.Control.Feedback type="invalid">{errors.description}</Form.Control.Feedback>
                          </div>
                          <p> {t("Fields required")} </p>
                          <Button data-testid="register-user-button" variant="success" type="submit">
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
