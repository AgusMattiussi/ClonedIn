import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import * as React from "react"
import * as Icon from "react-bootstrap-icons"
import { educationLevels } from "../utils/constants"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { useRegisterUser, useLogin } from "../api/authService"
import { useRequestApi } from "../api/apiRequest"
import * as formik from "formik"
import * as yup from "yup"

function RegisterUser() {
  const [categoryList, setCategoryList] = useState([])
  const [passwordVisibility, setPasswordVisibility] = useState(false)
  const [repeatPasswordVisibility, setRepeatPasswordVisibility] = useState(false)
  const [category, setCategory] = useState("")
  const [educationLevel, setEducationLevel] = useState("")

  const { loading, apiRequest } = useRequestApi()
  const { registerHandler } = useRegisterUser()
  const { loginHandler } = useLogin()

  useEffect(() => {
    const fetchCategories = async () => {
      const response = await apiRequest({
        url: "/categories",
        method: "GET",
      })
      setCategoryList(response.data)
    }

    if (categoryList.length === 0) {
      fetchCategories()
    }
  }, [apiRequest, categoryList.length])

  const handleRegister = async (e: any) => {
    await registerHandler(
      e.email,
      e.pass,
      e.repeatPass,
      e.name,
      e.location,
      e.position,
      e.description,
      category,
      educationLevel,
    )
    await loginHandler(e.email, e.pass)
    navigate("/jobOffers")
  }

  const handlePasswordVisibility = () => {
    setPasswordVisibility(!passwordVisibility)
  }

  const handleRepeatPasswordVisibility = () => {
    setRepeatPasswordVisibility(!repeatPasswordVisibility)
  }

  const handleEducationLevelSelect = (e: any) => {
    if (educationLevels.includes(e.target.value)) {
      setEducationLevel(e.target.value)
    } else {
      alert("ERROR");
    }
  }

  //TODO: Ver como leer la category list
  const handleJobCategorySelect = (e: any) => {
    if (e.target.value == "Moda") {
      setCategory(e.target.value)
    } else {
      alert("ERROR");
    }
  }

  /* TODO: En caso de que haya ERRORS, devolver pantalla adecuada */
  const { t } = useTranslation()
  const navigate = useNavigate()

  document.title = t("Register Page Title")

  const { Formik } = formik

  const schema = yup.object().shape({
    email: yup.string().email(t('Invalid Email') as string).required(t('Required') as string),
    name: yup.string().required(t('Required') as string),
    pass: yup.string().required(t('Required') as string).min(8, t('Password Min Length') as string),
    repeatPass: yup
      .string()
      .oneOf([yup.ref("pass")], t('Password Match') as string)
      .required(t('Required') as string),
    location: yup.string().max(50, t('Single Line Max Length') as string),
    position: yup.string().max(50, t('Single Line Max Length') as string),
    description: yup.string().max(200, t('Multi Line Max Length') as string),
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
                        pass: "",
                        repeatPass: "",
                        location: "",
                        position: "",
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
                                name="pass"
                                className="input"
                                type={passwordVisibility ? "text" : "password"}
                                placeholder={t("Password*").toString()}
                                value={values.pass}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.pass}
                              />
                              <Button
                                className="pb-3"
                                onClick={handlePasswordVisibility}
                                style={{ backgroundColor: "white", color: "black", border: "none" }}
                              >
                                {passwordVisibility ? <Icon.Eye /> : <Icon.EyeSlash />}
                              </Button>
                              <Form.Control.Feedback type="invalid">{errors.pass}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3 d-flex" controlId="formBasicCheckPassword">
                              <Form.Control
                                name="repeatPass"
                                className="input"
                                type={repeatPasswordVisibility ? "text" : "password"}
                                placeholder={t("Repeat Password*").toString()}
                                value={values.repeatPass}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.repeatPass}
                              />
                              <Button
                                className="pb-3"
                                onClick={handleRepeatPasswordVisibility}
                                style={{ backgroundColor: "white", color: "black", border: "none" }}
                              >
                                {repeatPasswordVisibility ? <Icon.Eye /> : <Icon.EyeSlash />}
                              </Button>
                              <Form.Control.Feedback type="invalid">{errors.repeatPass}</Form.Control.Feedback>
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
                                name="position"
                                className="input"
                                placeholder={t("Current Position").toString()}
                                value={values.position}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.position}
                              />
                              <Form.Control.Feedback type="invalid">{errors.position}</Form.Control.Feedback>
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
