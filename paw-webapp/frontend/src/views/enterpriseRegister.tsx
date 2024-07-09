import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import * as Icon from "react-bootstrap-icons"
import * as React from "react"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { useRegisterEnterprise, useLogin } from "../api/authService"
import { useGetCategories } from "../hooks/useGetCategories"
import * as formik from "formik"
import * as yup from "yup"

function RegisterEnterprise() {
  const [categoryList, setCategoryList] = useState([])
  const [passwordVisibility, setPasswordVisibility] = useState(false)
  const [repeatPasswordVisibility, setRepeatPasswordVisibility] = useState(false)
  const [workers, setWorkers] = useState("No-especificado")
  const [category, setCategory] = useState("No-Especificado")

  const { getCategories } = useGetCategories()
  const { registerHandler } = useRegisterEnterprise()
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
    let fYear = e.foundingYear
    if (fYear == "") {
      fYear = "null"
    }
    const registered = await registerHandler(
      e.email,
      e.pass,
      e.repeatPass,
      e.name,
      e.location,
      category,
      workers,
      fYear,
      e.website,
      e.description,
    )
    if (registered) {
      await loginHandler(e.email, e.pass)
      navigate("/users")
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

  const { t } = useTranslation()
  const navigate = useNavigate()

  document.title = t("Register Page Title")

  const { Formik } = formik

  const re =
    /^((ftp|http|https):\/\/)?(www.)?(?!.*(ftp|http|https|www.))[a-zA-Z0-9_-]+(\.[a-zA-Z]+)+((\/)[\w#]+)*(\/\w+\?[a-zA-Z0-9_]+=\w+(&[a-zA-Z0-9_]+=\w+)*)?$/gm

  const schema = yup.object().shape({
    email: yup
      .string()
      .email(t("Invalid Email") as string)
      .required(t("Required") as string)
      .max(100, t("Email Max Length") as string),
    name: yup
      .string()
      .required(t("Required") as string)
      .max(50, t("Single Line Max Length") as string),
    pass: yup
      .string()
      .required(t("Required") as string)
      .min(8, t("Password Min Length") as string)
      .max(20, t("Password Max Length") as string),
    repeatPass: yup
      .string()
      .oneOf([yup.ref("pass")], t("Password Match") as string)
      .required(t("Required") as string),
    location: yup.string().max(50, t("Single Line Max Length") as string),
    foundingYear: yup
      .number()
      .typeError(t("Invalid Number") as string)
      .min(1000, t("Invalid Year Min") as string)
      .max(new Date().getFullYear(), t("Invalid Year Max") as string),
    website: yup
      .string()
      .matches(re, t("Invalid URL") as string)
      .max(200, t("Multi Line Max Length") as string),
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
                        location: "",
                        foundingYear: "",
                        website: "",
                        description: "",
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
                                name="location"
                                className="input"
                                placeholder={t("Location").toString()}
                                value={values.location}
                                onChange={handleChange}
                                isInvalid={!!errors.location}
                              />
                              <Form.Control.Feedback type="invalid">{errors.location}</Form.Control.Feedback>
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
                                placeholder={t("Founding Year").toString()}
                                value={values.foundingYear}
                                onChange={handleChange}
                                isInvalid={!!errors.foundingYear}
                              />
                              <Form.Control.Feedback type="invalid">{errors.foundingYear}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicWebsite">
                              <Form.Control
                                name="website"
                                className="input"
                                placeholder={t("Website").toString()}
                                value={values.website}
                                onChange={handleChange}
                                isInvalid={!!errors.website}
                              />
                              <Form.Control.Feedback type="invalid">{errors.website}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                              <Form.Control
                                name="description"
                                placeholder={t("About Us").toString()}
                                as="textarea"
                                rows={3}
                                value={values.description}
                                onChange={handleChange}
                                isInvalid={!!errors.description}
                              />
                              <Form.Control.Feedback type="invalid">{errors.description}</Form.Control.Feedback>
                            </Form.Group>
                          </div>
                          <p>{t("Fields required")}</p>
                          <Button data-testid="register-enterprise-button" variant="success" type="submit">
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
