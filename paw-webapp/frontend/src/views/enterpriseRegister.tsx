import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import * as Icon from "react-bootstrap-icons"
import * as React from "react"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { useRegisterEnterprise, useLogin } from "../api/authService"
import { useRequestApi } from "../api/apiRequest"
import * as formik from "formik"
import * as yup from "yup"

function RegisterEnterprise() {
  const [categoryList, setCategoryList] = useState([])
  const [passwordVisibility, setPasswordVisibility] = useState(false)
  const [repeatPasswordVisibility, setRepeatPasswordVisibility] = useState(false)
  const [city, setCity] = useState("")
  const [workers, setWorkers] = useState("")
  const [year, setYear] = useState("")
  const [link, setLink] = useState("")
  const [aboutUs, setAboutUs] = useState("")
  const [category, setCategory] = useState("")

  const { loading, apiRequest } = useRequestApi()
  const { registerHandler } = useRegisterEnterprise()
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
    await registerHandler(e.email, e.pass, e.repeatPass, e.name, city, workers, year, link, aboutUs, category)
    await loginHandler(e.email, e.pass)
    navigate("/users")
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

  const { Formik } = formik

  const schema = yup.object().shape({
    email: yup.string().email(t('Invalid Email') as string).required(t('Required') as string),
    name: yup.string().required(t('Required') as string),
    pass: yup.string().required(t('Required') as string).min(8, t('Password Min Length') as string),
    repeatPass: yup
      .string()
      .oneOf([yup.ref("pass")], t('Password Match') as string)
      .required(t('Required') as string),
    foundingYear: yup.number().max(new Date().getFullYear(), t('Invalid Year') as string),
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
                <p>{t("Fill all fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Formik
                      validationSchema={schema}
                      initialValues={{
                        email: "",
                        name: "",
                        pass: "",
                        repeatPass: "",
                        foundingYear: "",
                      }}
                      onSubmit={(values) => {
                        handleRegister(values)
                      }}
                    >
                      {({ handleSubmit, handleChange, values, touched, errors }) => (
                        <Form className="msform" onSubmit={handleSubmit}>
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
                              <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicName">
                              <Form.Control
                                name="name"
                                className="input"
                                placeholder={t("Enterprise Name*").toString()}
                                value={values.name}
                                onChange={handleChange}
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
                              <Form.Control.Feedback type="invalid">{errors.pass}</Form.Control.Feedback>
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
                              <Form.Control.Feedback type="invalid">{errors.repeatPass}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicLocation">
                              <Form.Control
                                className="input"
                                placeholder={t("Location").toString()}
                                value={city}
                                onChange={(e) => setCity(e.target.value)}
                              />
                            </Form.Group>
                            <div className="d-flex mb-4 justify-content-between">
                              <label className="area pt-1 mx-1">{t("Quantity of employees")}</label>
                              <Form.Select
                                className="selectFrom"
                                value={workers}
                                onChange={(e) => setWorkers(e.target.value)}
                                style={{ width: "60%" }}
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
                            <div className="d-flex mb-4 justify-content-between">
                              <label className="area pt-1 mx-1">{t("Job Category")}</label>
                              <Form.Select
                                className="selectFrom"
                                aria-label="Categories select"
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
                            <Form.Group className="mb-3" controlId="formBasicYear">
                              <Form.Control
                                name="foundingYear"
                                className="input"
                                placeholder={t("Funding Year").toString()}
                                value={values.foundingYear}
                                onChange={handleChange}
                                isInvalid={!!errors.foundingYear}
                              />
                              <Form.Control.Feedback type="invalid">{errors.foundingYear}</Form.Control.Feedback>
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

export default RegisterEnterprise
