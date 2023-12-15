import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { useSharedAuth } from "../api/auth"
import * as formik from "formik"
import * as yup from "yup"

function ExperienceForm() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const { userInfo } = useSharedAuth()

  document.title = t("Experience Form Page Title")

  const { Formik } = formik

  const schema = yup.object().shape({
    name: yup.string().required("Required"),
    position: yup.string().required("Required"),
  })

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>{t("Experience add")}</strong>
                </h2>
                <p>{t("Fill all fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Formik
                      validationSchema={schema}
                      initialValues={{
                        name: "",
                        position: "",
                      }}
                      onSubmit={(values) => {
                        console.log(values)
                      }}
                    >
                      {({ handleSubmit, handleChange, values, touched, errors }) => (
                        <Form className="msform" onSubmit={handleSubmit}>
                          <div className="form-card">
                            <h2 className="fs-title">{t("Experience")}</h2>
                            <Form.Group className="mb-3 mt-3" controlId="formBasicEnterprise">
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
                            <Form.Group className="mb-3" controlId="formBasicPosition">
                              <Form.Control
                                name="position"
                                className="input"
                                placeholder={t("Position*").toString()}
                                value={values.position}
                                onChange={handleChange}
                                isInvalid={!!errors.position}
                              />
                              <Form.Control.Feedback type="invalid">{errors.position}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicDescription">
                              <Form.Control className="input" placeholder={t("Description").toString()} />
                            </Form.Group>
                            <div className="d-flex mb-4">
                              <div className="row ml-4">
                                <div className="col-sm-4">
                                  <label>{t("From")}</label>
                                </div>
                                <div className="col-sm-4">
                                  <Form.Select className="selectFrom">
                                    <option value="Enero"> {t("Enero")} </option>
                                    <option value="Febrero"> {t("Febrero")} </option>
                                    <option value="Marzo"> {t("Marzo")} </option>
                                    <option value="Abril"> {t("Abril")} </option>
                                    <option value="Mayo"> {t("Mayo")} </option>
                                    <option value="Junio"> {t("Junio")} </option>
                                    <option value="Julio"> {t("Julio")} </option>
                                    <option value="Agosto"> {t("Agosto")} </option>
                                    <option value="Septiembre"> {t("Septiembre")} </option>
                                    <option value="Octubre"> {t("Octubre")} </option>
                                    <option value="Noviembre"> {t("Noviembre")} </option>
                                    <option value="Diciembre"> {t("Diciembre")} </option>
                                  </Form.Select>
                                </div>
                                <div className="col-sm-4">
                                  <Form.Control className="input" placeholder="YYYY" />
                                </div>
                              </div>
                            </div>
                            <div className="d-flex mb-4">
                              <div className="row ml-4">
                                <div className="col-sm-4">
                                  <label>{t("To ")}</label>
                                </div>
                                <div className="col-sm-4">
                                  <Form.Select className="selectTo">
                                    <option value="No-Especificado"> {t("No-especificado")} </option>
                                    <option value="Enero"> {t("Enero")} </option>
                                    <option value="Febrero"> {t("Febrero")} </option>
                                    <option value="Marzo"> {t("Marzo")} </option>
                                    <option value="Abril"> {t("Abril")} </option>
                                    <option value="Mayo"> {t("Mayo")} </option>
                                    <option value="Junio"> {t("Junio")} </option>
                                    <option value="Julio"> {t("Julio")} </option>
                                    <option value="Agosto"> {t("Agosto")} </option>
                                    <option value="Septiembre"> {t("Septiembre")} </option>
                                    <option value="Octubre"> {t("Octubre")} </option>
                                    <option value="Noviembre"> {t("Noviembre")} </option>
                                    <option value="Diciembre"> {t("Diciembre")} </option>
                                  </Form.Select>
                                </div>
                                <div className="col-sm-4">
                                  <Form.Control className="input" placeholder="YYYY" />
                                </div>
                              </div>
                            </div>
                          </div>
                          <p>{t("Fields required")}</p>
                          {/* TODO: arreglar el metodo de link porque href es ilegal - funciona though*/}
                          <Button variant="success" type="submit">
                            <strong>{t("Save")}</strong>
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

export default ExperienceForm
