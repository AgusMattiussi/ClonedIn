import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import * as formik from 'formik';
import * as yup from 'yup';

function EditUserForm() {
  const [categoryList, setCategoryList] = useState([])
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
    navigate("/profiles")
  }

  const { t } = useTranslation()
  const navigate = useNavigate()

  document.title = t("Edit Page Title")

  const { Formik } = formik;

  const schema = yup.object().shape({
    name: yup.string().required('Required'),
  });

  return (
    <div>
      <Navigation />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>{t("Edit")}</strong>
                </h2>
                <p>{t("Fill some fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                  <Formik
                    validationSchema={schema}
                    initialValues={{
                      name: '',
                    }}
                    onSubmit={values => {
                      console.log(values);
                    }}
                  >
                  {({handleSubmit, handleChange, values, touched, errors }) => (
                    <Form className="msform" noValidate onSubmit={handleSubmit}>
                      <div className="form-card">
                        <h2 className="fs-title">{t("Basic Information")}</h2>
                        <Form.Group className="mb-3 mt-3" controlId="formBasicName">
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
                        <Form.Group className="mb-3" controlId="formBasicLocation">
                          <Form.Control className="input" placeholder={t("Location").toString()} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicPosition">
                          <Form.Control className="input" placeholder={t("Current Position").toString()} />
                        </Form.Group>
                        <div className="d-flex mb-4">
                          <label className="area">{t("Education Level")}</label>
                          <Form.Select className="selectFrom" aria-label="Default select example">
                            <option value="No-especificado">{t("No-especificado")}</option>
                            <option value="Primario">{t("Primario")}</option>
                            <option value="Secundario">{t("Secundario")}</option>
                            <option value="Terciario">{t("Terciario")}</option>
                            <option value="Graduado">{t("Graduado")}</option>
                            <option value="Postgrado">{t("Postgrado")}</option>
                          </Form.Select>
                        </div>
                        <div className="d-flex mb-4">
                          <label className="area">{t("Job Category")}</label>
                          <Form.Select className="selectFrom" aria-label="Default select example">
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
                          <Form.Control placeholder={t("About Me").toString()} as="textarea" rows={3} />
                        </Form.Group>
                      </div>
                      <p> {t("Fields required")}</p>
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

export default EditUserForm
