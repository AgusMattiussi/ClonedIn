import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate, useParams } from "react-router-dom"
import { useSharedAuth } from "../api/auth"
import * as formik from "formik"
import * as yup from "yup"
import { useRequestApi } from "../api/apiRequest"
import { HttpStatusCode } from "axios"

function JobOfferForm() {
  const navigate = useNavigate()
  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const { loading, apiRequest } = useRequestApi()
  const [categoryList, setCategoryList] = useState([])

  document.title = t("Job Offer Page Title")

  const { Formik } = formik

  const schema = yup.object().shape({
    position: yup.string().required(t('Required') as string).max(50, t('Single Line Max Length') as string),
    salary: yup.number().typeError(t('Invalid Number') as string).min(1, t('Invalid Salary Min') as string).max(1000000000, t('Invalid Salary Max') as string),
    description: yup.string().max(5000, t('Description Max Length') as string),
    skill1: yup.string().max(50, t('Single Line Max Length') as string),
    skill2: yup.string().max(50, t('Single Line Max Length') as string),
    skill3: yup.string().max(50, t('Single Line Max Length') as string),
    skill4: yup.string().max(50, t('Single Line Max Length') as string),
  })
  const [category, setCategory] = useState("No-Especificado")
  const [modality, setModality] = useState("Remoto")

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

  const handlePost = async (e: any) => {
    const jobPosition = e.position
    const jobDescription = e.description
    const salary = e.salary
    const skill1 = e.skill1
    const skill2 = e.skill2
    const skill3 = e.skill3
    const skill4 = e.skill4
    const response = await apiRequest({
      url: `/enterprises/${id}/jobOffers`,
      method: "POST",
      body: {
        jobPosition,
        jobDescription,
        salary,
        category,
        modality,
        skill1,
        skill2,
        skill3,
        skill4,
      },
    })
    if (response.status === HttpStatusCode.Created) {
      navigate(`/enterprises/${id}`)
    } else {
      //TODO: manejar error
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
                  <strong> {t("JobOffer add")} </strong>
                </h2>
                <p>{t("Fill all fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Formik
                      validationSchema={schema}
                      initialValues={{
                        position: "",
                        salary: "",
                        description: "",
                        skill1: "",
                        skill2: "",
                        skill3: "",
                        skill4: "",
                      }}
                      onSubmit={(values) => {
                        handlePost(values)
                      }}
                    >
                      {({ handleSubmit, handleChange, values, touched, errors }) => (
                        <Form className="msform" onSubmit={handleSubmit}>
                          <div className="form-card">
                            <h2 className="fs-title"> {t("Job Offer")} </h2>
                            <Form.Group className="mb-3 mt-3" controlId="formBasicPosition">
                              <Form.Control
                                name="position"
                                className="input"
                                placeholder={t("Position*").toString()}
                                value={values.position}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.position}
                              />
                              <Form.Control.Feedback type="invalid">{errors.position}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicSalary">
                              <Form.Control
                                name="salary"
                                className="input"
                                placeholder={t("Salary").toString()}
                                value={values.salary}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.salary}
                              />
                              <Form.Control.Feedback type="invalid">{errors.salary}</Form.Control.Feedback>
                            </Form.Group>
                            <div className="d-flex">
                            <Form.Group controlId="formBasicSkill">
                              <Form.Control
                                name="skill1"
                                className="input"
                                placeholder={t("Required Skills").toString()}
                                value={values.skill1}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.skill1}
                              />
                              <Form.Control.Feedback type="invalid">{errors.skill1}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group controlId="formBasicSkill">
                              <Form.Control
                                name="skill2"
                                className="input"
                                placeholder={t("Required Skills").toString()}
                                value={values.skill2}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.skill2}
                              />
                              <Form.Control.Feedback type="invalid">{errors.skill2}</Form.Control.Feedback>
                            </Form.Group>
                            </div>
                            <div className="d-flex mb-4">
                            <Form.Group className="mb-3" controlId="formBasicSkill">
                              <Form.Control
                                name="skill3"
                                className="input"
                                placeholder={t("Required Skills").toString()}
                                value={values.skill3}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.skill3}
                              />
                              <Form.Control.Feedback type="invalid">{errors.skill3}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3 ml-3" controlId="formBasicSkill">
                              <Form.Control
                                name="skill4"
                                className="input"
                                placeholder={t("Required Skills").toString()}
                                value={values.skill4}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.skill4}
                              />
                              <Form.Control.Feedback type="invalid">{errors.skill4}</Form.Control.Feedback>
                            </Form.Group>
                            </div>
                            <div className="d-flex mb-4">
                              <label className="area mx-2 py-1" style={{ width: "100px" }}>
                                {t("Modality*")}
                              </label>
                              <Form.Select
                                className="selectFrom"
                                aria-label="Default select example"
                                value={modality}
                                onChange={(e) => setModality(e.target.value)}
                                style={{ width: "70%" }}
                              >
                                <option value="Remoto">{t("Remoto")}</option>
                                <option value="Presencial">{t("Presencial")}</option>
                                <option value="Mixto">{t("Mixto")}</option>
                              </Form.Select>
                            </div>
                            <div className="d-flex mb-4">
                              <label className="area mx-2 py-1" style={{ width: "100px" }}>
                                {t("Job Category")}
                              </label>
                              <Form.Select
                                className="selectFrom"
                                aria-label="Default select example"
                                value={category}
                                onChange={(e) => setCategory(e.target.value)}
                                style={{ width: "70%" }}
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
                                placeholder={t("Description").toString()}
                                as="textarea"
                                rows={3}
                                value={values.description}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.description}
                              />
                              <Form.Control.Feedback type="invalid">{errors.description}</Form.Control.Feedback>
                            </Form.Group>
                          </div>
                          <p>{t("Fields required")}</p>
                          <Button variant="success" type="submit">
                            <strong> {t("Create")} </strong>
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

export default JobOfferForm
