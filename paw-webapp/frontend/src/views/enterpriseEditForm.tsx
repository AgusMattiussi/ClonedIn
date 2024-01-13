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

function EditEnterpriseForm() {
  const navigate = useNavigate()
  const [categoryList, setCategoryList] = useState([])
  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const { loading, apiRequest } = useRequestApi()

  document.title = t("Edit Page Title")

  const { Formik } = formik

  const schema = yup.object().shape({
    name: yup.string().required(t('Required') as string),
  })
  const [location, setLocation] = useState("")
  const [workers, setWorkers] = useState("")
  const [category, setCategory] = useState("")
  const [link, setLink] = useState("")
  const [year, setYear] = useState("")
  const [description, setDescription] = useState("")

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
  }, [apiRequest])

  const handlePost = async (e: any) => {
    const name = e.name
    const response = await apiRequest({
      url: `/enterprises/${id}`,
      method: "PUT",
      body: {
        name,
        location,
        category,
        workers,
        year,
        link,
        description,
      },
    })
    console.log(response)
    if (response.status === HttpStatusCode.NoContent) {
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
                  <strong>{t("Edit")}</strong>
                </h2>
                <p>{t("Fill some fields")}.</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Formik
                      validationSchema={schema}
                      initialValues={{
                        name: "",
                      }}
                      onSubmit={(values) => {
                        handlePost(values)
                      }}
                    >
                      {({ handleSubmit, handleChange, values, touched, errors }) => (
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
                              <Form.Control.Feedback type="invalid">{errors.name}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicLocation">
                              <Form.Control
                                className="input"
                                placeholder={t("Location").toString()}
                                value={location}
                                onChange={(e) => setLocation(e.target.value)}
                              />
                            </Form.Group>
                            <div className="d-flex mb-4">
                              <label
                                className="area"
                                style={{
                                  width: "100px",
                                }}
                              >
                                {t("Quantity of employees")}
                              </label>
                              <Form.Select
                                className="selectFrom"
                                aria-label="Default select example"
                                value={workers}
                                onChange={(e) => setWorkers(e.target.value)}
                              >
                                <option value="No-especificado">{t("No-especificado")}</option>
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
                              <label
                                className="area"
                                style={{
                                  width: "100px",
                                }}
                              >
                                {t("Job Category")}
                              </label>
                              <Form.Select
                                className="selectFrom"
                                aria-label="Default select example"
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
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                              />
                            </Form.Group>
                          </div>
                          <p>{t("Fields required")}</p>
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

export default EditEnterpriseForm
