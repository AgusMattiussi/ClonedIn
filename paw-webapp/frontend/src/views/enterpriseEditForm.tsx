import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import EnterpriseDto from "../utils/EnterpriseDto"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate, useParams } from "react-router-dom"
import { useSharedAuth } from "../api/auth"
import { useGetEnterpriseById } from "../hooks/useGetEnterpriseById"
import { useGetCategories } from "../hooks/useGetCategories"
import { usePostEnterpriseInfo } from "../hooks/usePostEnterpriseInfo"
import { HttpStatusCode } from "axios"
import * as formik from "formik"
import * as yup from "yup"

function EditEnterpriseForm() {
  const navigate = useNavigate()
  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()

  const { getCategories } = useGetCategories()
  const { getEnterpriseById } = useGetEnterpriseById()
  const { modifyEnterpriseInfo } = usePostEnterpriseInfo()

  const [categoryList, setCategoryList] = useState([])

  document.title = t("Edit Page Title")

  const { Formik } = formik
  const re =
    /^((ftp|http|https):\/\/)?(www.)?(?!.*(ftp|http|https|www.))[a-zA-Z0-9_-]+(\.[a-zA-Z]+)+((\/)[\w#]+)*(\/\w+\?[a-zA-Z0-9_]+=\w+(&[a-zA-Z0-9_]+=\w+)*)?$/gm

  const schema = yup.object().shape({
    name: yup.string().max(50, t("Single Line Max Length") as string),
    city: yup.string().max(50, t("Single Line Max Length") as string),
    foundingYear: yup
      .number()
      .typeError(t("Invalid Number") as string)
      .min(1000, t("Invalid Year Min") as string)
      .max(new Date().getFullYear(), t("Invalid Year Max") as string),
    link: yup
      .string()
      .matches(re, t("Invalid URL") as string)
      .max(200, t("Multi Line Max Length") as string),
    aboutUs: yup.string().max(600, t("Long Line Max Length") as string),
  })
  const [enterprise, setEnterprise] = useState<EnterpriseDto | undefined>({} as EnterpriseDto)
  const [workers, setWorkers] = useState(enterprise?.workers)
  const [category, setCategory] = useState(enterprise?.categoryInfo?.name)

  useEffect(() => {
    const fetchEnterprise = async () => {
      const response = await getEnterpriseById(id)
      if (response.status === HttpStatusCode.Ok) {
        setEnterprise(response.data)
      } else {
        console.error("Error getting enterprise info:", response)
      }
    }

    const fetchCategories = async () => {
      const response = await getCategories()
      setCategoryList(response.data)
    }

    if (categoryList.length === 0) {
      fetchCategories()
      fetchEnterprise()
    }
  }, [id, getEnterpriseById, getCategories, categoryList.length, navigate])

  const handlePost = async (e: any) => {
    const name = e.name
    const city = e.city
    const year = e.foundingYear === "" ? enterprise?.yearFounded : e.foundingYear
    const link = e.link
    const aboutUs = e.aboutUs
    const response = await modifyEnterpriseInfo(id, name, aboutUs, city, category, link, workers, year)
    if (response.status === HttpStatusCode.Ok) {
      navigate(`/enterprises/${id}`)
    } else {
      console.error("Error updating enterprise info:", response)
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
                        city: "",
                        foundingYear: "",
                        link: "",
                        aboutUs: "",
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
                                placeholder={enterprise?.name}
                                value={values.name}
                                onChange={handleChange}
                                isInvalid={!!errors.name}
                              />
                              <Form.Control.Feedback type="invalid">{errors.name}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicLocation">
                              <Form.Control
                                name="city"
                                className="input"
                                placeholder={
                                  enterprise?.location === ""
                                    ? t("Location").toString()
                                    : enterprise?.location || undefined
                                }
                                value={values.city}
                                onChange={handleChange}
                                isInvalid={!!errors.city}
                              />
                            </Form.Group>
                            <div className="d-flex mb-4">
                              <label
                                className="area"
                                style={{
                                  width: "200px",
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
                                  width: "200px",
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
                                name="foundingYear"
                                className="input"
                                placeholder={
                                  enterprise?.yearFounded === undefined
                                    ? t("Founding Year").toString()
                                    : enterprise?.yearFounded.toString() || undefined
                                }
                                value={values.foundingYear}
                                onChange={handleChange}
                                isInvalid={!!errors.foundingYear}
                              />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicWebsite">
                              <Form.Control
                                name="link"
                                className="input"
                                placeholder={
                                  enterprise?.website === ""
                                    ? t("Website").toString()
                                    : enterprise?.website || undefined
                                }
                                value={values.link}
                                onChange={handleChange}
                                isInvalid={!!errors.link}
                              />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                              <Form.Control
                                name="aboutUs"
                                placeholder={
                                  enterprise?.description === ""
                                    ? t("About Us").toString()
                                    : enterprise?.description || undefined
                                }
                                as="textarea"
                                rows={3}
                                value={values.aboutUs}
                                onChange={handleChange}
                                isInvalid={!!errors.aboutUs}
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
