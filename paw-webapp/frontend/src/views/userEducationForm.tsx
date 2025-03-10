import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useState } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate, useParams } from "react-router-dom"
import { useSharedAuth } from "../api/auth"
import { usePostEducation } from "../hooks/usePostEducation"
import { HttpStatusCode } from "axios"
import * as formik from "formik"
import * as yup from "yup"

function EducationForm() {
  const navigate = useNavigate()
  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const { addEducation } = usePostEducation()
  const [error, setError] = useState("")

  document.title = t("Education Form Page Title")

  const { Formik } = formik

  const schema = yup.object().shape({
    college: yup
      .string()
      .required(t("Required") as string)
      .max(50, t("Single Line Max Length") as string),
    degree: yup
      .string()
      .required(t("Required") as string)
      .max(50, t("Single Line Max Length") as string),
    comment: yup.string().max(200, t("Multi Line Max Length") as string),
    yearFrom: yup
      .number()
      .typeError(t("Invalid Number") as string)
      .required(t("Required") as string)
      .min(1900, t("Invalid Year Min") as string)
      .max(new Date().getFullYear(), t("Invalid Year Max") as string),
    yearTo: yup
      .number()
      .typeError(t("Invalid Number") as string)
      .required(t("Required") as string)
      .max(new Date().getFullYear(), t("Invalid Year Max") as string),
  })
  const [monthFrom, setMonthFrom] = useState("1")
  const [monthTo, setMonthTo] = useState("1")

  const handlePost = async (e: any) => {
    const college = e.college
    const degree = e.degree
    const comment = e.comment
    const yearFrom = e.yearFrom
    const yearTo = e.yearTo
    const date1 = new Date(yearFrom, parseInt(monthFrom, 10))
    const date2 = new Date(yearTo, parseInt(monthTo, 10))
    if (date1 > date2) {
      setError(t("Date Validation") as string)
    } else {
      const response = await addEducation(id, college, degree, comment, monthFrom, yearFrom, monthTo, yearTo)
      if (response.status === HttpStatusCode.Created) {
        navigate(`/users/${id}`)
      } else {
        console.error("Error adding education:", response)
      }
    }
  }

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>{t("Education add")}</strong>
                </h2>
                <p>{t("Fill all fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Formik
                      validationSchema={schema}
                      initialValues={{
                        college: "",
                        degree: "",
                        comment: "",
                        yearFrom: "",
                        yearTo: "",
                      }}
                      onSubmit={(values) => {
                        handlePost(values)
                      }}
                    >
                      {({ handleSubmit, handleChange, values, touched, errors }) => (
                        <Form className="msform" onSubmit={handleSubmit}>
                          <div className="form-card">
                            <h2 className="fs-title"> {t("Educacion")} </h2>
                            <Form.Group className="mb-3 mt-3" controlId="formBasicInstitution">
                              <Form.Control
                                name="college"
                                className="input"
                                placeholder={t("Institution*").toString()}
                                value={values.college}
                                onChange={handleChange}
                                isInvalid={!!errors.college}
                              />
                              <Form.Control.Feedback type="invalid">{errors.college}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicDegree">
                              <Form.Control
                                name="degree"
                                className="input"
                                placeholder={t("Degree*").toString()}
                                value={values.degree}
                                onChange={handleChange}
                                isInvalid={!!errors.degree}
                              />
                              <Form.Control.Feedback type="invalid">{errors.degree}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicComment">
                              <Form.Control
                                name="comment"
                                className="input"
                                placeholder={t("Comment").toString()}
                                value={values.comment}
                                onChange={handleChange}
                                isInvalid={!!errors.comment}
                              />
                              <Form.Control.Feedback type="invalid">{errors.comment}</Form.Control.Feedback>
                            </Form.Group>
                            <div className="d-flex mb-4">
                              <div className="row ml-4">
                                <div className="col-sm-4">
                                  <label>{t("From")}</label>
                                </div>
                                <div className="col-sm-4">
                                  <Form.Select
                                    className="selectFrom"
                                    value={monthFrom}
                                    onChange={(e) => setMonthFrom(e.target.value)}
                                  >
                                    <option value="1"> {t("Enero")} </option>
                                    <option value="2"> {t("Febrero")} </option>
                                    <option value="3"> {t("Marzo")} </option>
                                    <option value="4"> {t("Abril")} </option>
                                    <option value="5"> {t("Mayo")} </option>
                                    <option value="6"> {t("Junio")} </option>
                                    <option value="7"> {t("Julio")} </option>
                                    <option value="8"> {t("Agosto")} </option>
                                    <option value="9"> {t("Septiembre")} </option>
                                    <option value="10"> {t("Octubre")} </option>
                                    <option value="11"> {t("Noviembre")} </option>
                                    <option value="12"> {t("Diciembre")} </option>
                                  </Form.Select>
                                </div>
                                <div className="col-sm-4">
                                  <Form.Control
                                    name="yearFrom"
                                    className="input"
                                    placeholder={t("Year").toString()}
                                    value={values.yearFrom}
                                    onChange={handleChange}
                                    isInvalid={!!errors.yearFrom}
                                  />
                                  <Form.Control.Feedback type="invalid">{errors.yearFrom}</Form.Control.Feedback>
                                </div>
                              </div>
                            </div>
                            <div className="d-flex mb-4">
                              <div className="row ml-4">
                                <div className="col-sm-4">
                                  <label>{t("To")}</label>
                                </div>
                                <div className="col-sm-4">
                                  <Form.Select
                                    className="selectTo"
                                    value={monthTo}
                                    onChange={(e) => setMonthTo(e.target.value)}
                                  >
                                    <option value="1"> {t("Enero")} </option>
                                    <option value="2"> {t("Febrero")} </option>
                                    <option value="3"> {t("Marzo")} </option>
                                    <option value="4"> {t("Abril")} </option>
                                    <option value="5"> {t("Mayo")} </option>
                                    <option value="6"> {t("Junio")} </option>
                                    <option value="7"> {t("Julio")} </option>
                                    <option value="8"> {t("Agosto")} </option>
                                    <option value="9"> {t("Septiembre")} </option>
                                    <option value="10"> {t("Octubre")} </option>
                                    <option value="11"> {t("Noviembre")} </option>
                                    <option value="12"> {t("Diciembre")} </option>
                                  </Form.Select>
                                </div>
                                <div className="col-sm-4">
                                  <Form.Control
                                    name="yearTo"
                                    className="input"
                                    placeholder={t("Year").toString()}
                                    value={values.yearTo}
                                    onChange={handleChange}
                                    isInvalid={!!errors.yearTo}
                                  />
                                  <Form.Control.Feedback type="invalid">{errors.yearTo}</Form.Control.Feedback>
                                </div>
                              </div>
                            </div>
                            {error && (
                              <div className="error" style={{ color: "red" }}>
                                {error}
                              </div>
                            )}
                          </div>
                          <p>{t("Fields required")}</p>
                          <Button data-testid="education-form-button" variant="success" type="submit">
                            <strong>{t("Save")}</strong>
                          </Button>
                        </Form>
                      )}
                    </Formik>
                    <div className="row">
                      <div className="col mt-2 mb-2">
                        <Button onClick={() => navigate(`/users/${id}`)} variant="outline-secondary">
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

export default EducationForm
